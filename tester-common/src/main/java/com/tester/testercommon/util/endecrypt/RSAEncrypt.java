package com.tester.testercommon.util.endecrypt;

import com.alibaba.fastjson.JSON;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 温昌营
 * @Date
 */
public class RSAEncrypt {

    private static final String ALG_RSA = "RSA";
    private static final String ALG_DSA = "DSA";
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static final String ALGORITHM = "SHA256withRsa";
    private static final String CIPHER_INSTANCE = "RSA/ECB/PKCS1Padding";


    public static void main(String[] args) {
        // 密钥对（本示例仅用一个密钥对）
        Map<String, String> map = genKeyPair();
        String publicKey = map.get("publicKey");
        String privateKey = map.get("privateKey");
        System.out.println("公钥：");
        System.out.println(publicKey);
        System.out.println("私钥：");
        System.out.println(privateKey);
        // 模拟敏感数据，用接收方的公钥加密
        String certId = "35011111111111111X";
        String encCertId = encrypt(certId, publicKey);

        // 请求报文
        Map<String, Object> reqDataMap = new HashMap<String, Object>();
        reqDataMap.put("txn_id", "100");
        reqDataMap.put("data1", "123");
        reqDataMap.put("data2", "abc");
        reqDataMap.put("data3", "中文");
        reqDataMap.put("data4", encCertId);
        String reqJson = JSON.toJSONString(reqDataMap);
        System.out.println("请求报文：" + reqJson);

        // 用请求方的私钥签名
        String signResult = sign(reqJson, privateKey);
        // 测试结果
        System.out.println("签名结果：" + signResult);
        System.out.println("验签结果：" + verify(reqJson, signResult, publicKey));

        // 用接收方的私钥解密
        System.out.println("certId解密结果：" + decrypt(encCertId, privateKey));
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     */
    public static String encrypt(String data, String publicKey) {
        byte[] dataByte = data.getBytes(UTF_8);
        byte[] result = encrypt(dataByte, publicKey);
        return byteArr2HexString(result);
    }


    public static byte[] encrypt(byte[] dataByte, String publicKey) {
        byte[] keyByte = hexString2ByteArr(publicKey);
        byte[] result = null;
        try {
            KeyFactory keyFac = KeyFactory.getInstance(ALG_RSA);
            PublicKey pubKey = keyFac.generatePublic(new X509EncodedKeySpec(keyByte));
            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            result = cipher(dataByte, cipher, getBlockSize(pubKey) - 11);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 私钥解密
     *
     * @param encryptedData
     * @param privateKey
     * @return
     */
    public static String decrypt(String encryptedData, String privateKey) {
        byte[] dataByte = hexString2ByteArr(encryptedData);
        byte[] result = decrypt(dataByte, privateKey);
        return new String(result, UTF_8);
    }
    public static byte[] decrypt(byte[] encryptedData, String privateKey){
        byte[] result = null;
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(hexString2ByteArr(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance(ALG_RSA);
            PrivateKey priKey = keyFactory.generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            result = cipher(encryptedData, cipher, getBlockSize(priKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



    /**
     * 私钥签名
     *
     * @param data
     * @param privateKey
     * @return
     */
    public static String sign(String data, String privateKey) {
        String sign = null;
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(hexString2ByteArr(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance(ALG_RSA);
            PrivateKey priKey = keyFactory.generatePrivate(keySpec);
            Signature si = Signature.getInstance(ALGORITHM);
            si.initSign(priKey);
            si.update(data.getBytes(UTF_8));
            byte[] dataSign = si.sign();
            sign = byteArr2HexString(dataSign);
        } catch (Exception e) {
            e.printStackTrace();
            sign = null;
        }
        return sign;
    }

    /**
     * 公钥验签
     *
     * @param data
     * @param sign
     * @param publicKey
     * @return
     */
    public static boolean verify(String data, String sign, String publicKey) {
        boolean succ = false;
        try {
            Signature verf = Signature.getInstance(ALGORITHM);
            KeyFactory keyFac = KeyFactory.getInstance(ALG_RSA);
            PublicKey puk = keyFac.generatePublic(new X509EncodedKeySpec(hexString2ByteArr(publicKey)));
            verf.initVerify(puk);
            verf.update(data.getBytes(UTF_8));
            succ = verf.verify(hexString2ByteArr(sign));
        } catch (Exception e) {
            e.printStackTrace();
            succ = false;
        }
        return succ;
    }

    private static byte[] cipher(byte[] data, Cipher cipher, int blockSize) throws Exception {
        final ByteArrayInputStream in = new ByteArrayInputStream(data);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] cache = new byte[blockSize];
        while (true) {
            final int r = in.read(cache);
            if (r < 0) {
                break;
            }
            final byte[] temp = cipher.doFinal(cache, 0, r);
            out.write(temp, 0, temp.length);
        }
        return out.toByteArray();
    }
    private static int getBlockSize(final Key key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String alg = key.getAlgorithm();
        final KeyFactory keyFactory = KeyFactory.getInstance(alg);
        if (key instanceof PublicKey) {
            final BigInteger prime;
            if (ALG_RSA.equals(alg)) {
                prime = keyFactory.getKeySpec(key, RSAPublicKeySpec.class).getModulus();
            } else if (ALG_DSA.equals(alg)) {
                prime = keyFactory.getKeySpec(key, DSAPublicKeySpec.class).getP();
            } else {
                throw new NoSuchAlgorithmException("不支持的解密算法：" + alg);
            }
            return prime.toString(2).length() / 8;
        } else if (key instanceof PrivateKey) {
            final BigInteger prime;
            if (ALG_RSA.equals(alg)) {
                prime = keyFactory.getKeySpec(key, RSAPrivateKeySpec.class).getModulus();
            } else if (ALG_DSA.equals(alg)) {
                prime = keyFactory.getKeySpec(key, DSAPrivateKeySpec.class).getP();
            } else {
                throw new NoSuchAlgorithmException("不支持的解密算法：" + alg);
            }
            return prime.toString(2).length() / 8;
        } else {
            throw new RuntimeException("不支持的密钥类型：" + key.getClass());
        }
    }
    /**
     * 生成密钥对
     */
    private static Map<String, String> genKeyPair() {
        Map<String, String> map = null;
        try {
            map = new HashMap<String, String>();
            // RSA算法要求有一个可信任的随机数源
            SecureRandom secureRandom = new SecureRandom();
            //** 为RSA算法创建一个KeyPairGenerator对象
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALG_RSA);
            // 利用上面的随机数据源初始化这个KeyPairGenerator对象
            keyPairGenerator.initialize(1024, secureRandom);
            // 生成密匙对
            KeyPair kp = keyPairGenerator.generateKeyPair();

            String pubKeyStr = byteArr2HexString(kp.getPublic().getEncoded());
            String priKeyStr = byteArr2HexString(kp.getPrivate().getEncoded());

            map.put("publicKey", pubKeyStr);
            map.put("privateKey", priKeyStr);
        } catch (Exception e) {
            e.printStackTrace();
            map = null;
        }
        return map;
    }

    /**
     * byte数组转16进制字符串
     * @param bytearr
     * @return java.lang.String
     * @Date 15:26 2020/11/23
     * @Author 温昌营
     **/
    private static String byteArr2HexString(byte[] bytearr) {
        if (bytearr == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();

        for (byte b : bytearr) {
            if ((b & 0xFF) < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(b & 0xFF, 16));
        }
        return sb.toString();
    }

    /**
     * 16进制字符串转byte数组
     * @param hexString
     * @return byte[]
     * @Date 15:26 2020/11/23
     * @Author 温昌营
     **/
    private static byte[] hexString2ByteArr(String hexString) {
        if ((hexString == null) || (hexString.length() % 2 != 0)) {
            return new byte[0];
        }

        byte[] dest = new byte[hexString.length() / 2];

        for (int i = 0; i < dest.length; i++) {
            String val = hexString.substring(2 * i, 2 * i + 2);
            dest[i] = ((byte) Integer.parseInt(val, 16));
        }
        return dest;
    }

}
