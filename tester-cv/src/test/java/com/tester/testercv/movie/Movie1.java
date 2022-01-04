package com.tester.testercv.movie;


import com.tester.testercommon.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * movie new one
 */
@Slf4j
public class Movie1 {

    //解密算法名称
    private String method;

    //密钥
    private String key = "";


    //合并后的视频文件名称
    private String fileName = "qqq.mp4";

    private String folderPath = "C:\\Users\\wenc\\Desktop\\captureImg\\movie5";

    public static final String allListUrl = "";
    // 网页获取的
    public static final String trueListUrl = "https://xxx/index.m3u8";
    // 网页获取的key·
    public static final String trueKey = "";

    @Test
    public void test_movie() throws Exception {
        printAllList(allListUrl);
        List<String> tsList = getTSListAndMethod(trueListUrl);
        setDecryptKey(trueKey);

        HashMap hashMap = downLoadIndexFile(tsList, trueListUrl);

        // 看需求是否需要
//        composeFile(hashMap);
    }

    private void printAllList(String allListUrl) throws BusinessException {
        if (StringUtils.isEmpty(allListUrl)) {
            return;
        }
        StringBuilder allList = getUrlContent(allListUrl);
        System.out.println("allList = \n" + allList);
    }

    private List<String> getTSListAndMethod(String trueListUrl) throws BusinessException {
        List<String> tsList = new ArrayList<>();
        StringBuilder urlContent = getUrlContent(trueListUrl);
        String[] split = urlContent.toString().split("\\n");
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (s.contains("#EXTINF")) {
                boolean hasRelativePath = false;
                String tempUrl = split[++i];
                if (tempUrl.contains("/")) {
                    hasRelativePath = true;
                }
                //将 http://重新替换为http:!!
                String replace = trueListUrl.replace("//", "!!");
                // 截取
                if (hasRelativePath) {
                    replace = replace.substring(0, replace.indexOf("/"));
                } else {
                    replace = replace.substring(0, replace.lastIndexOf("/"));
                    replace = replace + "/";
                }
                // 将 http:!!重新替换为http://
                replace = replace.replace("!!", "//");
                tsList.add(replace + tempUrl);
            } else if (s.contains("EXT-X-KEY")) {
                String[] split1 = s.split(",", 2);
                if (split1[0].contains("METHOD")) {
                    method = split1[0].split("=", 2)[1];
                }
            }
        }
        return tsList;
    }

    private void setDecryptKey(String trueKey) throws BusinessException {
        if (StringUtils.isEmpty(trueKey)) {
            return;
        }
        StringBuilder urlContent = getUrlContent(trueKey);
        this.key = urlContent.toString().replace("\n", "");
    }


    private StringBuilder getUrlContent(String urls) throws BusinessException {
        int count = 1;
        System.out.println("url is:" + urls);
        HttpURLConnection httpURLConnection = null;
        StringBuilder content = new StringBuilder();
        while (count <= 3) {
            try {
                URL url = new URL(urls);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout((int) 20000);
                httpURLConnection.setReadTimeout((int) 20000);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoInput(true);
                String line;
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = bufferedReader.readLine()) != null)
                    content.append(line).append("\n");
                bufferedReader.close();
                inputStream.close();
//                System.out.println(content);
                break;
            } catch (Exception e) {
//                    System.out.println("第" + count + "获取链接重试！\t" + urls);
                count++;
//                    e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        }
        if (count > 3) {
            throw new BusinessException(201L, "连接超时！");
        }
        return content;
    }

    public HashMap downLoadIndexFile(List<String> urlList, String trueListUrl) throws Exception {
        HashMap<Integer, String> keyFileMap = new HashMap<>();
        File file1 = new File(folderPath);
        if (!file1.exists())
            file1.mkdirs();
        for (int i = 0; i < urlList.size(); i++) {
            String fileOutPath = folderPath + File.separator;
            String subUrlPath = urlList.get(i);

            int oriName = subUrlPath.lastIndexOf("/");
            String fileOutName = subUrlPath.substring(oriName + 1);

            try {
                downloadNet(subUrlPath, fileOutPath, fileOutName);
                keyFileMap.put(i, fileOutPath + fileOutName);
                System.out.println("成功：" + (i + 1) + "/" + urlList.size());
            } catch (Exception e) {
                System.err.println("*************失败：" + (i + 1) + "/" + urlList.size() + "************************************************************************");
            }
        }
        saveM3U8(trueListUrl, folderPath);

        return keyFileMap;
    }

    private void saveM3U8(String trueListUrl, String folderPath) throws Exception {

        StringBuilder urlContent = getUrlContent(trueListUrl);
        String[] split = urlContent.toString().split("\\n");
        StringBuilder finalM3U8Str = new StringBuilder();
        for (String s : split) {
            //如果含有此字段，则获取加密算法以及获取密钥的链接
            if (s.contains("EXT-X-KEY")) {
                continue;
            }
            int i = s.lastIndexOf("/");
            finalM3U8Str.append(s.substring(i + 1)).append("\n");
        }
        String path = folderPath + File.separator + "index.m3u8";
        try (FileOutputStream fs = new FileOutputStream(path)) {
            fs.write(finalM3U8Str.toString().getBytes());
        }
    }

    private void downloadNet(String fullUrlPath, String fileOutPath, String fileOutName) throws Exception {
        int byteread = 0;
        String tempFile = fileOutPath + "temp_" + fileOutName;
        String finalPath = fileOutPath + fileOutName;

        URL url = new URL(fullUrlPath);
        URLConnection conn = url.openConnection();
        try (InputStream inStream = conn.getInputStream();
             FileOutputStream fs = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                //bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
        }
        decrypt_agent(tempFile, key, method, finalPath);
        try {
            boolean delete = new File(tempFile).delete();
        } catch (Exception e) {
            System.out.println("delete temp file error: " + tempFile);
        }
    }

    public String composeFile(HashMap<Integer, String> keyFileMap) throws Exception {

        if (keyFileMap.isEmpty()) return null;

        String fileOutPath = folderPath + File.separator + fileName;
        FileOutputStream fileOutputStream = new FileOutputStream(new File(fileOutPath));
        byte[] bytes = new byte[1024];
        int length = 0;
        for (int i = 0; i < keyFileMap.size(); i++) {
            String nodePath = keyFileMap.get(i);
            File file = new File(nodePath);
            if (!file.exists()) continue;

            FileInputStream fis = new FileInputStream(file);
            while ((length = fis.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, length);
            }
        }

        return fileName;
    }

    private static void decrypt_agent(String filePath, String sKey, String method, String finalPath) {
        boolean needDecrypt = true;
        if (StringUtils.isEmpty(sKey) || StringUtils.isEmpty(method)) {
            needDecrypt = false;
        }
        File file = new File(finalPath);
        try (FileInputStream inputStream1 = new FileInputStream(filePath);
             FileOutputStream outputStream1 = new FileOutputStream(file)) {
            byte[] bytes1 = new byte[inputStream1.available()];
            inputStream1.read(bytes1);
            outputStream1.write(needDecrypt ? decrypt(bytes1, sKey, method) : bytes1);
        } catch (Exception e) {
            log.error("some thing went wrong", e);
        }
    }

    private static byte[] decrypt(byte[] sSrc, String sKey, String method) {
        try {
            if (!StringUtils.isEmpty(method) && !method.contains("AES"))
                throw new BusinessException(201L, "未知的算法！");
            // 判断Key是否正确
            if (StringUtils.isEmpty(sKey)) {
                return sSrc;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(sKey.getBytes("utf-8"), "AES");
            //如果m3u8有IV标签，那么IvParameterSpec构造函数就把IV标签后的内容转成字节数组传进去
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(new byte[16]);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
            return cipher.doFinal(sSrc);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
