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
//    private static String token = "AZ2z2sNoWyYVeu9WGYIRT1Ut2eT1WGi0dQLGX-P6BUg017GGZCnw7f6nf83HxSYxLIIuNS3qQ5NpZ9RP79pqsL78tRgAT19uzIQtASzuoUW2cVGiWTBSxHYnlERQ2PhdUj-hjXmlZDYMMZQyjiIGtg5Lma4CAx4MZIy6G5QRxxiL1YooS_D6WPpFa-bcjlbtcXL_Lkkp9wKjUjBulQzz6w";
//    private static String userId = "liufei";

    private static String getToken = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
    private static String corpId = "ww4749ac23812cffae";
    private static String contactSecret = "5JMxsYqj1VnHTT5suo4xpQG1RA19aTix_hwAp_SGJUw";
    private static String testSecret = "75eMTOfJJ2tXs3ogQZ1VMCj0InSj0yh50fP-TruHRHk";

    public static SSLSocketFactory ssf;
    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";

    public static void main(String[] args){
        String token = "";
//        String userId = "liufei";
        String userId = "18883394480";
        token = getAccessToken(testSecret);
        System.out.println(token);
//        token = getAccessToken(contactSecret);

        System.out.println("\n\n");

        JSONObject user = getUser(userId, token);
//        user.put("mobile", "18883394480");
//        user.put("position", "d当");
        System.out.println(user.toString());
        System.out.println("\n\n");

        JSONObject jsonObject = updateUser(user, token);
        System.out.println(jsonObject);
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

    public static String getAccessToken(String secret){
        String newToken = httpsRequestRetry(String.format(getToken, corpId, secret),
                GET_METHOD,
                null,
                0,
                false,
                true,
                "getToken");
        System.out.println(newToken);
        JSONObject parse1 = (JSONObject)JSONObject.parse(newToken);
        return String.valueOf(parse1.get("access_token"));
    }
    public static JSONObject getUser(String userId, String token){
        String wenc = httpsRequestRetry(String.format(getUrl, token, userId),
                GET_METHOD,
                null,
                0,
                false,
                true,
                "getUser");
        return (JSONObject)JSONObject.parse(wenc);
    }
    public static JSONObject updateUser(JSONObject user, String token){
        String wenc1 = httpsRequestRetry(String.format(updateUrl, token),
                POST_METHOD,
                user.toString(),
                0,
                false,
                true,
                "updateUser");
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