package com.tester.testersearch.util.binc.binance;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.http.HttpsClient;
import com.tester.testersearch.dao.domain.TradeDataBaseDomain;
import org.json.JSONArray;

import java.math.BigDecimal;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BinCommon {

    private static String BASE_URL = "https://api.binance.com";
    private static String API = "/api/v3/klines";

    //    String PARAM = "?symbol=BTCUSDT&interval=1s&limit=5&startTime=1742037060000&endTime=1742037120000";
    private static String PARAM = "?symbol={}&interval={}&limit={}&startTime={}&endTime={}";



    private static Proxy proxy = HttpsClient.getProxy("127.0.0.1", 7890);

    public static void main(String[] args) {
        fetchData("BTCUSDT", "1s", "100", "1742037060000", "1742037071000");
    }

    public static List<TradeDataBaseDomain> fetchData(String bKey, String bar, String limit, String startAt, String endAt) {
        String param = format(PARAM, bKey, bar, limit, startAt, endAt);
        String url = BASE_URL + API + param;
        Map<String, String> propertyMap = new HashMap<>();
        propertyMap.put("Content-Type", "application/json;charset=UTF-8");
        List<TradeDataBaseDomain> res = new ArrayList<>();
        try {
            String s = HttpsClient.requestForString(url, HttpsClient.GET_METHOD, null, propertyMap, proxy);
            JSONArray dataArray = new JSONArray(s);
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
                        .setBKey(bKey)
                        .setRealData(1)
                        .setTimestamp(timestamp)
                        .setBar(bar)
                        .setId(timestamp);
                res.add(dataInfo);
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return res;
    }


    public static String format(String messagePattern, Object... arguments) {
        if (messagePattern == null || arguments == null) {
            return messagePattern;
        }
        for (Object argument : arguments) {
            messagePattern = messagePattern.replaceFirst("\\{\\}", String.valueOf(argument));
        }
        return messagePattern;
    }
}
