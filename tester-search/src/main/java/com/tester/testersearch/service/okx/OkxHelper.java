package com.tester.testersearch.service.okx;

import com.tester.testersearch.dao.domain.TradeDataBaseDomain;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OkxHelper {

    private static final String API_KEY = "64fdbec7-b3c6-4255-8ee7-2c89a8d1dba3";
    private static final String SECRET_KEY = "D246AC7C2DDA7903181E4092E7013B65"; // 秘钥
    private static final String PASSPHRASE = "Q!w2e3r4";
    private static final String BASE_URL = "https://www.okx.com";
    public static final String B_KEY = "BTC-USDT";
    public static int count = 15;

    public static List<TradeDataBaseDomain> getOKXKlineData(String limit, String bar) {
        return getOKXKlineData(limit, bar, null, null);
    }
    public static List<TradeDataBaseDomain> getOKXKlineData(String limit, String bar, Long after, Long before) {
        if(StringUtils.isEmpty(API_KEY)
                || StringUtils.isEmpty(SECRET_KEY)
                || StringUtils.isEmpty(PASSPHRASE)
                || StringUtils.isEmpty(BASE_URL)
                || StringUtils.isEmpty(B_KEY)){
            return Collections.emptyList();
        }
        List<TradeDataBaseDomain> res = new ArrayList<>();
        try {
            String klineData = getKlineDataWithLimit(limit,bar,after,before);
            JSONObject jsonResponse = new JSONObject(klineData);
            JSONArray dataArray = jsonResponse.getJSONArray("data");

            for (int i = dataArray.length() - 1; i >= 0; i--) {
                JSONArray candle = dataArray.getJSONArray(i);
                long timestamp = candle.getLong(0);
                BigDecimal openPrice = BigDecimal.valueOf(candle.getDouble(1));
                BigDecimal highPrice = BigDecimal.valueOf(candle.getDouble(2));
                BigDecimal lowPrice = BigDecimal.valueOf(candle.getDouble(3));
                BigDecimal closePrice = BigDecimal.valueOf(candle.getDouble(4));
                BigDecimal volume = BigDecimal.valueOf(candle.getDouble(5));

                TradeDataBaseDomain dataInfo = new TradeDataBaseDomain();
                dataInfo.setOpen(openPrice)
                        .setHigh(highPrice)
                        .setLow(lowPrice)
                        .setClose(closePrice)
                        .setVolume(volume)
                        .setBKey(B_KEY)
                        .setRealData(1)
                        .setTimestamp(timestamp)
                        .setBar(bar)
                        .setId(timestamp);
                res.add(dataInfo);
            }
        } catch (Exception e) {
            if(--count < 3) {
                log.error("数据解析失败");
                count = 15;
            }
        }
        return res;
    }

    /**
     *
     * @param limit
     * @param bar 时间粒度，默认值1m
     * 如 [1m/3m/5m/15m/30m/1H/2H/4H]
     * 香港时间开盘价k线：[6H/12H/1D/2D/3D/1W/1M/3M]
     * UTC时间开盘价k线：[/6Hutc/12Hutc/1Dutc/2Dutc/3Dutc/1Wutc/1Mutc/3Mutc]
     * @param before 请求此时间戳之后（更新的数据）的分页内容，传的值为对应接口的ts, 单独使用时，会返回最新的数据。
     * @param after 请求此时间戳之前（更旧的数据）的分页内容，传的值为对应接口的ts
     * @return
     */
    private static String getKlineDataWithLimit(String limit, String bar, Long after, Long before) {
        try {
            String instId = B_KEY;
//            String urlP1 = "/api/v5/market/candles?instId={}&bar={}&limit={}&before={}&after={}";
            String urlP1 = "/api/v5/market/candles?instId={}&bar={}&limit={}";
            if(null != after){
                urlP1 = urlP1+"&after={}";
            }
            if(null != before){
                urlP1 = urlP1+"&before={}";
            }
//            String urlP = format(urlP1, instId,bar,limit,before,after);
            String urlP = format(urlP1, instId, bar, limit, after, before);
            String result = commonGet(urlP);
//            System.out.println("8 合约清算订单数据: " + result);
            return result;
        } catch (Exception e) {
//            log.error("获取okx数据失败",e);
        }
        return null;
    }

    private static String format(String messagePattern, Object... arguments) {
        if (messagePattern == null || arguments == null) {
            return messagePattern;
        }
        for (Object argument : arguments) {
            messagePattern = messagePattern.replaceFirst("\\{\\}", String.valueOf(argument));
        }
        return messagePattern;
    }

    private static String commonGet(String urlP) throws Exception {
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
