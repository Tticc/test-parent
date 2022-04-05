package com.tester.testercommon.util.http;

import com.alibaba.fastjson.JSONObject;
import com.tester.testercommon.enums.BussinessExceptionEnum;
import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.MyConsumer;
import com.tester.testercommon.util.MyFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * https/http请求管理
 *
 * @Date 11:15 2021/10/22
 * @Author 温昌营
 **/
@Slf4j
public class HttpsClient {

    public static SSLSocketFactory ssf;

    public static final String GET_METHOD = "GET";

    public static final String POST_METHOD = "POST";

    public static final String HTTPS = "https";

    static {
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从SSLContext对象中得到SSLSocketFactory对象
            ssf = sslContext.getSocketFactory();
        } catch (Exception e) {
            log.error("init SSLSocketFactory error", e);
        }
    }

    /**
     * 常用的get请求
     * @Date 11:08 2021/11/9
     * @Author 温昌营
     **/
    public static JSONObject commonHttpsGetRequest(String url) throws BusinessException {
        Map<String, String> propertyMap = new HashMap<>();
        propertyMap.put("Content-Type", "application/json;charset=UTF-8");
        return (JSONObject) JSONObject.parse(requestForString(url, GET_METHOD, null, propertyMap, null));
    }

    /**
     * 常用的post请求
     * @Date 11:08 2021/11/9
     * @Author 温昌营
     **/
    public static JSONObject commonHttpsPostRequest(String url, JSONObject params) throws BusinessException {
        Map<String, String> propertyMap = new HashMap<>();
        propertyMap.put("Content-Type", "application/json;charset=UTF-8");
        return (JSONObject) JSONObject.parse(requestForString(url, POST_METHOD, params, propertyMap, null));
    }
    /**
     * 发送请求，将返回转为字符串
     *
     * @Date 10:19 2021/11/9
     * @Author 温昌营
     **/
    public static String requestForString(String url,
                                          String requestMethod,
                                          @Nullable JSONObject reqParams,
                                          @Nullable Map<String, String> propertyMap) throws BusinessException {
        return requestForString(url, requestMethod, reqParams, propertyMap,null);
    }

    /**
     * 发送请求，将返回转为字符串
     *
     * @Date 10:19 2021/11/9
     * @Author 温昌营
     **/
    public static String requestForString(String url,
                                          String requestMethod,
                                          @Nullable JSONObject reqParams,
                                          @Nullable Map<String, String> propertyMap,
                                          @Nullable Proxy proxy) throws BusinessException {
        return requestForString(url, requestMethod, reqParams, propertyMap,proxy,null);
    }

    /**
     * 发送请求，将返回转为字符串
     *
     * @Date 10:19 2021/11/9
     * @Author 温昌营
     **/
    public static String requestForString(String url,
                                          String requestMethod,
                                          @Nullable JSONObject reqParams,
                                          @Nullable Map<String, String> propertyMap,
                                          @Nullable Proxy proxy,
                                          @Nullable MyConsumer<HttpURLConnection> consumer) throws BusinessException {
        AtomicReference<String> resContainer = new AtomicReference<>(null);
        doHttpRequest_sub(url, requestMethod, reqParams, propertyMap, proxy, (inputStream) -> {
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                String str;
                StringBuilder sb = new StringBuilder();
                while ((str = bufferedReader.readLine()) != null) {
                    sb.append(str);
                }
                String res = sb.toString();
                resContainer.set(res);
                if (StringUtils.isEmpty(res)) {
                    log.warn("empty result");
                }
            } catch (Exception e) {
                log.error("error.", e);
                throw new BusinessException(BussinessExceptionEnum.IO_ERROR.getCode());
            }
            return resContainer.get();
        },consumer);
        return resContainer.get();
    }
    /**
     * 发送请求，将返回的数据流写入文件
     *
     * @Date 10:26 2021/11/9
     * @Author 温昌营
     **/
    public static void requestForFile(String url,
                                      String requestMethod,
                                      @Nullable JSONObject reqParams,
                                      @Nullable Map<String, String> propertyMap,
                                      File outFile) throws BusinessException {
        requestForFile(url, requestMethod, reqParams, propertyMap, outFile,null);
    }

    /**
     * 发送请求，将返回的数据流写入文件
     *
     * @Date 10:26 2021/11/9
     * @Author 温昌营
     **/
    public static void requestForFile(String url,
                                      String requestMethod,
                                      @Nullable JSONObject reqParams,
                                      @Nullable Map<String, String> propertyMap,
                                      File outFile,
                                      @Nullable Proxy proxy) throws BusinessException {
        requestForFile(url, requestMethod, reqParams, propertyMap, outFile,proxy,null);
    }

    /**
     * 发送请求，将返回的数据流写入文件
     *
     * @Date 10:26 2021/11/9
     * @Author 温昌营
     **/
    public static void requestForFile(String url,
                                      String requestMethod,
                                      @Nullable JSONObject reqParams,
                                      @Nullable Map<String, String> propertyMap,
                                      File outFile,
                                      @Nullable Proxy proxy,
                                      @Nullable MyConsumer<HttpURLConnection> consumer) throws BusinessException {
        doHttpRequest_sub(url, requestMethod, reqParams, propertyMap, proxy, (inputStream) -> {
            try (FileOutputStream fo = new FileOutputStream(outFile)) {
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(bytes, 0, bytes.length)) != -1) {
                    fo.write(bytes, 0, len);
                }
            } catch (Exception e) {
                log.error("error.", e);
                throw new BusinessException(BussinessExceptionEnum.IO_ERROR.getCode());
            }
            return "FILE";
        }, consumer);
    }


    /**
     * @param requestUrl    请求地址 不能为null
     * @param requestMethod 请求方法. GET/POST 不能为null
     * @param reqParams     请求参数 可为null
     * @param propertyMap   其他属性。如header 可为null
     * @param proxy         代理 可为null
     * @param function      响应流消费者 不能为null
     * @return void
     * @Date 9:43 2021/11/9
     * @Author 温昌营
     **/
    public static void doHttpRequest_sub(String requestUrl,
                                         String requestMethod,
                                         @Nullable JSONObject reqParams,
                                         @Nullable Map<String, String> propertyMap,
                                         @Nullable Proxy proxy,
                                         MyFunction<InputStream, String> function,
                                         @Nullable MyConsumer<HttpURLConnection> consumer) throws BusinessException {
        assert !StringUtils.isEmpty(requestUrl) : "requestUrl empty";
        assert !StringUtils.isEmpty(requestMethod) : "requestMethod empty";
        assert !StringUtils.isEmpty(requestMethod) : "requestMethod empty";

        long start = System.currentTimeMillis();
        HttpURLConnection httpUrlConn = null;
        String res = null;
        try {
            URL url = new URL(requestUrl);
            httpUrlConn = proxy == null ? (HttpURLConnection) url.openConnection() : (HttpURLConnection) url.openConnection(proxy);
            if (requestUrl.startsWith(HTTPS)) {
                httpUrlConn = proxy == null ? (HttpsURLConnection) url.openConnection() : (HttpsURLConnection) url.openConnection(proxy);
                ((HttpsURLConnection) httpUrlConn).setSSLSocketFactory(ssf);
                // 这有什么用吗？ 2021-11-9 09:28:35
//                if (isBizmpvers) {
//                    httpUrlConn.addRequestProperty("Bizmp-Version", "1.0");
//                }
            }
            httpUrlConn.setConnectTimeout(10 * 1000);
            httpUrlConn.setReadTimeout(7 * 1000);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if (!CollectionUtils.isEmpty(propertyMap)) {
                Set<Map.Entry<String, String>> entries = propertyMap.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    // such as
                    // httpUrlConn.setRequestProperty("Cookie","_ga=GA1.2.254215258.1610677327; SERVERID=10.10.36.74:8083; JSESSIONID=6498FC4F1154F1AB554875BDC386498C");
                    // httpUrlConn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
                    httpUrlConn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            if (GET_METHOD.equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (null != reqParams) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                outputStream.write(reqParams.toString().getBytes(StandardCharsets.UTF_8));
                outputStream.close();
            }
            try (InputStream inputStream = httpUrlConn.getInputStream()) {
                // 这里可以读取返回头
                if(consumer != null){
                    consumer.accept(httpUrlConn);
                }
                // 读取返回体数据流
                res = function.apply(inputStream);
            } catch (Exception e) {
                log.error("stream process error");
                throw e;
            }
        } catch (Exception e) {
            log.error("http request error.", e);
            throw new BusinessException(BussinessExceptionEnum.HTTP_REQUEST_ERROR.getCode());
        } finally {
            // 释放连接
            try {
                if (httpUrlConn != null) {
                    httpUrlConn.disconnect();
                }
            } catch (Exception e) {
                log.error("close httpUrlConn error.", e);
            }
            log.info("http request end. time cost: {}. requestUrl={}. reqParams={}. return={}"
                    , System.currentTimeMillis() - start, requestUrl, reqParams, res);
        }
    }

    /**
     * 根据代理服务器的ip和端口获取http代理
     *
     * @param ip
     * @param port
     * @return java.net.Proxy
     * @Date 10:57 2021/11/9
     * @Author 温昌营
     **/
    public static Proxy getProxy(String ip, int port) {
        InetSocketAddress addr = new InetSocketAddress(ip, port);
        return new Proxy(Proxy.Type.HTTP, addr);
    }
}
