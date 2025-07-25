package com.tester.testercommon.util.endecrypt.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

/**
 * ECC 签名验签
 */
@Slf4j
public class EccCryptoUtil {
    private static final KeyFactory keyFactory;
    private static final KeyPairGenerator kpg;
    private static final Cipher cipher;
    private static final MessageDigest digest;
    private static final Signature signature;

    public static void main(String[] args) {
        Pair<String, String> keyPair = EccCryptoUtil.generateEccKeyPair();
        System.out.println("keyPair = " + keyPair);
        System.out.println("publicKey = " + keyPair.getLeft());
        System.out.println("privateKey = " + keyPair.getRight());
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
        try {
            keyFactory = KeyFactory.getInstance("EC", "BC");
            kpg = KeyPairGenerator.getInstance("EC", "BC");
            kpg.initialize(256);
            cipher = Cipher.getInstance("ECIES", "BC");
            digest = MessageDigest.getInstance("SHA-256");
            signature = Signature.getInstance("SHA256withECDSA", "BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
            log.error("初始化密钥失败", e);
            throw new RuntimeException(e);
        }
    }

    // Base64 字符串解析 PublicKey
    public static PublicKey parsePublicKey(String publicKey) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    // Base64 字符串解析 PrivateKey
    public static PrivateKey parsePrivateKey(String privateKey) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    public static byte[] encrypt(String jsonParams, String publicKey) {
        // 要加密的数据
        byte[] dataToEncrypt = jsonParams.getBytes();
        // 使用公钥加密
        try {
            cipher.init(Cipher.ENCRYPT_MODE, parsePublicKey(publicKey));
            return cipher.doFinal(dataToEncrypt);
        } catch (Exception e) {
            log.error("加密失败", e);
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(byte[] encryptedData, String privateKey) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, parsePrivateKey(privateKey));
            return new String(cipher.doFinal(encryptedData));
        } catch (Exception e) {
            log.error("解密失败", e);
            throw new RuntimeException(e);
        }

    }

    // 使用私钥生成数字签名
    public static String signData(String data, String privateKey) {
        // 增加时间戳
        byte[] hash = digestData(data);

        try {
            signature.initSign(parsePrivateKey(privateKey));
            signature.update(hash);
            byte[] signedHash = signature.sign();

            return Base64.getEncoder().encodeToString(signedHash);
        } catch (Exception e) {
            log.error("数字签名失败", e);
            throw new RuntimeException(e);
        }
    }

    public static boolean verifySignature(String data, String sign, String publicKey) {
        byte[] hash = digestData(data);

        try {
            signature.initVerify(parsePublicKey(publicKey));
            signature.update(hash);
            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (Exception e) {
            log.error("数字签名验证失败", e);
            throw new RuntimeException(e);
        }
    }

    public static Pair<String, String> generateEccKeyPair() {
        // 生成密钥对
        KeyPair keyPair = kpg.generateKeyPair();

        // 获取公钥和私钥
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();

        // 将字节数组转换为 Base64 编码字符串以便显示
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKeyBytes);
        String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKeyBytes);
        return Pair.of(publicKeyBase64, privateKeyBase64);
    }

    private static byte[] digestData(String data) {
        // 增加时间戳,校验签名有效期
        LocalDateTime localDateTime = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        long timestamp = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        data = data + timestamp;
        return digest.digest(data.getBytes());
    }

}