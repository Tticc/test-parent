package com.tester.testercommon.util.endecrypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @Author 温昌营
 * @Date 2021-10-27 14:32:58
 */
public class SHA1Encrypt {

    public static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f' };

    // 40位
    public static String getSha1(String str) {
        return doSha(str,"SHA1");
    }

    // 64位
    public static String getSha256(String str) {
        return doSha(str,"SHA-256");
    }


    public static String doSha(String data, String type) {
        try {
            MessageDigest digest = MessageDigest.getInstance(type);
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            int j = hash.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = hash[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }
}
