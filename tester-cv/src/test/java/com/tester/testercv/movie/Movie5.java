package com.tester.testercv.movie;


import com.tester.base.dto.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.spec.AlgorithmParameterSpec;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * movie
 */
@Slf4j
public class Movie5 {

    //线程数
    private int threadCount = 12;

    //重试次数
    private int retryCount = 30;

    //链接连接超时时间（单位：毫秒）
    private long timeoutMillisecond = 1000L;

    //已完成ts片段个数
    private int finishedCount = 0;

    //解密算法名称
    private String method;

    //密钥
    private String key = "";

    //所有ts片段下载链接
    private Set<String> tsSet = new LinkedHashSet<>();

    //解密后的片段
    private Set<File> finishedFiles = new ConcurrentSkipListSet<>(Comparator.comparingInt(o -> Integer.parseInt(o.getName().replace(".xyz", ""))));

    //已经下载的文件大小
    private BigDecimal downloadBytes = new BigDecimal(0);





    //合并后的视频文件名称
    private String fileName = "qqq.mp4";

    private String folderPath = "C:\\Users\\wenc\\Desktop\\captureImg\\movie4";

    public static final String downloadUrl = "https://xxx/index.m3u8";
    // 网页获取的
    public static final String hardKeyUrl = "https://xxx/index.m3u8";
    // 网页获取的key
    public static final String retryKey = "https://cxx/key.key";

    @Test
    public void test_movie() throws Exception {
        List<String> tsSet = new ArrayList<>();
        String tsUrl = getTsUrl(tsSet);
        downLoadIndexFile(tsSet);
    }
    private String getTsUrl(List<String> tsSet) throws Exception {
        StringBuilder content = getUrlContent(downloadUrl);
        //判断是否是m3u8链接
        if (!content.toString().contains("#EXTM3U")) {
            throw new BusinessException(2000L, downloadUrl + "不是m3u8链接！");
        }
        String[] split = content.toString().split("\\n");
        String keyUrl = "";
        boolean isKey = false;
        for (String s : split) {
            //如果含有此字段，则说明只有一层m3u8链接
            if (s.contains("#EXT-X-KEY") || s.contains("#EXTINF")) {
                isKey = true;
                keyUrl = downloadUrl;
                break;
            }
            //如果含有此字段，则说明ts片段链接需要从第二个m3u8链接获取
            if (s.contains(".m3u8")) {
                String replace = downloadUrl.replace("//", "!!");
                replace = replace.substring(0, replace.indexOf("/")).replace("!!","//");
                String relativeUrl = replace;
                keyUrl = relativeUrl + s;
                break;
            }
        }
        if (StringUtils.isEmpty(keyUrl)) {
            keyUrl = hardKeyUrl;
//            throw new BusinessException(100L,"未发现key链接！");
        }
        //获取密钥

        String key1 = isKey ? getKey(keyUrl, content,tsSet) : getKey(keyUrl, null,tsSet);
        if (!StringUtils.isEmpty(key1)) {
            key = key1;
        }else {
            key = null;
        }
        return key;
    }

