package com.tester.testercommon.util.endecrypt.http;

public class Constants {

    public static final String BASE_HTTP_API_URL = "http://127.0.0.1:8008";

    /**
     * 系统给客户端生成的id，用于
     * 1. 快速检索对应的 SecretKey（用于验证HMAC签名）
     * 2. 统计API调用频次、权限控制等
     * 3. 实现请求追踪（如日志中可通过AccessKey定位问题）
     */
    public static String accessKey = "tester-common";

    /**
     * 调用下面方法生成密钥对
     * @see HmacKeyGen#main(String[])
     */
    public static String secretKey = "iH2gFnDywy1C2XSWOtKD9pSk2Zu5Qk0f3aOFfX9zlaM=";

    /**
     * 调用下面方法生成非堆成密钥对
     * @see EccCryptoUtil#generateEccKeyPair()
     */
//    public static String serverPublicKey = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEG/6q87sHVOgJKwgdIT" +
//            "WY55XsZOhtAFMz8TERFCbbFzov9ZYCvIAD/+zsCUq5X9TvkT3pj++qV22/VJPQBCI1fA==";
//    public static String clientPrivateKey = "MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQggjf2l" +
//            "PMT+8H1e+qYAye9XW98rv/aJwT8bFCr9A8HMn+gCgYIKoZIzj0DAQehRANCAAQb/qrzuwdU6AkrCB0hNZjnle" +
//            "xk6G0AUzPxMREUJtsXOi/1lgK8gAP/7OwJSrlf1O+RPemP76pXbb9Uk9AEIjV8";

    public static String serverPublicKey = null;
    public static String clientPrivateKey = null;
}