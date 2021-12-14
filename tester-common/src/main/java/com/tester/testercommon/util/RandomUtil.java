package com.tester.testercommon.util;

import java.util.Random;

public class RandomUtil {
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwsyzABCDEFGHIJKLMNOPQRSTUVWSYZ0123456789!@#$%^&*_=";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
