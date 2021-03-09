package com.tester.testercommon.util.http;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * https
 */
@Slf4j
public class HttpsClient {

    public static SSLSocketFactory ssf;

    public static final String GET_METHOD = "GET";

    public static final String POST_METHOD = "POST";

    static{
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从SSLContext对象中得到SSLSocketFactory对象
            ssf = sslContext.getSocketFactory();
        } catch (Exception e) {
            log.error("init SSLSocketFactory error", e);
        }
    }


    public static JSONObject commonGetRequest(String url){
        String wenc1 = httpsRequestRetry(url,
                GET_METHOD,
                null,
                0,
                false,
                true,
                "commonGetRequest");
        return (JSONObject)JSONObject.parse(wenc1);
    }
    public static JSONObject commonPostRequest(JSONObject obj, String url){
        String wenc1 = httpsRequestRetry(url,
                POST_METHOD,
                obj.toString(),
                0,
                false,
                true,
                "commonPostRequest");
        return (JSONObject)JSONObject.parse(wenc1);
    }
    /**
     *
     * @param requestUrl URL
     * @param requestMethod post/get
     * @param outputStr 请求数据
     * @param retryCount 传0，重试三次
     * @param isBizmpvers 传false
     * @param isHttps http/https
     * @param requestId 日志追踪用
     * @return
     */
    public static String httpsRequestRetry(String requestUrl,
                                           String requestMethod,
                                           String outputStr,
                                           int retryCount,
                                           boolean isBizmpvers,
                                           boolean isHttps,
                                           String requestId) {
        long strat = System.currentTimeMillis();
        if(retryCount>2){//最多重试3次
            return null;
        }
        retryCount = retryCount + 1;
        String thisRequestId = requestId+"_"+retryCount;
        String jsonObject = null;
        log.info("httprequest start uuid=" + thisRequestId);
        HttpURLConnection httpUrlConn = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(requestUrl);
            if (isHttps) {
                HttpsURLConnection httpsUrlConn = (HttpsURLConnection) url.openConnection();
                httpUrlConn = httpsUrlConn;
                httpsUrlConn.setSSLSocketFactory(ssf);
                if(isBizmpvers){
                    httpsUrlConn.addRequestProperty("Bizmp-Version", "1.0");
                }
            }
            else {
                httpUrlConn = (HttpURLConnection) url.openConnection();
            }
            httpUrlConn.setConnectTimeout(10*1000);
            httpUrlConn.setReadTimeout(7 * 1000);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if (GET_METHOD.equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            inputStream = httpUrlConn.getInputStream();
            inputStreamReader = new InputStreamReader(
                    inputStream, "utf-8");
            bufferedReader = new BufferedReader(
                    inputStreamReader);

            String str;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            jsonObject = buffer.toString();
            if (StringUtils.isEmpty(jsonObject)) {
                log.error("empty result");
            }
        } catch (IOException ce) {
            log.error("httprequest error uuid=" + thisRequestId
                    + ";server connection timed out.", ce);
            return httpsRequestRetry(requestUrl, requestMethod, outputStr, retryCount, isBizmpvers, isHttps, requestId);
        } catch (Exception e) {
            log.error("httprequest error uuid=" + thisRequestId + ";", e);
        } finally {
            // 释放资源
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    log.error("httprequest BufferedReader close error uuid=" + thisRequestId, e);
                }
            }
            if(inputStreamReader != null){
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    log.error("httprequest InputStreamReader close error uuid=" + thisRequestId, e);
                }
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("httprequest InputStream close error uuid=" + thisRequestId, e);
                }
            }
            if(httpUrlConn != null){
                httpUrlConn.disconnect();
            }
            log.info("httprequest end uuid=" + thisRequestId + ";timeout"
                    + (System.currentTimeMillis() - strat) + ";url="
                    + requestUrl + ";outputStr=" + outputStr + ";return="
                    + jsonObject);
        }
        return jsonObject;
    }
}
