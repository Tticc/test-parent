package com.tester.testercommon.util.endecrypt;

import org.springframework.util.DigestUtils;

/**
 * @Author 温昌营
 * @Date
 */
public class Md5Security {
    public static void main(String[] args) throws Exception {
        String pwd = "123456";
        String salt = "305a878cd5e668aebb66baa6";
        String encrypt = encrypt(pwd + salt);
        System.out.println("final pwd: " + encrypt);
    }

    private static String SECRET_KEY = "e95f8a3e7205ced3e12o";

    public static String encrypt(String message) throws Exception {
        message = message + SECRET_KEY;
        return DigestUtils.md5DigestAsHex(message.getBytes("UTF-8"));
    }

    public String decrypt(String ciphertext) throws Exception {
        throw new RuntimeException("Do not support decryption.");
    }

}
