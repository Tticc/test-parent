package com.tester.testercommon.util.endecrypt.http;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

/**
 * 签名生成工具类
 */
@Slf4j
public class SignatureGeneratorUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String generateSignature(String sk, Object params) {
        Map<String, Object> paramMap;
        try {
            paramMap = objectMapper.convertValue(params, TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class));
        } catch (Exception e) {
            log.error("签名失败，参数转换异常", e);
            throw new RuntimeException("签名失败，暂不支持非对象或map类型参数");
        }

        // 1.去关键字
        paramMap.remove("Signature");
        // 2.拼接时间戳
        if (!paramMap.containsKey("timestamp")) {
            // 取当前小时的时间戳
            paramMap.put("timestamp", System.currentTimeMillis() / 1000 / 60 / 60);
        }

        // 2.根据参数Key排序拼接所有参数
        TreeMap<String, Object> sortParams = new TreeMap<>(paramMap);
        Iterator<String> it = sortParams.keySet().iterator();
        StringBuilder paramString = new StringBuilder();
        while (it.hasNext()) {
            String key = it.next();
            // 如果对应的value为null，则不拼接
            if (sortParams.get(key) != null) {
                paramString.append(key);
                // json化后排序,对于map或set等集合参数，http传输过程无法保证顺序
                String jsonString = JSON.toJSONString(sortParams.get(key));
                char[] chars = jsonString.toCharArray();
                Arrays.sort(chars);
                paramString.append(new String(chars));
            }
        }
        // 加密
        return sign(sk, paramString.toString());

    }

    public static String sign(String accessSecret, String stringToSign) {
        try {
            stringToSign = specialUrlEncode(stringToSign);
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA1"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(signData);
        } catch (Exception e) {
            log.error("签名失败", e);
            throw new RuntimeException("sign error", e);
        }
    }

    public static String specialUrlEncode(String value) throws Exception {
        return java.net.URLEncoder.encode(value, "UTF-8")
                .replace("+", "%20")
                .replace("*", "%2A")
                .replace("~", "%7E");
    }

}