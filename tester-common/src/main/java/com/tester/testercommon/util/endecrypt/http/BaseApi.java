package com.tester.testercommon.util.endecrypt.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.tester.base.dto.controller.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@Slf4j
public abstract class BaseApi {

    protected ApiServiceOption serviceOption;

    protected <T extends RestResult> T call(Object cmd, TypeReference<T> responseTypeRef) {
        T resp;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = getHttpUrl();

            // 生成签名
            String signature = SignatureGeneratorUtil.generateSignature(Constants.secretKey, cmd);

            String httpMethod = serviceOption.getHttpMethod();
            if ("post".equals(httpMethod)) {
                HttpPost httpPost = new HttpPost(url);

                httpPost.addHeader("AccessKey", Constants.accessKey);
                httpPost.addHeader("Signature", signature);
                // 设置参数
                String jsonParams = JSON.toJSONString(cmd);
                if (StringUtils.isNotBlank(Constants.clientPrivateKey)) {
                    // 对请求体进行非对称加密
                    byte[] encryptReq = EccCryptoUtil.encrypt(jsonParams, Constants.serverPublicKey);
                    httpPost.addHeader("Ecc-Crypto-Enable", "true");
                    httpPost.setHeader("Content-Type", "application/octet-stream");
                    httpPost.setHeader("Accept", "application/octet-stream");
                    ByteArrayEntity entity = new ByteArrayEntity(encryptReq);
                    // 设置请求体
                    httpPost.setEntity(entity);
                    // 执行请求
                    CloseableHttpResponse response = httpClient.execute(httpPost);
                    // 解密响应体
                    byte[] encryptResp = EntityUtils.toByteArray(response.getEntity());
                    resp = JSON.parseObject(EccCryptoUtil.decrypt(encryptResp, Constants.clientPrivateKey), responseTypeRef,
                            Feature.OrderedField);
                } else {
                    // 请求体明文传输
                    httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
                    httpPost.setHeader("Accept", "application/json;charset=UTF-8");
                    StringEntity entity = new StringEntity(jsonParams, "UTF-8");
                    // 设置请求体
                    httpPost.setEntity(entity);

                    // 执行请求
                    CloseableHttpResponse response = httpClient.execute(httpPost);
                    resp = JSON.parseObject(EntityUtils.toString(response.getEntity()), responseTypeRef, Feature.OrderedField);
                }
            } else {
                throw new RuntimeException("Unsupported http method: " + httpMethod);
            }
            if (resp == null) {
                return buildFailure(responseTypeRef, new RuntimeException("response is empty"));
            }
            if(resp.getCode() != 200) {
                log.error("请求异常，response：{}", resp);
                return buildFailure(responseTypeRef, new RuntimeException("请求异常，请检查网络或密钥等信息。。"));
            }
            return resp;
        } catch (Exception e) {
            log.error("请求异常：", e);
            return buildFailure(responseTypeRef, e);
        }
    }

    private String getHttpUrl() {
        String baseUrl = Constants.BASE_HTTP_API_URL;
        if (serviceOption.getBaseHttpUrl() != null) {
            baseUrl = serviceOption.getBaseHttpUrl();
        }
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        return baseUrl + serviceOption.httpUrl();
    }

    private <T extends RestResult> T buildFailure(TypeReference<T> responseTypeRef, Exception e) {
        // 根据需要实现具体的失败处理逻辑
        // 示例：
        return JSON.parseObject("{\"code\":\"500\",\"message\":\"" + e.getMessage() + "\"}", responseTypeRef);
    }
}