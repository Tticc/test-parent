package com.tester.thirdparty.apollo;

import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.*;
import com.tester.thirdparty.apollo.request.ApolloCommonRequest;
import com.tester.thirdparty.apollo.response.ApolloCommonResponse;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 温昌营
 * @Date 2021-10-22 13:55:01
 */
public class ApolloHelper {


    private ApolloOpenApiClient client;

    public ApolloHelper(String portalUrl, String token){
        client = ApolloOpenApiClient.newBuilder()
                .withPortalUrl(portalUrl)
                .withToken(token)
                .build();
    }

    /**
     * 获取所有的app
     * @Date 11:45 2021/10/22
     * @Author 温昌营
     **/
    public ApolloCommonResponse<OpenAppDTO> getAllApps() {
        return new ApolloCommonResponse<OpenAppDTO>().setData(client.getAllApps());
    }

    /**
     * 获取app信息<br/>
     * [{"clusters":["default"],"env":"DEV"},{"clusters":["default"],"env":"FAT"},{"clusters":["default"],"env":"UAT"}]
     * @param appId
     * @return java.util.List<com.ctrip.framework.apollo.openapi.dto.OpenEnvClusterDTO>
     * @Date 11:45 2021/10/22
     * @Author 温昌营
     **/
    public ApolloCommonResponse<OpenEnvClusterDTO> getEnvClusterInfo(String appId) {
        return new ApolloCommonResponse<OpenEnvClusterDTO>().setData(client.getEnvClusterInfo(appId));
    }

    /**
     * 新增一条配置。如果原来有，报错
     * @Date 11:48 2021/10/22
     * @Author 温昌营
     **/
    public ApolloCommonResponse<OpenItemDTO> createItem(ApolloCommonRequest<OpenItemDTO> request) {
        String appId = request.getAppId();
        String env = request.getEnv();
        String clusterName = request.getClusterName();
        String namespaceName = request.getNamespaceName();
        List<OpenItemDTO> reqDatas = request.getDatas();
        if(CollectionUtils.isEmpty(reqDatas)){
            return new ApolloCommonResponse();
        }

        List<OpenItemDTO> resDatas = new ArrayList<>();
        for (OpenItemDTO reqData : reqDatas) {
            reqData.setDataChangeCreatedBy(request.getBy());
            resDatas.add((OpenItemDTO) client.createItem(appId, env, clusterName, namespaceName, reqData));
        }
        return new ApolloCommonResponse<OpenItemDTO>().setData(reqDatas);
    }

    /**
     * 更新一条配置。如果原来没有，报错
     * @Date 11:50 2021/10/22
     * @Author 温昌营
     **/
    public void updateItem(ApolloCommonRequest<OpenItemDTO> request) {
        String appId = request.getAppId();
        String env = request.getEnv();
        String clusterName = request.getClusterName();
        String namespaceName = request.getNamespaceName();
        List<OpenItemDTO> reqDatas = request.getDatas();
        if(CollectionUtils.isEmpty(reqDatas)){
            return ;
        }
        for (OpenItemDTO reqData : reqDatas) {
            reqData.setDataChangeLastModifiedBy(request.getBy());
            client.updateItem(appId, env, clusterName, namespaceName, reqData);
        }
    }

    /**
     * 新增或更新配置
     * @Date 11:51 2021/10/22
     * @Author 温昌营
     **/
    public void createOrUpdateItem(ApolloCommonRequest<OpenItemDTO> request) {
        String appId = request.getAppId();
        String env = request.getEnv();
        String clusterName = request.getClusterName();
        String namespaceName = request.getNamespaceName();
        List<OpenItemDTO> reqDatas = request.getDatas();
        if(CollectionUtils.isEmpty(reqDatas)){
            return ;
        }
        for (OpenItemDTO reqData : reqDatas) {
            reqData.setDataChangeCreatedBy(request.getBy());
            client.createOrUpdateItem(appId, env, clusterName, namespaceName, reqData);
        }
    }

    /**
     * 删除
     * @Date 14:36 2021/10/22
     * @Author 温昌营
     **/
    public void removeItem(ApolloCommonRequest<OpenItemDTO> request){
        String appId = request.getAppId();
        String env = request.getEnv();
        String clusterName = request.getClusterName();
        String namespaceName = request.getNamespaceName();
        List<OpenItemDTO> reqDatas = request.getDatas();
        if(CollectionUtils.isEmpty(reqDatas)){
            return ;
        }
        for (OpenItemDTO reqData : reqDatas) {
            client.removeItem(appId, env, clusterName, namespaceName, reqData.getKey(),request.getBy());
        }
    }

    /**
     * 发布
     * @Date 11:52 2021/10/22
     * @Author 温昌营
     **/
    public ApolloCommonResponse<OpenReleaseDTO> publishNamespace(ApolloCommonRequest<NamespaceReleaseDTO> request) {
        String appId = request.getAppId();
        String env = request.getEnv();
        String clusterName = request.getClusterName();
        String namespaceName = request.getNamespaceName();
        List<NamespaceReleaseDTO> reqDatas = request.getDatas();
        if(CollectionUtils.isEmpty(reqDatas)){
            return new ApolloCommonResponse();
        }
        List<OpenReleaseDTO> resDatas = new ArrayList<>();
        for (NamespaceReleaseDTO reqData : reqDatas) {
            resDatas.add(client.publishNamespace(appId, env, clusterName, namespaceName, reqData));
        }
        return new ApolloCommonResponse<OpenReleaseDTO>().setData(resDatas);
    }

    /**
     * 获取所有配置
     * @Date 11:52 2021/10/22
     * @Author 温昌营
     **/
    public OpenNamespaceDTO getNamespace(ApolloCommonRequest request) {
        String appId = request.getAppId();
        String env = request.getEnv();
        String clusterName = request.getClusterName();
        String namespaceName = request.getNamespaceName();
        return client.getNamespace(appId, env, clusterName, namespaceName);
    }

    /**
     * 获取所有配置
     * @Date 11:52 2021/10/22
     * @Author 温昌营
     **/
    public List<OpenNamespaceDTO> getAllItem(String appId, String env, String clusterName) {
        return client.getNamespaces(appId, env, clusterName);
    }


}
