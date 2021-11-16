package com.tester.thirdparty.nacos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;

@Slf4j
public class ConnectNacosTest_Sign {


    public static final String UTF_8 = "UTF-8";


    public void checkSignature(Map<String, String> params) {
        String ak = getAccessKey();
        String sk = getSecretKey();
        if (StringUtils.isEmpty(ak) && StringUtils.isEmpty(sk)) {
            return;
        }

        try {
            String app = System.getProperty("project.name");
            String signData = getSignData(params.get("serviceName"));
            String signature = sign(signData, sk);
            params.put("signature", signature);
            params.put("data", signData);
            params.put("ak", ak);
            params.put("app", app);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAccessKey() {
        return null;
    }
    public String getSecretKey() {
        return null;
    }

    private static String getSignData(String serviceName) {
        return !StringUtils.isEmpty(serviceName)
                ? System.currentTimeMillis() + "@@" + serviceName
                : String.valueOf(System.currentTimeMillis());
    }

    public static String sign(String data, String key) throws Exception {
        try {
            byte[] signature = sign(data.getBytes(UTF_8), key.getBytes(UTF_8),
                    SigningAlgorithm.HmacSHA1);
            return new String(ConnectNacosTest_Base64.encodeBase64(signature));
        } catch (Exception var3) {
            throw new Exception(
                    "Unable to calculate a request signature: " + var3.getMessage(),
                    var3);
        }
    }

    private static byte[] sign(byte[] data, byte[] key,
                               SigningAlgorithm algorithm) throws Exception {
        try {
            Mac mac = Mac.getInstance(algorithm.toString());
            mac.init(new SecretKeySpec(key, algorithm.toString()));
            return mac.doFinal(data);
        } catch (Exception var4) {
            throw new Exception(
                    "Unable to calculate a request signature: " + var4.getMessage(),
                    var4);
        }
    }

    public enum SigningAlgorithm {
        // Hmac SHA1 algorithm
        HmacSHA1;

        SigningAlgorithm() {
        }
    }
}
