package com.tester.thirdparty.xxljob;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.http.HttpsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author 温昌营
 * @Date 2021-11-1 14:41:06
 */
@Service
@Slf4j
public class JobAutoManager implements InitializingBean {


    Pattern splitRegex = Pattern.compile("<button class=\"btn btn-warning(.*?)</button>");
    Pattern parseRegex = Pattern.compile("(.*?)(id=\")(.*?)(\".*?)(appName=\")(.*?)(\".*)");

    @Autowired
    private JobConfigurer jobConfigurer;

    private final Map<String, String> groupMap = new HashMap<>();


    /**
     * 检查任务是否已存在
     * @Date 17:07 2021/11/1
     * @Author 温昌营
     **/
    public boolean alreadyHad(String jobHandler, JSONArray list){
        for (Object o : list) {
            JSONObject s = (JSONObject)o;
            Object value = s.get("executorHandler");
            if(Objects.equals(value,jobHandler)){
                return true;
            }
        }
        return false;
    }

    /**
     * 新增job
     * @Date 16:43 2021/11/1
     * @Author 温昌营 
     **/
    public void addJob(String cookie, Map<String, String> reqParam) throws Exception {
        log.info("开始创建job。任务描述：{}, bean名称：{}",reqParam.get("jobDesc"),reqParam.get("executorHandler"));
        StringBuilder sb = new StringBuilder();
        sb.append(jobConfigurer.getJobUrl()).append("/jobinfo/add").append("?");

        // 拼接请求地址。http://dev-xxx.com/job/jobinfo/add?alarmEmail=xxx%40com.cn&executorParam=xx&cronGen_display=0+0%2F1+*+*+*+%3F&executorBlockStrategy=SERIAL_EXECUTION&executorRouteStrategy=FIRST&author=%E6%B8%A9%E6%98%8C%E8%90%A5&jobCron=0+0%2F1+*+*+*+%3F&jobGroup=33&glueRemark=&jobDesc=desc&glueType=BEAN&executorHandler=jobName001&executorFailRetryCount=0&executorTimeout=0
        Set<Map.Entry<String, String>> entries = reqParam.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(),"UTF-8")).append("&");
        }
        String s = sb.toString();
        String url = s.substring(0, s.length()-1);
        Map<String, String> params = new HashMap<>();
        params.put("Cookie",cookie);
        params.put("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        String jsonObject = HttpsClient.requestForString(url,HttpsClient.POST_METHOD,null,params);
        JSONObject jsonObject1 = JSON.parseObject(jsonObject, JSONObject.class);
        if(!Objects.equals(jsonObject1.get("code"), 200)){
            log.error("job创建失败。请求参数：\n {} \n请求结果:\n {}",reqParam,jsonObject);
        }else {
            log.info("job创建成功");
        }
    }


    public JSONArray getList(String cookie, String groupId) throws BusinessException {
        String url = jobConfigurer.getJobUrl()+"/jobinfo/pageList"+"?jobGroup="+groupId+"&triggerStatus=-1&jobDesc=&executorHandler=&author=&start=0&length=10";
        Map<String, String> params = new HashMap<>();
        params.put("Cookie",cookie);
        params.put("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        String jsonObject = HttpsClient.requestForString(url,HttpsClient.POST_METHOD,null,params);
        JSONObject jsonObject1 = JSON.parseObject(jsonObject, JSONObject.class);
        return  (JSONArray)jsonObject1.get("data");
    }



    /**
     * 登录获取cookie
     * @Date 16:53 2021/11/1
     * @Author 温昌营
     **/
    public String getCookie(String usr, String pwd) throws BusinessException {
        AtomicReference<String> cookie = new AtomicReference<String>("");
        String path = jobConfigurer.getJobUrl() + "/login?userName="+usr+"&password="+pwd;
        Map<String, String> params = new HashMap<>();
        params.put("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");

        HttpsClient.requestForString(path,HttpsClient.POST_METHOD,null,params,null,(httpUrlConn) -> {
            Map<String, List<String>> headerFields;
            if(null == httpUrlConn || (headerFields = httpUrlConn.getHeaderFields()) == null){
                return;
            }
            List<String> strings = headerFields.get("Set-Cookie");
            if(CollectionUtils.isEmpty(strings)){
                return;
            }
            cookie.set(strings.get(0));
        });
        return cookie.get();
    }


    /**
     * 构建创建job的请求参数
     * @param groupId 执行器id
     * @param jobHandler JobHandler
     * @param desc 任务描述
     * @param cron job运行周期cron
     * @param author 负责人
     * @param alarmEmail 报警邮件
     * @param executorParam 任务参数
     * @Date 16:53 2021/11/1
     * @Author 温昌营
     **/
    public Map<String,String> buildReqParam(String groupId,
                                                   String jobHandler,
                                                   String desc,
                                                   String cron,
                                                   String author,
                                                   String alarmEmail,
                                                   String executorParam){
        Map<String,String> requestInfo = new HashMap<>();
        requestInfo.put("jobGroup", groupId); // "33"
        // 任务描述
        requestInfo.put("jobDesc", desc); // "xxxJob"
        // 执行器路由策略
        requestInfo.put("executorRouteStrategy", "FIRST");

        requestInfo.put("cronGen_display", cron); // "0 0/1 * * * ?"
        requestInfo.put("jobCron", cron);
        // GLUE类型    #com.xxl.job.core.glue.GlueTypeEnum
        requestInfo.put("glueType", "BEAN");
        // GLUE备注
        requestInfo.put("glueRemark", "");
        // 执行器，任务Handler名称
        requestInfo.put("executorHandler", jobHandler);
        // 阻塞处理策略
        requestInfo.put("executorBlockStrategy", "SERIAL_EXECUTION");
        // 任务执行超时时间，单位秒
        requestInfo.put("executorTimeout", "0");
        // 失败重试次数
        requestInfo.put("executorFailRetryCount", "0");
        // 负责人
        requestInfo.put("author", author);
        // 报警邮件
        requestInfo.put("alarmEmail", alarmEmail); // xxx@com.cn
        // 执行器，任务参数
        requestInfo.put("executorParam", executorParam); // xxx
        return requestInfo;
    }


    /**
     * 获取
     * @Date 17:51 2021/11/1
     * @Author 温昌营
     **/
    public Map<String, String> getGroupMap() {
        return groupMap;
    }

    /**
     * 重新构建groupMap
     * @Date 17:52 2021/11/1
     * @Author 温昌营
     **/
    public Map<String, String> reBuildGroupMap() throws BusinessException {
        String cookie = getCookie(jobConfigurer.getJobUsr(), jobConfigurer.getJobPwd());
        Map<String, String> map = parseGroupId(cookie);
        synchronized(groupMap) {
            groupMap.putAll(map);
        }
        return groupMap;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        String cookie = getCookie(jobConfigurer.getJobUsr(), jobConfigurer.getJobPwd());
        if(StringUtils.isEmpty(cookie)){
            throw new Exception("获取xxlJob cookie失败！");
        }
        groupMap.putAll(parseGroupId(cookie));
    }


    /**
     * 读取group
     * @Date 17:48 2021/11/1
     * @Author 温昌营
     **/
    public Map<String, String> parseGroupId(String cookie) throws BusinessException {
        String url = jobConfigurer.getJobUrl()+"/jobgroup";
        Map<String, String> params = new HashMap<>();
        params.put("Cookie",cookie);
        String jsonObject = HttpsClient.requestForString(url,HttpsClient.GET_METHOD,null,params);
        return doParse(jsonObject);
    }

    /**
     * 分解、解析
     * @Date 17:47 2021/11/1
     * @Author 温昌营
     **/
    private Map<String, String> doParse(String line){
        Map<String, String> res = new HashMap<>();
        Matcher m1 = splitRegex.matcher(line);
        while (m1.find( )) {
            // <button class="btn btn-warning btn-xs update"  id="31"    appName="middleend-tenant-job"   title="中台商户执行器"   order="101"     addressType="0"        addressList="10.10.61.109:9999" >编辑</button>
            String group = m1.group(1);
            Matcher matcher = parseRegex.matcher(group);
            if(matcher.matches()){
                // key=appName, value=groupid
                // 中台商户执行器 -> 31
                res.put(matcher.group(6), matcher.group(3));
            }
        }
        return res;
    }
}
