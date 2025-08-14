package com.tester.testersearch.service.binc.okx;

import com.alibaba.fastjson.JSON;
import com.tester.testercommon.util.MyConsumer;
import com.tester.testersearch.service.binc.okx.trade.TickerTradeResponse;
import com.tester.testersearch.service.binc.okx.trade.TradeRequest;
import com.tester.testersearch.service.binc.okx.trade.order.OrderSubmitResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.math.BigDecimal;
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
    private static final String API_KEY = "1b3ad5c6-6dbb-4bd6-b783-a12f0a449f70";
    private static final String SECRET_KEY = "BCF9588A144269F911FA3218E3521B9D"; // 秘钥
    private static final String PASSPHRASE = "Q!w2e3r4";
    // 查询
//    private static final String API_KEY = "64fdbec7-b3c6-4255-8ee7-2c89a8d1dba3";
//    private static final String SECRET_KEY = "D246AC7C2DDA7903181E4092E7013B65"; // 秘钥
//    private static final String PASSPHRASE = "Q!w2e3r4";
    private static final String BASE_URL = "https://www.okx.com";
    public static final String B_KEY = "BTC-USDT";
    public static int count = 15;

    public static BigDecimal cha = new BigDecimal(5);

    private static OkHttpClient client;
    static {
        client = getUnsafeOkHttpClient();
    }

    public static void main(String[] args) throws Exception {
        String instId = "BTC-USDT-SWAP";
        String instType = "SWAP";
        OkxTradeHelper okxTradeHelper = new OkxTradeHelper();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("交易");
//        okxTradeHelper.trade(instId,"sell");
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

    /**
     * 获取产品成交价
     * @param instId BTC-USDT-SWAP
     * @return
     * @throws Exception
     */
    public static void getOrderStatus(String instId, String orderId) throws Exception {
        String url = "/api/v5/trade/order?instId=" + instId+"&ordId"+orderId;
        String s = commonGet(url, (e) -> {});
        System.out.println("s = " + s);
    }
    public static TickerTradeResponse getTicker(String instId) throws Exception {
        String url = "/api/v5/market/ticker?instId=" + instId;
        String s = commonGet(url, (e) -> {});
        TickerTradeResponse tickerTradeResponse = JSON.parseObject(s, TickerTradeResponse.class);
        return tickerTradeResponse;
    }

    // 获取标记价格的方法
    public void trade(String instId,String side) throws Exception {
        BigDecimal tradePrice = null;
        TickerTradeResponse ticker = getTicker(instId);
        String posSide = null;
        if(Objects.equals(ticker.getCode(),"0") && !CollectionUtils.isEmpty(ticker.getData())){
            String last = ticker.getData().get(0).getLast();
            BigDecimal lastPrice = new BigDecimal(last);
            if("buy".equals(side)){
                tradePrice = lastPrice.subtract(cha);
                posSide = "long";
            }else if("sell".equals(side)){
                tradePrice = lastPrice.add(cha);
                posSide = "short";
            }
        }
        if(null == tradePrice){
            log.error("异常，交易价格为空");
            return;
        }
        String url = "/api/v5/trade/order";
        TradeRequest request = new TradeRequest();
        request.setInstId(instId);
        request.setTdMode("cross");
        request.setSide(side);
//        request.setPosSide(posSide);
        request.setOrdType("post_only");
        request.setSz("0.01");
        request.setPx(tradePrice.toPlainString());
        String s = commonPost(url, JSON.toJSONString(request), (e) -> {});
        OrderSubmitResponse orderSubmitResponse = JSON.parseObject(s, OrderSubmitResponse.class);
        if(orderSubmitResponse !=null && orderSubmitResponse.checkIfSuccess() && !CollectionUtils.isEmpty(orderSubmitResponse.getData())){
            OrderSubmitResponse.OrderData orderData = orderSubmitResponse.getData().get(0);
            String ordId = orderData.getOrdId();
            getOrderStatus(instId,ordId);
        }
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
        String respBody = commonPost(urlP, body, (e) -> {});
        return new JSONObject(respBody);
    }
    private static String commonPost(String urlP, String body, MyConsumer<String> consumer) throws Exception {
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
        System.out.println("====【Request】====");
        System.out.println("POST " + url);
        System.out.println("Body: " + body);
        System.out.println("Headers:");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("调用");
        Response response = client.newCall(request).execute();
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        String respBody = response.body() != null ? response.body().string() : "";
        System.out.println("====【Response】====");
        System.out.println("Status: " + response.code());
        System.out.println("Body: " + respBody);
        consumer.accept(respBody);
        return respBody;
    }

    private static JSONObject commonGet(String urlP) throws Exception {
        return new JSONObject(commonGet(urlP, (e) -> {}));
    }

    private static String commonGet(String urlP, MyConsumer<String> consumer) throws Exception {
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
        System.out.println("====【Request】====");
        System.out.println("POST " + url);
        System.out.println("Headers:");
        Response response = client.newCall(request).execute();
        String respBody = response.body() != null ? response.body().string() : "";
        System.out.println("====【Response】====");
        System.out.println("Status: " + response.code());
        System.out.println("Body: " + respBody);
        consumer.accept(respBody);
        return respBody;
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
