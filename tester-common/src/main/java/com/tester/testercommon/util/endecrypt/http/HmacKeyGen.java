package com.tester.testercommon.util.endecrypt.http;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class HmacKeyGen {
    public static void main(String[] args) throws Exception {
        // 1. 获取 HmacSHA256 的 KeyGenerator
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        // 2. 指定密钥长度（可选，默认就是 256 bit）
        keyGen.init(256);
        // 3. 生成 SecretKey
        SecretKey secretKey = keyGen.generateKey();
        // 4. 编码成 Base64 便于存储/传输
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("HMAC-SHA256 Key (Base64): " + base64Key);
    }
}