    private String getKey(String url, StringBuilder content,List<String> tsSet) throws BusinessException {
        StringBuilder urlContent;
        if (content == null || StringUtils.isEmpty(content.toString()))
            urlContent = getUrlContent(url);
        else urlContent = content;
        if (!urlContent.toString().contains("#EXTM3U"))
            throw new BusinessException(201L,downloadUrl + "不是m3u8链接！");
        String[] split = urlContent.toString().split("\\n");
        for (String s : split) {
            //如果含有此字段，则获取加密算法以及获取密钥的链接
            if (s.contains("EXT-X-KEY")) {
                String[] split1 = s.split(",", 2);
                if (split1[0].contains("METHOD"))
                    method = split1[0].split("=", 2)[1];
                if (split1[1].contains("URI"))
                    key = split1[1].split("=", 2)[1];
            }
        }
        String relativeUrl = url.substring(0, url.lastIndexOf("/") + 1);
        //将ts片段链接加入set集合
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (s.contains("#EXTINF")) {
                String tempUrl = split[++i];
//                tsSet.add(relativeUrl + tempUrl);
                String replace = url.replace("//", "!!");
                replace = replace.substring(0, replace.indexOf("/")).replace("!!","//");
                tsSet.add(replace + tempUrl);
            }
        }
        if (!StringUtils.isEmpty(key)) {
            key = key.replace("\"", "");
            key = key.substring(key.lastIndexOf("/")+1);
            try {
                return getUrlContent(relativeUrl + key).toString().replaceAll("\\s+", "");
            }catch (BusinessException be){
                log.error("error",be);
                log.info("retry key");
                return getUrlContent(retryKey).toString().replaceAll("\\s+", "");
            }
        }
//        System.out.println("set is:"+tsSet);
        return null;
    }
    private StringBuilder getUrlContent(String urls) throws BusinessException {
        int count = 1;
        System.out.println("url is:"+urls);
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
        System.out.println("content is:"+content);
        return content;
    }
    public HashMap downLoadIndexFile(List<String> urlList) throws Exception {
        HashMap<Integer,String> keyFileMap = new HashMap<>();
        File file1 = new File(folderPath);
        if (!file1.exists())
            file1.mkdirs();
        for(int i =0;i<urlList.size();i++){
            String subUrlPath = urlList.get(i);
            String fileOutPath = folderPath + File.separator + i + ".ts";
            keyFileMap.put(i, fileOutPath);
            try{
                String s = downloadNet(subUrlPath, fileOutPath);
                keyFileMap.put(i, s);
                System.out.println("成功："+ (i + 1) +"/" + urlList.size());
            }catch (Exception e){
                System.err.println("*************失败："+ (i + 1) +"/" + urlList.size()+"************************************************************************");
            }
        }
        composeFile(keyFileMap);

        return  keyFileMap;
    }

    private String downloadNet(String fullUrlPath, String fileOutPath) throws Exception {
        int byteread = 0;
        URL url = new URL(fullUrlPath);
        URLConnection conn = url.openConnection();
        InputStream inStream = conn.getInputStream();
        FileOutputStream fs = new FileOutputStream(fileOutPath);

        byte[] buffer = new byte[1204];
        while ((byteread = inStream.read(buffer)) != -1) {
            //bytesum += byteread;
            fs.write(buffer, 0, byteread);
        }
        return decrypt_agent(fileOutPath, key, method);
    }

    public String composeFile(HashMap<Integer,String> keyFileMap) throws Exception{

        if(keyFileMap.isEmpty()) return null;

        String fileOutPath = folderPath + File.separator + fileName;
        FileOutputStream fileOutputStream = new FileOutputStream(new File(fileOutPath));
        byte[] bytes = new byte[1024];
        int length = 0;
        for(int i=0; i<keyFileMap.size(); i++){
            String nodePath = keyFileMap.get(i);
            File file = new File(nodePath);
            if(!file.exists())  continue;

            FileInputStream fis = new FileInputStream(file);
            while ((length = fis.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, length);
            }
        }

        return fileName;
    }

    private static String decrypt_agent(String filePath, String sKey, String method){
        if(StringUtils.isEmpty(sKey)||StringUtils.isEmpty(method)){
            return filePath;
        }
        try {
            FileInputStream inputStream1 = new FileInputStream(filePath);
            byte[] bytes1 = new byte[inputStream1.available()];
            inputStream1.read(bytes1);
            String tempPath = filePath.substring(0, filePath.lastIndexOf(".")) + "000.ts";
            File file = new File(tempPath);
            FileOutputStream outputStream1 = new FileOutputStream(file);
            outputStream1.write(decrypt(bytes1, sKey, method));
            return tempPath;
        }catch (Exception e){
            log.error("some thing went wrong",e);
        }
        return filePath;
    }

    private static byte[] decrypt(byte[] sSrc, String sKey, String method) {
        try {
            if (!StringUtils.isEmpty(method) && !method.contains("AES"))
                throw new BusinessException(201L,"未知的算法！");
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
