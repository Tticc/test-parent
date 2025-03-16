package com.tester.thirdparty.okx;

import com.alibaba.fastjson.JSON;
import com.tester.testercommon.util.DateUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OKXPositionQuery {

    private static final String API_KEY = "64fdbec7-b3c6-4255-8ee7-2c89a8d1dba3";
    private static final String SECRET_KEY = "D246AC7C2DDA7903181E4092E7013B65"; // 秘钥
    private static final String PASSPHRASE = "Q!w2e3r4";
    private static final String BASE_URL = "https://www.okx.com";

    public static Map<Long, String> dataSet = new LinkedHashMap<>();


    private static final String CCY = "BTC";

    private static final String PARAM = "?instFamily="+CCY+"-USDT&instType=SWAP&instId="+CCY+"-USDT-SWAP&ccy="+CCY+"&period=1D";

    public static void main(String[] args) {
        getKline();
//        getPosi1();
//        getPosi2();
//        getPosi3();
//        getPosi4();
//        getPosi5();
//        getPosi6();
//        getPosi7();
//        getPosi8();
    }



    public static String getKlineData() {
        return getKlineDataWithLimit("300");
    }
    public static String getKlineDataWithLimit(String limit) {
        try {
            Long before = getTime("20240310000000");
            Long after = getTime("20250314000000");
            String instId = "BTC-USDT";
            String bar = "5m";
//            String urlP1 = "/api/v5/market/candles?instId={}&bar={}&limit={}&before={}&after={}";
            String urlP1 = "/api/v5/market/candles?instId={}&bar={}&limit={}";
//            String urlP = format(urlP1, instId,bar,limit,before,after);
            String urlP = format(urlP1, instId,bar,limit);
            String result = commonGet(urlP);
//            System.out.println("8 合约清算订单数据: " + result);
            return result;
        } catch (Exception e) {
            log.error("获取okx数据失败");
        }
        return null;
    }

    private static Long getTime(String dateStr){
        LocalDateTime localDateTime = DateUtil.getLocalDateTime(dateStr);
        ZoneId zoneId = ZoneId.systemDefault();
        Date from = Date.from(localDateTime.atZone(zoneId).toInstant());
        return from.getTime();
    }

    /**
     * K线
     */
    public static void getKline() {
        try {

            String instId = "BTC-USDT";
            String bar = "1m";
            String limit = "10";
            String urlP1 = "/api/v5/market/candles?instId={}&bar={}&limit={}";
            String urlP = format(urlP1, instId,bar,limit);
            String result = commonGet(urlP);
            System.out.println("8 合约清算订单数据: " + result);
            TopTradeInfo topTradeInfo = JSON.parseObject(result, TopTradeInfo.class);
            List<List<String>> data = topTradeInfo.getData();
            List<String> first = data.get(0);
            Date tt = new Date(Long.valueOf(first.get(0)));
//            System.out.println("all持仓比例信息: time:" + DateUtil.dateFormat(tt) +", ratio:"+first.get(1));
            for (List<String> datum : data) {
                String s = DateUtil.dateFormat(new Date(Long.valueOf(datum.get(0))));
                String openPrice = datum.get(1);
                String hPrice = datum.get(2);
                String lPrice = datum.get(3);
                String closePrice = datum.get(4);
                System.out.println(format("时间:{},开盘价:{},最高价:{},最低价:{},收盘价:{}",s, openPrice, hPrice, lPrice, closePrice));
            }
        } catch (Exception e) {
        }
    }
    public static String format(String messagePattern, Object... arguments) {
        if (messagePattern == null || arguments == null) {
            return messagePattern;
        }

        StringBuilder sb = new StringBuilder(messagePattern.length() + 50);  // 预分配空间
        int i = 0, j = 0;

        int index = 0;
        while (index < arguments.length) {
            // 查找第一个占位符 "{}"
            j = messagePattern.indexOf("{}", j);
            if (j == -1) {
                break;  // 如果没有更多占位符，退出
            }

            // 将占位符前的部分添加到sb中
            sb.append(messagePattern, i, j);

            // 将相应的参数添加到sb中
            sb.append(arguments[index]);

            // 更新 i 和 j
            index++;
            j += 2;  // 移动 j，跳过 "{}"
            i = j;
        }

        // 如果还有剩余的部分（没有占位符的部分），添加到sb中
        if (j < messagePattern.length()) {
            sb.append(messagePattern, j, messagePattern.length());
        }

        return sb.toString();
    }




    /**
     * 合约清算订单数据（Liquidation Orders）
     */
    public static void getPosi8() {
        try {
            String urlP = "/api/v5/public/liquidation-orders"+PARAM+"&state=filled";
            String result = commonGet(urlP);
            System.out.println("8 合约清算订单数据: " + result);
            TopTradeInfo topTradeInfo = JSON.parseObject(result, TopTradeInfo.class);
            List<List<String>> data = topTradeInfo.getData();
            List<String> first = data.get(0);
            Date tt = new Date(Long.valueOf(first.get(0)));
            System.out.println("all持仓比例信息: time:" + DateUtil.dateFormat(tt) +", ratio:"+first.get(1));
        } catch (Exception e) {
        }
    }
    /**
     * 永续合约资金费率（Funding Rate）
     */
    public static void getPosi7() {
        try {
            String urlP = "/api/v5/public/funding-rate"+PARAM;
            String result = commonGet(urlP);
            System.out.println("7 永续合约资金费率: " + result);
            TopTradeInfo topTradeInfo = JSON.parseObject(result, TopTradeInfo.class);
            List<List<String>> data = topTradeInfo.getData();
            List<String> first = data.get(0);
            Date tt = new Date(Long.valueOf(first.get(0)));
            System.out.println("永续合约资金费率: time:" + DateUtil.dateFormat(tt) +", ratio:"+first.get(1));
        } catch (Exception e) {
        }
    }
    /**
     * 大额主动买卖比（Taker Long/Short Ratio）
     */
    public static void getPosi6() {
        try {
            String urlP = "/api/v5/rubik/stat/taker-volume-contract"+PARAM;
            String result = commonGet(urlP);
            System.out.println("6 大额主动买卖比: " + result);
            TopTradeInfo topTradeInfo = JSON.parseObject(result, TopTradeInfo.class);
            List<List<String>> data = topTradeInfo.getData();
            List<String> first = data.get(0);
            Date tt = new Date(Long.valueOf(first.get(0)));
        } catch (Exception e) {
        }
    }
    /**
     * 合约账户杠杆使用情况（Margin Lending Ratio）
     */
    public static void getPosi5() {
        try {
            String urlP = "/api/v5/rubik/stat/margin/loan-ratio"+PARAM;
            String result = commonGet(urlP);
            System.out.println("5 合约账户杠杆使用情况: " + result);
            TopTradeInfo topTradeInfo = JSON.parseObject(result, TopTradeInfo.class);
            List<List<String>> data = topTradeInfo.getData();
            List<String> first = data.get(0);
            Date tt = new Date(Long.valueOf(first.get(0)));
            System.out.println("5 合约账户杠杆使用情况: time:" + DateUtil.dateFormat(tt) +", ratio:"+first.get(1));
        } catch (Exception e) {
        }
    }
    /**
     * 顶级账号数量多空比
     */
    public static void getPosi1() {
        try {
            String result = getPositions1();
            System.out.println("res: " + result);
            TopTradeInfo topTradeInfo = JSON.parseObject(result, TopTradeInfo.class);
            List<List<String>> data = topTradeInfo.getData();
            List<String> first = data.get(0);
            Date tt = new Date(Long.valueOf(first.get(0)));
            System.out.println("帐号比例信息: time:" + DateUtil.dateFormat(tt) +", ratio:"+first.get(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 顶级账号持仓多空比
     */
    public static void getPosi2() {
        try {
            String result = getPositions2();
            System.out.println("res: " + result);
            TopTradeInfo topTradeInfo = JSON.parseObject(result, TopTradeInfo.class);
            List<List<String>> data = topTradeInfo.getData();
            List<String> first = data.get(0);
            Date tt = new Date(Long.valueOf(first.get(0)));
            System.out.println("持仓比例信息: time:" + DateUtil.dateFormat(tt) +", ratio:"+first.get(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 所有交易者多空比（All Traders Long/Short Ratio）
     */
    public static void getPosi3() {
        try {
            String urlP = "/api/v5/rubik/stat/contracts/long-short-account-ratio"+PARAM;
            String result = commonGet(urlP);
            System.out.println("res: " + result);
            TopTradeInfo topTradeInfo = JSON.parseObject(result, TopTradeInfo.class);
            List<List<String>> data = topTradeInfo.getData();
            List<String> first = data.get(0);
            Date tt = new Date(Long.valueOf(first.get(0)));
            System.out.println("all持仓比例信息: time:" + DateUtil.dateFormat(tt) +", ratio:"+first.get(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 合约持仓量（Open Interest, OI）
     */
    public static void getPosi4() {
        try {
            String urlP = "/api/v5/public/open-interest"+PARAM;
            String result = commonGet(urlP);
            System.out.println("4 合约持仓量: " + result);
            TopTradeInfo topTradeInfo = JSON.parseObject(result, TopTradeInfo.class);
            List<List<String>> data = topTradeInfo.getData();
            List<String> first = data.get(0);
            Date tt = new Date(Long.valueOf(first.get(0)));
            System.out.println("all持仓比例信息: time:" + DateUtil.dateFormat(tt) +", ratio:"+first.get(1));
        } catch (Exception e) {
        }
    }


    public static String commonGet(String urlP) throws Exception {
        String url = BASE_URL + urlP;
        String timestamp = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now());
        String sign = signMessage(timestamp, "GET", urlP, "");
        OkHttpClient client = getUnsafeOkHttpClient();
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
        return response.body().string();
    }
    public static String getPositions1() throws Exception {
        String urlP = "/api/v5/rubik/stat/contracts/long-short-account-ratio-contract-top-trader"+PARAM;
        String url = BASE_URL + urlP;


        String timestamp = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now());
        String sign = signMessage(timestamp, "GET", urlP, "");
        OkHttpClient client = getUnsafeOkHttpClient();
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
        return response.body().string();
    }

    public static String getPositions2() throws Exception {
        String urlP = "/api/v5/rubik/stat/contracts/long-short-position-ratio-contract-top-trader"+PARAM;
        String url = BASE_URL + urlP;


        String timestamp = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now());
        String sign = signMessage(timestamp, "GET", urlP, "");
        OkHttpClient client = getUnsafeOkHttpClient();
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
        return response.body().string();
    }

    public static String getPositions() throws Exception {
        String url = BASE_URL + "/api/v5/account/positions";


        String timestamp = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now());
        String sign = signMessage(timestamp, "GET", "/api/v5/account/positions", "");


        System.out.println(timestamp);  // 示例输出: 2024-02-01T12:34:56.789Z

        OkHttpClient client = getUnsafeOkHttpClient();
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
        return response.body().string();
    }

    private static String signMessage(String timestamp, String method, String requestPath, String body) throws Exception {
        String preHash = timestamp + method + requestPath + body;
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(preHash.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // 创建一个信任所有证书的 TrustManager
            final TrustManager[] trustAllCertificates = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                        @Override
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
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

    @Data
    @Accessors(chain = true)
    public static class TopTradeInfo{
        private String code;
        private List<List<String>> data;
        private String msg;

    }
}
