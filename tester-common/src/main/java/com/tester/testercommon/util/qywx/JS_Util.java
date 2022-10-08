package com.tester.testercommon.util.qywx;

import java.security.MessageDigest;
import java.util.Random;

public class JS_Util {

    private static Random random = new Random();

    /**
     * SHA1加密
     * @return
     */
    public static String SHA1(String decript){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(decript.getBytes());
            byte[] digest = md.digest();

            StringBuffer hexstr = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                String shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取随机字符串
     * @param length
     * @return java.lang.String
     * @throws
     * @author 温昌营
     * @date 2019/12/9
     */
    public static String getRandomStr(int length){
            String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < length; i++) {
                int number = random.nextInt(base.length());
                sb.append(base.charAt(number));
            }
            return sb.toString();
        }
}
