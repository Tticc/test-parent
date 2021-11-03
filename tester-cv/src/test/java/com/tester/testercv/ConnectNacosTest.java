package com.tester.testercv;

import com.google.common.net.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.GZIPInputStream;

@Slf4j
public class ConnectNacosTest {

    private static ConnectNacosTest_Sign sign = new ConnectNacosTest_Sign();

//    private static final String serviceName = "DEFAULT_GROUP@@card-platform-backend";
//    private static final String serviceName = "DEFAULT_GROUP@@middleend-order-backend";
    private static final String serviceName = "DEFAULT_GROUP@@giftcard-prepay-api";

    private static final int UDP_MSS = 64 * 1024;
    public static final String UTF_8 = "UTF-8";
    public static final int TIME_OUT_MILLIS = Integer
            .getInteger("com.alibaba.nacos.client.naming.ctimeout", 50000);
    public static final int CON_TIME_OUT_MILLIS = Integer
            .getInteger("com.alibaba.nacos.client.naming.ctimeout", 3000);
    private static final String POST = "POST";
    private static final String PUT = "PUT";

    private static final String LOCAL_NAMESPACE_ID = "244cede4-d276-4f64-8ede-8c7fc944ca05"; // dev-api
//    private static final String NAMESPACE_ID = "6b9123fb-8a52-41c7-b206-0ad2c0adbe3d"; // dev-backend


    private static final String port = "8080"; // dev-api


    @Test
    public void test_regAndKeepAlive() throws Exception {
        String ipPrefix = "10.100.69.";
        int startIndex = 67;
        int endIndex = 68;
        for (int i = startIndex; i < endIndex; i++) {
            String ip = ipPrefix + i;
            doReg(ip);
        }
        while (true){
            for (int i = startIndex; i < endIndex; i++) {
                String ip = ipPrefix+i;
                keep_alive(ip);
            }
            Thread.sleep(3000L);
        }
    }

    @Test
    public void test_reg() throws InterruptedException {
        String ipPrefix = "10.100.69.";
//        while (true) {
            for (int i = 0; i < 25; i++) {
                String ip = ipPrefix + i;
                doReg(ip);
            }
//            Thread.sleep(5000L);
//        }
    }

    @Test
    public void test_keep_alive() throws InterruptedException {
        String ipPrefix = "10.100.69.";
        while (true){
            for (int i = 0; i < 5; i++) {
                String ip = ipPrefix+i;
                keep_alive(ip);
            }
            Thread.sleep(3000L);
        }
    }

    public void keep_alive(String ip){
        String url = "http://dev-nacos.aeonbuy.com/nacos/v1/ns/instance/beat";
        Map<String,String> params = new HashMap<>();
        params.put(CommonParams.NAMESPACE_ID, LOCAL_NAMESPACE_ID);
        params.put("serviceName",serviceName);
        params.put("encoding",UTF_8);
        params.put("beat","{\"cluster\":\"DEFAULT\",\"ip\":\""+ip+"\",\"metadata\":{\"preserved.register.source\":\"SPRING_CLOUD\"},\"period\":5000,\"port\":"+port+",\"scheduled\":false,\"serviceName\":\""+serviceName+"\",\"stopped\":false,\"weight\":1.0}");
        List<String> headers = builderHeaders();
        sign.checkSignature(params);
        String encoding = UTF_8;
        String method = PUT;
        HttpResult request = request(url, headers, params, encoding, method);
        System.out.println(request);
    }


    public void doReg(String ip){
        String url = "http://dev-nacos.aeonbuy.com:80/nacos/v1/ns/instance";
        Map<String,String> params = new HashMap<>();
        params.put(CommonParams.NAMESPACE_ID, LOCAL_NAMESPACE_ID);
//        params.put(CommonParams.SERVICE_NAME, serviceName);
        params.put(CommonParams.SERVICE_NAME, serviceName);
        params.put(CommonParams.GROUP_NAME, "DEFAULT_GROUP");
        params.put(CommonParams.CLUSTER_NAME, "DEFAULT");
        params.put("ip", ip);
        params.put("port", port);
        params.put("weight", "1.0");
        params.put("enable", "true");
        params.put("healthy", "true");
        params.put("ephemeral", "true");
        params.put("metadata", "{\"preserved.register.source\":\"SPRING_CLOUD\"}");


        List<String> headers = builderHeaders();
        sign.checkSignature(params);
        String encoding = UTF_8;
        String method = POST;
        HttpResult request = request(url, headers, params, encoding, method);
        System.out.println(request);
    }



