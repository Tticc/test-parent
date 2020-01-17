package com.tester.testercommon.util.endecrypt;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.util.Arrays;
import java.util.Objects;

/**
 * @Author 温昌营
 * @Date
 */
public class AesSecurityBase64 {
    public static void main(String[] args) throws Exception {
        String msg = "i hold the key。@……￥*@#……@#分季度付金额偶发";
        String encrypted = encrypt(msg);
        System.out.println("密文：" + encrypted);
        String decrypted = decrypt(encrypted);
        System.out.println("原文：" + decrypted);
        System.out.println("解密成功："+ Objects.equals(decrypted, msg));

    }
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static String CODE = "UTF-8";

    /** aes 加密*/
    public static String encrypt(String msg) throws Exception {
        return encrypt(msg, defaultKey(), defaultOffSet());
    }
    /** aes 解密*/
    public static String decrypt(String msg) throws Exception {
        return decrypt(msg, defaultKey(), defaultOffSet());
    }

    /** aes 加密*/
    public static String encrypt(String msg, String aseKey, String offSet) throws Exception {
        byte[] message = msg.getBytes(CODE);
        byte[] key = base64Decode(aseKey);
        byte[] oft = base64Decode(offSet);
        // PKCS5Padding
        // PKCS7Padding
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(oft,0,16));
        // 加密
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        byte[] encrypted = cipher.doFinal(message);
        return base64EncodeToString(encrypted);
    }
    /** aes 解密*/
    public static String decrypt(String msg, String aseKey, String offSet) throws Exception {
        byte[] message = base64Decode(msg);
        byte[] key = base64Decode(aseKey);
        byte[] oft = base64Decode(offSet);
        // PKCS5Padding
        // PKCS7Padding
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(oft,0,16));
        // 解密
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
        byte[] encrypted = cipher.doFinal(message);

        return new String(encrypted,CODE);
    }

    private static String defaultKey(){
        return "etXs3ogQZ1VMCj0InSfP-T";
    }
    private static String defaultOffSet(){
        return "00000000000000000000000000000000";
    }

    private static byte[] base64Decode(String key){
        return Base64.decodeBase64(key);
    }
    private static String base64EncodeToString(byte[] bytes){
        return Base64.encodeBase64String(bytes);
    }
}
