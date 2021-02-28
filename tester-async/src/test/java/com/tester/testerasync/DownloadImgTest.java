package com.tester.testerasync;

import com.alibaba.fastjson.asm.FieldWriter;
import com.tester.testercommon.util.http.MyX509TrustManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


@Slf4j
public class DownloadImgTest {

    String basepath = "C:\\Users\\wenc\\Desktop\\captureImg\\img";

    RestTemplate restTemplate = new RestTemplate();

    public static SSLSocketFactory ssf;
    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";

    @Test
    public void test_download(){
        for (int i = 1; i < 199; i++) {
            String path = basepath+"\\pic1\\";
            String name = ""+getName(i);
            String url = "https://cdn.baidu.com/g/pic/"+i+".jpg";
            downloadImg(url,path,name);
        }
    }

    private String getName(Integer i){
        if(i < 10){
            return "00"+i;
        }else if(i < 100){
            return "0"+i;
        }else {
            return ""+i;
        }
    }

    private void downloadImg(String url, String path, String imgName) {
        // 创建文件夹
        File dir = new File(path);
        if(!dir.exists()){
            if(dir.mkdirs()){
                System.out.println("dir create success. --" + path);
            }else{
                System.out.println("dir create failed! --" + path);
                return;
            }
        }
        // 创建文件
        File file = new File(path + File.separator + imgName + ".jpg");
        try(InputStream inputStream = httpsRequestRetry(url,
                GET_METHOD,
                null,
                0,
                true,
                true,
                imgName)){
            try(FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buf = new byte[1024];
                int length = 0;
                while ((length = inputStream.read(buf, 0, buf.length)) != -1) {
                    fos.write(buf, 0, length);
                }
            }
        }catch (Exception ex){
            log.error("error.", ex);
        }


    }


    public static InputStream httpsRequestRetry(String requestUrl,
                                           String requestMethod,
                                           String outputStr,
                                           int retryCount,
                                           boolean isBizmpvers,
                                           boolean isHttps,
                                           String requestId) {
        long strat = System.currentTimeMillis();
        if(retryCount>2){//最多重试3次
            return null;
        }
        retryCount = retryCount + 1;
        String thisRequestId = requestId+"_"+retryCount;
        String jsonObject = null;
        log.info("httprequest start uuid=" + thisRequestId);
        HttpURLConnection httpUrlConn = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(requestUrl);
            if (isHttps) {
                HttpsURLConnection httpsUrlConn = (HttpsURLConnection) url.openConnection();
                httpsUrlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                httpUrlConn = httpsUrlConn;
                httpsUrlConn.setSSLSocketFactory(ssf);
                if(isBizmpvers){
                    httpsUrlConn.addRequestProperty("Bizmp-Version", "1.0");
                }
            }
            else {
                httpUrlConn = (HttpURLConnection) url.openConnection();
            }
            httpUrlConn.setConnectTimeout(10*1000);
            httpUrlConn.setReadTimeout(7 * 1000);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if (GET_METHOD.equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            inputStream = httpUrlConn.getInputStream();
            return inputStream;
        } catch (IOException ce) {
            log.error("httprequest error uuid=" + thisRequestId
                    + ";server connection timed out.", ce);
            return httpsRequestRetry(requestUrl, requestMethod, outputStr, retryCount, isBizmpvers, isHttps, requestId);
        } catch (Exception e) {
            log.error("httprequest error uuid=" + thisRequestId + ";", e);
        } finally {
            // 释放资源
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    log.error("httprequest BufferedReader close error uuid=" + thisRequestId, e);
                }
            }
            if(inputStreamReader != null){
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    log.error("httprequest InputStreamReader close error uuid=" + thisRequestId, e);
                }
            }
        }
        return null;
    }


    static{
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从SSLContext对象中得到SSLSocketFactory对象
            ssf = sslContext.getSocketFactory();
        } catch (Exception e) {
            log.error("init SSLSocketFactory error", e);
        }
    }
}
