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
 * for test
 */
@Slf4j
public class HttpClient {
    private static String getUrl =  "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=%s&userid=%s";
    private static String updateUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token=%s";
    private static String token = "S7sq-U0BdxY7JU7MRFh0Zm9XwXQanEyhPm2zOy6uNUFxwaL_j1YkzzhkS5wuRc3dpYBGEY_9NurTPOJIxDcfFVfRGMRDtfZGcLgss5UBsCYb3uXorREKwynKrmANpQmc4DLLoa4YXCs2Xw2JMdmEC_t_Y8sJjr8BeL3gPDAdbbqLRALNr9JDN7r2t_01oraoNM92unDNRdjl15FAdf__JA";
    private static String userId = "liufei";
    public static SSLSocketFactory ssf;
    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";

    public static void main(String[] args){
        String wenc = httpsRequestRetry(String.format(getUrl, token, userId),
                GET_METHOD,
                null,
                0,
                false,
                true,
                "wenc");
        JSONObject parse = (JSONObject)JSONObject.parse(wenc);
        parse.put("mobile", "18883394480");
        parse.put("position", "d当");
        System.out.println(parse.toString());

        String wenc1 = httpsRequestRetry(String.format(updateUrl, token),
                POST_METHOD,
                parse.toString(),
                0,
                false,
                true,
                "wenc");
        System.out.println(wenc1);
    }


    static{
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从SSLContext对象中得到SSLSocketFactory对象
            ssf = sslContext.getSocketFactory();
        } catch (Exception e) {
            log.error("init SSLSocketFactory error", e);
        }
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
