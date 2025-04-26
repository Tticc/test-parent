package com.tester.testersearch.service.binc.okx;

import com.alibaba.fastjson.JSON;
import com.tester.testersearch.service.binc.okx.trade.TradeRequest;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.util.StopWatch;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OkxTradeHelper {

    // 交易+查询
    private static final String API_KEY = "8f6a9728-0191-4778-b056-63efb3e4d7b7";
    private static final String SECRET_KEY = "E7657D77125CBE37B9944B72DB36C2F0"; // 秘钥
    private static final String PASSPHRASE = "Q!w2e3r4";
    // 查询
//    private static final String API_KEY = "64fdbec7-b3c6-4255-8ee7-2c89a8d1dba3";
//    private static final String SECRET_KEY = "D246AC7C2DDA7903181E4092E7013B65"; // 秘钥
//    private static final String PASSPHRASE = "Q!w2e3r4";
    private static final String BASE_URL = "https://www.okx.com";
    public static final String B_KEY = "BTC-USDT";
    public static int count = 15;

    private static OkHttpClient client;
    static {
        client = getUnsafeOkHttpClient();
    }

    public static void main(String[] args) throws Exception {
        OkxTradeHelper okxTradeHelper = new OkxTradeHelper();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("交易");
        JSONObject swap = okxTradeHelper.trade("SWAP", "BTC-USDT-SWAP");
        stopWatch.stop();
        System.out.println("swap = " + swap);
        System.out.println(stopWatch.prettyPrint());

//        JSONObject post_only = okxTradeHelper.orderHis("SWAP", "BTC-USDT-SWAP");
//        JSONArray data = post_only.getJSONArray("data");
//        for (Object datum : data) {
//            JSONObject json = (JSONObject)datum;
//            Object fillTime = json.get("fillTime");
//            String s = String.valueOf(fillTime);
//            Date date1 = new Date(Long.valueOf(s));
//            json.put("fillTime", DateUtil.dateFormat(date1));
//        }
//        System.out.println("post_only = " + data);
//        for (int i = 0; i < 200; i++) {
////            JSONObject swap = okxTradeHelper.getTicker("BTC-USDT-SWAP");
//            JSONObject swap = okxTradeHelper.getTickers("SWAP","BTC-USDT");
//            JSONArray data = swap.getJSONArray("data");
//            for (Object datum : data) {
//                JSONObject json = (JSONObject)datum;
//                Object fillTime = json.get("ts");
//                String s = String.valueOf(fillTime);
//                Date date1 = new Date(Long.valueOf(s));
//                json.put("ts", DateUtil.dateFormat(date1));
//            }
////            Object ts = swap.get("ts");
////            String s = String.valueOf(ts);
////            Date date = new Date(Long.valueOf(s));
////            swap.put("ts",DateUtil.dateFormat(date));
//            System.out.println("swap = " + swap);
//            TimeUnit.MILLISECONDS.sleep(500);
//        }
    }


    /**
     * 获取产品成交价
     * @param instType SWAP
     * @param uly BTC-USDT
     * @return
     * @throws Exception
     */
    public JSONObject getTickers(String instType, String uly) throws Exception {
        String url = "/api/v5/market/tickers?instType=" + instType + "&uly=" + uly;
        return commonGet(url);
    }

    /**
     * 获取产品成交价
     * @param instId BTC-USDT-SWAP
     * @return
     * @throws Exception
     */
    public JSONObject getTicker(String instId) throws Exception {
        String url = "/api/v5/market/ticker?instId=" + instId;
        return commonGet(url);
    }

    // 获取标记价格的方法
    public JSONObject trade(String instType, String instId) throws Exception {
        String url = "/api/v5/trade/order";
        TradeRequest request = new TradeRequest();
        request.setInstId(instId);
        request.setTdMode("cross");
        request.setSide("buy");
        request.setPosSide("long");
        request.setOrdType("post_only");
        request.setSz("0.02");
        request.setPx("88000");
        return commonPost(url, JSON.toJSONString(request));
    }

    /**
     * 获取未完成订单
     * @param instType
     * @param instId
     * @return
     * @throws Exception
     */
    public JSONObject listPendingOrder(String instType, String instId) throws Exception {
        String url = "/api/v5/trade/orders-pending?instType=" + instType + "&instId=" + instId;
        return commonGet(url);
    }

    /**
     * 获取订单历史
     * @param instType
     * @param instId
     * @return
     * @throws Exception
     */
    public JSONObject orderHis(String instType, String instId) throws Exception {
        String url = "/api/v5/trade/orders-history?instType=" + instType + "&instId=" + instId;
        return commonGet(url);
    }

    private static JSONObject commonPost(String urlP, String body) throws Exception {
        String url = BASE_URL + urlP;String timestamp = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now());
        String sign = signMessage(timestamp, "POST", urlP, body);
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json"), body))
                .addHeader("OK-ACCESS-KEY", API_KEY)
                .addHeader("OK-ACCESS-SIGN", sign)
                .addHeader("OK-ACCESS-TIMESTAMP", timestamp)
                .addHeader("OK-ACCESS-PASSPHRASE", PASSPHRASE)
                .addHeader("Content-Type", "application/json")
                .build();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("调用");
        Response response = client.newCall(request).execute();
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        String string = response.body().string();
        return new JSONObject(string);
    }

    private static JSONObject commonGet(String urlP) throws Exception {
        String url = BASE_URL + urlP;
        String timestamp = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now());
        String sign = signMessage(timestamp, "GET", urlP, "");
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("OK-ACCESS-KEY", API_KEY)
                .addHeader("OK-ACCESS-SIGN", sign)
                .addHeader("OK-ACCESS-TIMESTAMP", timestamp)
                .addHeader("OK-ACCESS-PASSPHRASE", PASSPHRASE)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        String string = response.body().string();
        return new JSONObject(string);
    }

    private static String signMessage(String timestamp, String method, String requestPath, String body) throws Exception {
        String preHash = timestamp + method + requestPath + body;
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(preHash.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // 创建一个信任所有证书的 TrustManager
            final TrustManager[] trustAllCertificates = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            // 安装 TrustManager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

            // 创建 SSL 套接字工厂
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
            // 创建 OkHttpClient
            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCertificates[0])
                    .hostnameVerifier((hostname, session) -> true) // 信任所有主机
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .proxy(proxy)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
