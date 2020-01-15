package com.tester.testerfuncprogram;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Security;
import java.util.Arrays;

public class EncryptTest {


    private byte[] getAesKey(){
        return Base64.decodeBase64("75eMTOfJJ2tXs3ogQZ1VMCj0InSj0yh50fP-TruHRHk");
    }
    private byte[] getOffset(){
        // 偏移量为16位
//        return "00000000000000000000000000000000".getBytes(Charset.forName("GBK"));
        return Base64.decodeBase64("00000000000000000000000000000000");
    }
    private byte[] getMessage(){
        return "i hold the key".getBytes(Charset.forName("GBK"));
    }
    private byte[] getMessage(String str){
        return Base64.decodeBase64(str);
    }
    private Base64 getBase64(){
        return new Base64();
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    @Test
    public void test_sap_encrypt() throws Exception {
        byte[] message = getMessage();
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
        String base64Encrypted = getBase64().encodeToString(encrypted);
        System.out.println(base64Encrypted);
    }
    @Test
    public void test_sap_decrypt() throws Exception {
        byte[] message = getMessage("IZ8qEwXJgnhfpIoNgVeHsg==");
        // PKCS5Padding
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(getAesKey(), "AES");
        IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(getOffset(),0,16));
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

        // 加密
        byte[] encrypted = cipher.doFinal(message);
        System.out.println(new String(encrypted));
    }
}
