package com.tester.testercommon.util;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Security;
import java.util.Arrays;

public class SecurityTool {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    public static String encrypt(String msg) throws Exception {
        byte[] message = msg.getBytes(Charset.forName("utf-8"));
        System.out.println("message.length:" + message.length);

        // PKCS5Padding
        // PKCS7Padding
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(getAesKey(), "AES");
        IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(getOffset(),0,16));
        System.out.println("iv.getIV().length:"+iv.getIV().length);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

        // 加密
        byte[] encrypted = cipher.doFinal(message);
        String base64Encrypted = new Base64().encodeToString(encrypted);
        return base64Encrypted;
    }
    public static String decrypt(String msg) throws Exception {
        byte[] message = Base64.decodeBase64(msg);
        // PKCS5Padding
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(getAesKey(), "AES");
        IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(getOffset(),0,16));
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

        // 加密
        byte[] encrypted = cipher.doFinal(message);
        return new String(encrypted);
    }

    private static byte[] getAesKey(){
        return Base64.decodeBase64("75eMTOfJJ2tXs3ogQZ1VMCj0InSj0yh50fP-TruHRHk");
    }
    private static byte[] getOffset(){
        // 偏移量为16位
//        return "00000000000000000000000000000000".getBytes(Charset.forName("GBK"));
        return Base64.decodeBase64("00000000000000000000000000000000");
    }

}
