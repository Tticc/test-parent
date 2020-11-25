package com.tester.testercommon.util.file;

import com.tester.testercommon.util.endecrypt.AesSecurityBase64;
import com.tester.testercommon.util.endecrypt.RSAEncrypt;

import java.io.*;


/**
 * 文件加解密尝试
 * @Date 20:08 2020/11/25
 * @Author 温昌营
 **/
public class FileEncrypt {

    // rsa公钥
    private final static String publicKey = "30819f300d06092a864886f70d010101050003818d0030818902818100a4c073cc6fe54249307775c7b56a2ddcd349b317bae8bf9a3d53f0beadd0cfbf84a4d36fb10d869c6bdc04b25e4f15369e8c15e56d1f4c884655dd57a19f8381847443cedaff1e548ef486c98c457392f0dc7a3e928f83293604d17bc89e2f220ef0151c47b94c137f98735f2550a884afa3ccf133339745ee5184e523fd3a310203010001";
    // rsa私钥
    private final static String privateKey = "30820276020100300d06092a864886f70d0101010500048202603082025c02010002818100a4c073cc6fe54249307775c7b56a2ddcd349b317bae8bf9a3d53f0beadd0cfbf84a4d36fb10d869c6bdc04b25e4f15369e8c15e56d1f4c884655dd57a19f8381847443cedaff1e548ef486c98c457392f0dc7a3e928f83293604d17bc89e2f220ef0151c47b94c137f98735f2550a884afa3ccf133339745ee5184e523fd3a3102030100010281806d473207f25870e536fea5e2f7941c23930a197eadfd5fc9a0d022a977853fa75b063130ae91b82ddce52d405a89be1a6283acaa66fd11b87a5ecdd8285e33cd0d819f160444b8a36132641425eb0d4b2610abe11e60d96550d020642dc78854cb0d2052e9d678b51b8fe5e69970ce0104b51825e0fc2399c955394e332bf359024100dedf2fdaad9da69332e49793426f8e0af7ec20da915f85f2d36017bacc6fa6ca63edcae8f38a2a106e68dfe2e0e125ff7d2d5665a34cc836cfc2a28dacab95c3024100bd3da8082e366fc1ddf73d5e88e09b90085282645539b21a05a8559d0c7603998c1d77468ccb400c8906038d6b9a0b5960bd20a103f0d11ee58eab1ae94eccfb02406159377bdb2deb5e8c7250cadfcffe49483579bc3e976cd8bdff511e24c1f8269b3378d7fbd8b3baed9f1d7bea222e08c4d292d45e59e1721f99b28b138af725024100882a3921c9beb9a35147790cf0884ce6d2562c246d7e5a3c871d9c9ca83eb25065953e21b164fded071b20ab3d65560d35a2603bfb4de7eac75a649bb48c52c902405aaa60edaabf63fe0cd0b9f74fd44f4173c2f525f1a0faaf99447ac07e6217e26cf17a98ec7fd0d24487889e6043ec7938b819304c45d568016dfea67b950a47";


    public static void main(String[] args) throws Exception {
        String filePath = "C:\\Users\\Admin\\Desktop\\captureImg\\saved_minInAll_lined.png";
        File file = new File(filePath);
        String newFile = "C:\\Users\\Admin\\Desktop\\captureImg\\saved_minInAll_lined_enc.png";
        encryptFile(file,newFile);

        String sFileStr = "C:\\Users\\Admin\\Desktop\\captureImg\\saved_minInAll_lined_enc.png";
        File sFile = new File(sFileStr);
        String outFileStr = "C:\\Users\\Admin\\Desktop\\captureImg\\saved_minInAll_lined_dec.png";
        decryptFile(sFile,outFileStr);

    }

    /**
     * 解密文件
     * @param inputFile
     * @param outputFilePath
     * @throws Exception
     */
    private static void decryptFile(File inputFile,String outputFilePath) throws Exception {
        // 读取数据
        byte[] bytes = MyFileReaderWriter.file2Byte(inputFile);
        // 解密数据
        // 先rsa解密
        byte[] rsaDecrypt = RSAEncrypt.decrypt(bytes,privateKey);
        // 后aes解密
        byte[] decrypt = AesSecurityBase64.decrypt(rsaDecrypt);

        // 输出数据
        File newF = new File(outputFilePath);
        MyFileReaderWriter.byte2File(decrypt,newF);
    }

    /**
     * 加密文件
     * @param inputFile
     * @param outputFilePath
     * @throws Exception
     */
    private static void encryptFile(File inputFile,String outputFilePath) throws Exception {
        // 读取数据
        byte[] bytes = MyFileReaderWriter.file2Byte(inputFile);
        // 加密数据
        // 先aes加密
        byte[] encrypt = AesSecurityBase64.encrypt(bytes);
        // 后rsa加密
        byte[] rsaEncrypt = RSAEncrypt.encrypt(encrypt,publicKey);

        // 输出数据
        File newF = new File(outputFilePath);
        MyFileReaderWriter.byte2File(rsaEncrypt,newF);
    }
}
