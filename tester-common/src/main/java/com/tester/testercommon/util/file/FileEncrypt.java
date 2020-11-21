package com.tester.testercommon.util.file;

import com.tester.testercommon.util.endecrypt.AesSecurityBase64;

import java.io.*;

public class FileEncrypt {



    public static void main(String[] args) throws Exception {
        String filePath = "C:\\Users\\wenc\\Desktop\\captureImg\\encrypt\\oriFile.png";
        File file = new File(filePath);
        String newFile = "C:\\Users\\wenc\\Desktop\\captureImg\\encrypt\\encFile";
        encryptFile(file,newFile);

        String sFileStr = "C:\\Users\\wenc\\Desktop\\captureImg\\encrypt\\encFile";
        File sFile = new File(sFileStr);
        String outFileStr = "C:\\Users\\wenc\\Desktop\\captureImg\\encrypt\\decFile";
        decryptFile(sFile,outFileStr);

    }

    /**
     * 解密文件
     * @param file
     * @param outputFilePath
     * @throws Exception
     */
    public static void decryptFile(File file,String outputFilePath) throws Exception {
        // 读取数据
        byte[] bytes = MyFileReader.readFileToByte(file);
        // 解密数据
        byte[] encrypt = AesSecurityBase64.decrypt(bytes);

        // 输出数据
        File newF = new File(outputFilePath);
        OutputStream os = null;
        try {
            os = new FileOutputStream(newF);
            os.write(encrypt, 0, encrypt.length);
            os.flush();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("文件没有找到！");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("写入文件失败！");
        }finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("关闭输出流失败！");
                }
            }
        }
    }

    /**
     * 加密文件
     * @param file
     * @param outputFilePath
     * @throws Exception
     */
    public static void encryptFile(File file,String outputFilePath) throws Exception {
        // 读取数据
        byte[] bytes = MyFileReader.readFileToByte(file);
        // 加密数据
        byte[] encrypt = AesSecurityBase64.encrypt(bytes);

        // 输出数据
        File newF = new File(outputFilePath);
        OutputStream os = null;
        try {
            os = new FileOutputStream(newF);
            os.write(encrypt, 0, encrypt.length);
            os.flush();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("文件没有找到！");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("写入文件失败！");
        }finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("关闭输出流失败！");
                }
            }
        }
    }
}
