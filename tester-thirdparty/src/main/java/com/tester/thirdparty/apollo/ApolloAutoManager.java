package com.tester.thirdparty.apollo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.openapi.dto.NamespaceReleaseDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenAppDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.util.http.HttpsClient;
import com.tester.thirdparty.apollo.request.ApolloCommonRequest;
import com.tester.thirdparty.apollo.response.ApolloCommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 温昌营
 * @Date 2021-10-22 11:21:08
 */
@Service
@Slf4j
public class ApolloAutoManager implements InitializingBean {


    // 客户端
    private ApolloHelper client;

    @Autowired
    private ApolloConfigurer apolloConfigurer;

    /**
     * 默认更新账号。需要是apollo管理后台实际存在的账号
     **/
    private String DEFAULT_MODIFIER = "auto-release";

    /**
     * 从发版单读取
     * @Date 17:21 2021/10/22
     * @Author 温昌营
     **/
    public void updateFromExcel(List<ApolloCommonRequest<OpenItemDTO>> apolloItems) throws Exception {
        log.info("apollo更新准备");
        List<NamespaceReleaseDTO> relDatas = new ArrayList<>();
        NamespaceReleaseDTO environment = new NamespaceReleaseDTO();
        environment.setReleaseComment(DEFAULT_MODIFIER);
        environment.setReleasedBy(DEFAULT_MODIFIER);
        environment.setReleaseTitle(DEFAULT_MODIFIER);
        relDatas.add(environment);
        log.info("apollo更新中");
        for (ApolloCommonRequest<OpenItemDTO> req : apolloItems) {
            String[] split = req.getClusterName().trim().split(",");
            for (String s : split) {
                req.setClusterName(s.trim());
                log.info("appId = {}, env = {}, clusterName = {}, namespaceName = {}, size = {}", req.getAppId(), req.getEnv(), req.getClusterName(), req.getNamespaceName(), req.getDatas().size());
                log.info("\n{}",JSON.toJSONString(req.getDatas()));

                log.info("更新...");
                createOrUpdate(req.getAppId(), req.getEnv(), req.getClusterName(), req.getNamespaceName(), req.getDatas());
                log.info("已更新，发布...");
                publish(req.getAppId(), req.getEnv(), req.getClusterName(), req.getNamespaceName(), relDatas);
                log.info("已发布");
                log.info("完成\n");
            }
        }
        log.info("apollo更新完成");
    }



    public void createOrUpdate(String appId, String env, String clusterName, String namespaceName, List<OpenItemDTO> datas){
        ApolloCommonRequest<OpenItemDTO> request = new ApolloCommonRequest<>();
        request.setAppId(appId)
                .setEnv(env)
                .setClusterName(clusterName)
                .setNamespaceName(namespaceName)
                .setBy(DEFAULT_MODIFIER);
        request.setDatas(datas);
        log.info("创建或更新数量:{}",datas.size());
        client.createOrUpdateItem(request);
    }


    public void remove(String appId, String env, String clusterName, String namespaceName, List<OpenItemDTO> datas){
        ApolloCommonRequest<OpenItemDTO> request = new ApolloCommonRequest<>();
        request.setAppId(appId)
                .setEnv(env)
                .setClusterName(clusterName)
                .setNamespaceName(namespaceName)
                .setBy(DEFAULT_MODIFIER);
        request.setDatas(datas);
        log.info("移除数量:{}",datas.size());
        client.removeItem(request);
    }

    public void publish(String appId, String env, String clusterName, String namespaceName, List<NamespaceReleaseDTO> datas){
        ApolloCommonRequest<NamespaceReleaseDTO> request = new ApolloCommonRequest<>();
        request.setAppId(appId)
                .setEnv(env)
                .setClusterName(clusterName)
                .setNamespaceName(namespaceName)
                .setBy(DEFAULT_MODIFIER);
        request.setDatas(datas);
        client.publishNamespace(request);
    }





    /**
     * 分配所有App权限，可以用来初始化。
     * @return void
     * @Date 14:13 2021/10/22
     * @Author 温昌营
     **/
    public void assignPermission(String cookieStr) throws BusinessException {
        ApolloCommonResponse<OpenAppDTO> resData = client.getAllApps();
        List<OpenAppDTO> allApps;
        if(!CollectionUtils.isEmpty(allApps = resData.getData())){
            for (OpenAppDTO allApp : allApps) {
                assignPermission(allApp.getAppId(), apolloConfigurer.getApolloToken(), cookieStr);
            }
        }
    }

    /**
     * 分配所有App权限，可以用来初始化。
     * @return void
     * @Date 14:13 2021/10/22
     * @Author 温昌营
     **/
    public void assignPermission(String token, String cookieStr) throws BusinessException {
        ApolloCommonResponse<OpenAppDTO> resData = client.getAllApps();
        List<OpenAppDTO> allApps;
        if(!CollectionUtils.isEmpty(allApps = resData.getData())){
            for (OpenAppDTO allApp : allApps) {
                assignPermission(allApp.getAppId(),token,cookieStr);
            }
        }
    }

    /**
     * 分配权限<br/>
     * 默认地，token没有任何App的权限，需要到后台管理配置，或者使用此方法配置。<br/>
     * 没能实现自动登录，所以传入cookie
     * @Date 11:24 2021/10/22
     * @Author 温昌营
     **/
    public void assignPermission(String appId, String token, String cookieStr) throws BusinessException {
        log.info("授权开始。appId:{}, token:{}",appId, token);
        String url = apolloConfigurer.getApolloPortalUrl() + "consumers/"+token+"/assign-role?type=AppRole";
        JSONObject jo = new JSONObject();
        jo.put("appId",appId);

        Map<String, String> params = new HashMap<>();
        params.put("Cookie",cookieStr);
        params.put("Content-Type","application/json;charset=UTF-8");

        String jsonObject = HttpsClient.requestForString(url,HttpsClient.POST_METHOD,jo,params);

        // 如果转换异常，则说明授权失败，可能是session过期
        JSONArray objects = JSONArray.parseArray(jsonObject);
        log.info("objects:{}",objects);
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        client = new ApolloHelper(apolloConfigurer.getApolloPortalUrl(),apolloConfigurer.getApolloToken());
    }



    public ApolloHelper getClient() {
        return client;
    }
}
