package com.tester.testerstarter.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * spring 管理的bean。
 * @Author 温昌营
 * @Date
 */
@Slf4j
public class RestTemplateUtil implements InitializingBean {

    @Autowired
    private RestTemplate restTemplate;

    public <TInput, TOutput> TOutput postRequest(String url,TInput param,Class<TOutput> clazz){
        String jsonString = JSONObject.toJSONString(param);
        JSONObject content = JSONObject.parseObject(jsonString);
        JSONObject jsonObject = httpPostRequest(content, url);
        if(null == jsonObject){
            return null;
        }
        return jsonObject.toJavaObject(clazz);
    }

    public <TOutput> TOutput getRequest(String url,Class<TOutput> clazz){
        JSONObject jsonObject = httpGetRequest(url);
        if(null == jsonObject){
            return null;
        }
        return jsonObject.toJavaObject(clazz);
    }

    public JSONObject httpPostRequest(JSONObject content, String url) {
        int maxTime = 3;
        int count = 0;
        boolean failed = true;
        log.info("post请求url:{},入参:{}", url,content);
        ResponseEntity<JSONObject> httpResult = null;
        try {
            do {
                httpResult = restTemplate.postForEntity(url, content, JSONObject.class);
                if (httpResult.getStatusCodeValue() == 200) {
                    // 设置failed=false，跳出循环
                    failed = false;
                }
            } while (failed && maxTime > ++count);
        }catch (Exception e){
            log.error("http POST 请求失败。url:{}",url,e);
            return null;
        }
        log.info("post请求出参：{}",httpResult);
        return httpResult.getBody();
    }

    private JSONObject httpGetRequest(String url){
        int maxTime = 3;
        int count = 0;
        boolean failed = true;
        log.info("get请求url:{}", url);
        ResponseEntity<JSONObject> httpResult;
        try {
            do {
                httpResult = restTemplate.getForEntity(url, JSONObject.class);
                if (httpResult.getStatusCodeValue() == 200) {
                    // 设置failed=false，跳出循环
                    failed = false;
                }
            } while (failed && maxTime > ++count);
        }catch (Exception e){
            log.error("http GET 请求失败。url:{}",url,e);
            return null;
        }
        log.info("get请求出参：{}",httpResult);
        return httpResult.getBody();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 暂时不需要
        // 获取accesstoken失败的时候，返回的数据流类型是：octet-stream 不是json，无法读取。所以即使加了这个errorhandler也无法处理
//        restTemplate.setErrorHandler(new MyResponseErrorHandler());
    }


}
