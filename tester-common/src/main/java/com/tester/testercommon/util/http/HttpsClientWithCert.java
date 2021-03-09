package com.tester.testercommon.util.http;

import com.tester.testercommon.constant.ConstantList;
import com.tester.testercommon.util.cache.CacheManagerImpl;
import com.tester.testercommon.util.cache.ICacheManager;
import com.tester.testercommon.util.stream.ByteToInputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyStore;

/**
 * https with certificate
 */
@Slf4j
public class HttpsClientWithCert {

    private static ICacheManager iCacheManager = CacheManagerImpl.getInstance();


    /**
     * 附带证书请求
     * @Date 16:39 2021/3/9
     * @Author 温昌营
     **/
    public static String sendSSLPost(String url, String param, String path, String attachmentId, String password) throws Exception {
        InputStream inputStream = null;
        KeyStore keyStore = null;
        byte[] bytes ;
        try {

            String cacheKey = "cache_" + attachmentId;
            bytes = (byte[]) iCacheManager.getCacheDataByKey(cacheKey);
            if (bytes == null) {
                log.info("缓存中不存在对应的证书，需要重新下载：{}", attachmentId);
                inputStream = StringUtils.isEmpty(path) ? download(attachmentId) : new FileInputStream(new File(path));

                //同时存到缓存中
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                bufferedInputStream.mark(0);
                bytes = ByteToInputStream.toByteArray(bufferedInputStream);
                bufferedInputStream.reset();
                inputStream = bufferedInputStream;
                iCacheManager.putCache(cacheKey, bytes);
            } else {
                log.info("微信退款证书从缓存中取：{}", attachmentId);
                inputStream = ByteToInputStream.byte2Input(bytes);
            }

            //指定读取证书格式为PKCS12
            keyStore = KeyStore.getInstance("PKCS12");
            //指定PKCS12的密码
            keyStore.load(inputStream, password.toCharArray());
        } catch (Exception e) {
            log.error("下载证书异常：{}", e.getMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

        }
        if (keyStore != null) {
            //指定TLS版本
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, password.toCharArray())
                    .build();
            //设置httpclient的SSLSocketFactory
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            StringBuffer stringBuffer = new StringBuffer();
            try {
                HttpPost httpPost = new HttpPost(url);
                InputStream is = new ByteArrayInputStream(param.getBytes(ConstantList.UTF_8_STAND));
                //InputStreamEntity严格是对内容和长度相匹配的。用法和BasicHttpEntity类似
                InputStreamEntity inputStreamEntity = new InputStreamEntity(is, is.available());
                httpPost.setEntity(inputStreamEntity);
                CloseableHttpResponse response = httpclient.execute(httpPost);
                BufferedReader reader = null;
                try {
                    HttpEntity entity = response.getEntity();
                    reader = new BufferedReader(new InputStreamReader(
                            entity.getContent(), ConstantList.UTF_8_STAND));
                    String inputLine;
                    while ((inputLine = reader.readLine()) != null) {
                        stringBuffer.append(inputLine);
                    }
                } finally {
                    response.close();
                    if (reader != null) {
                        reader.close();
                    }
                }
            } finally {
                httpclient.close();
            }
            return stringBuffer.toString();
        }
        return null;
    }

    private static ByteArrayInputStream download(String attachmentId) {
        return new ByteArrayInputStream(attachmentId.getBytes());
    }
}
