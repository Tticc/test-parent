package com.tester.testercommon.util.endecrypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Arrays;
import java.util.Objects;

/**
 * @Author 温昌营
 * @Date
 */
public class AesSecurityHex {
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
    public static String encrypt(String msg,String key) throws Exception {
        return encrypt(msg, key, defaultOffSet());
    }
    /** aes 解密*/
    public static String decrypt(String msg) throws Exception {
        return decrypt(msg, defaultKey(), defaultOffSet());
    }
    public static String decrypt(String msg,String key) throws Exception {
        return decrypt(msg, key, defaultOffSet());
    }

    /** aes 加密*/
    public static String encrypt(String msg, String aseKey, String offSet) throws Exception {
        byte[] message = msg.getBytes(CODE);
        byte[] key = hexDecode(aseKey);
        byte[] oft = hexDecode(offSet);
        // PKCS5Padding
        // PKCS7Padding
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(oft,0,16));
        // 加密
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        byte[] encrypted = cipher.doFinal(message);
        return hexEncodeToString(encrypted);
    }
    /** aes 解密*/
    public static String decrypt(String msg, String aseKey, String offSet) throws Exception {
        byte[] message = hexDecode(msg);
        byte[] key = hexDecode(aseKey);
        byte[] oft = hexDecode(offSet);
        // PKCS5Padding
        // PKCS7Padding
        Security.addProvider(new BouncyCastleProvider());
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(oft,0,16));
        // 解密
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
        byte[] encrypted = cipher.doFinal(message);

        return new String(encrypted,CODE);
    }

    private static String defaultKey(){
        return "etXs3ogQZ1VMCj0InSfP-T4e4s85gea1";
    }
    private static String defaultOffSet(){
        return "00000000000000000000000000000000";
    }

    private static byte[] hexDecode(String key){
        return Hex.decode(key);
    }
    private static String hexEncodeToString(byte[] bytes) throws UnsupportedEncodingException {
        return new String(Hex.encode(bytes),CODE);
    }
}