    public static HttpResult request(String url, List<String> headers, Map<String, String> paramValues, String encoding, String method) {
        HttpURLConnection conn = null;
        try {
            String encodedContent = encodingParams(paramValues, encoding);
            url += (StringUtils.isEmpty(encodedContent)) ? "" : ("?" + encodedContent);

            conn = (HttpURLConnection) new URL(url).openConnection();

            setHeaders(conn, headers, encoding);
            conn.setConnectTimeout(CON_TIME_OUT_MILLIS);
            conn.setReadTimeout(TIME_OUT_MILLIS);
            conn.setRequestMethod(method);
            conn.setDoOutput(true);
            if (POST.equals(method) || PUT.equals(method)) {
                // fix: apache http nio framework must set some content to request body
                byte[] b = encodedContent.getBytes();
                conn.setRequestProperty("Content-Length", String.valueOf(b.length));
                conn.getOutputStream().write(b, 0, b.length);
                conn.getOutputStream().flush();
                conn.getOutputStream().close();
            }
            conn.connect();
            log.debug("Request from server: " + url);
            return getResult(conn);
        } catch (Exception e) {
            try {
                if (conn != null) {
                    log.warn("failed to request " + conn.getURL() + " from "
                            + InetAddress.getByName(conn.getURL().getHost()).getHostAddress());
                }
            } catch (Exception e1) {
                log.error("[NA] failed to request ", e1);
                //ignore
            }

            log.error("[NA] failed to request ", e);

            return new HttpResult(500, e.toString(), Collections.<String, String>emptyMap());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
    public List<String> builderHeaders() {
        List<String> headers = Arrays.asList("Client-Version", "Nacos-Java-Client:v1.1.1",
                "User-Agent", "Nacos-Java-Client:v1.1.1",
                "Accept-Encoding", "gzip,deflate,sdch",
                "Connection", "Keep-Alive",
                "RequestId", generateUuid(), "Request-Module", "Naming");
        return headers;
    }
    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    private static void setHeaders(HttpURLConnection conn, List<String> headers, String encoding) {
        if (null != headers) {
            for (Iterator<String> iter = headers.iterator(); iter.hasNext(); ) {
                conn.addRequestProperty(iter.next(), iter.next());
            }
        }

        conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset="
                + encoding);
        conn.addRequestProperty("Accept-Charset", encoding);
    }

    private static String encodingParams(Map<String, String> params, String encoding)
            throws UnsupportedEncodingException {
        if (null == params || params.isEmpty()) {
            return "";
        }

        params.put("encoding", encoding);
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (StringUtils.isEmpty(entry.getValue())) {
                continue;
            }

            sb.append(entry.getKey()).append("=");
            sb.append(URLEncoder.encode(entry.getValue(), encoding));
            sb.append("&");
        }

        if (sb.length() > 0) {
            sb = sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private static HttpResult getResult(HttpURLConnection conn) throws IOException {
        int respCode = conn.getResponseCode();

        InputStream inputStream;
        if (HttpURLConnection.HTTP_OK == respCode
                || HttpURLConnection.HTTP_NOT_MODIFIED == respCode
                || 307 == respCode) {
            inputStream = conn.getInputStream();
        } else {
            inputStream = conn.getErrorStream();
        }

        Map<String, String> respHeaders = new HashMap<String, String>(conn.getHeaderFields().size());
        for (Map.Entry<String, List<String>> entry : conn.getHeaderFields().entrySet()) {
            respHeaders.put(entry.getKey(), entry.getValue().get(0));
        }

        String encodingGzip = "gzip";

        if (encodingGzip.equals(respHeaders.get(HttpHeaders.CONTENT_ENCODING))) {
            inputStream = new GZIPInputStream(inputStream);
        }

        return new HttpResult(respCode, MytoString(inputStream, getCharset(conn)), respHeaders);
    }

    static public String MytoString(InputStream input, String encoding) {

        try {
            return (null == encoding) ? toString(new InputStreamReader(input, UTF_8))
                    : toString(new InputStreamReader(input, encoding));
        } catch (Exception e) {
            log.error("NA", "read input failed.", e);
            return "";
        }
    }
    static public String toString(Reader reader) throws IOException {
        CharArrayWriter sw = new CharArrayWriter();
        copy(reader, sw);
        return sw.toString();
    }
    static public long copy(Reader input, Writer output) throws IOException {
        char[] buffer = new char[1 << 12];
        long count = 0;
        for (int n = 0; (n = input.read(buffer)) >= 0; ) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
    private static String getCharset(HttpURLConnection conn) {
        String contentType = conn.getContentType();
        if (StringUtils.isEmpty(contentType)) {
            return "UTF-8";
        }

        String[] values = contentType.split(";");
        if (values.length == 0) {
            return "UTF-8";
        }

        String charset = "UTF-8";
        for (String value : values) {
            value = value.trim();

            if (value.toLowerCase().startsWith("charset=")) {
                charset = value.substring("charset=".length());
            }
        }

        return charset;
    }
    public static class HttpResult {
        final public int code;
        final public String content;
        final private Map<String, String> respHeaders;

        public HttpResult(int code, String content, Map<String, String> respHeaders) {
            this.code = code;
            this.content = content;
            this.respHeaders = respHeaders;
        }

        public String getHeader(String name) {
            return respHeaders.get(name);
        }
    }

    public class CommonParams {

        public static final String SERVICE_NAME = "serviceName";

        public static final String CLUSTER_NAME = "clusterName";

        public static final String NAMESPACE_ID = "namespaceId";

        public static final String GROUP_NAME = "groupName";

    }
}
