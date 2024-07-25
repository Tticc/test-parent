package com.tester.testersearch.util;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.tester.testersearch.service.ESClientInterface;
import lombok.Data;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author 温昌营
 * @Date 2022-8-23 10:20:13
 */
public class ESClient_Bonsai implements InitializingBean, ESClientInterface {

    private ElasticsearchClient client;
    private ElasticsearchAsyncClient asyncClient;
//    public static RestHighLevelClient hlrc;

    @Value("${my.es.host:}")
    private String host;
    @Value("${my.es.port:}")
    private Integer port;

    @Value("${my.es.username:3906g77xl6}")
    private String username;

    @Value("${my.es.password:ew6glg92d5}")
    private String password;


    @Override
    public ElasticsearchClient getClient() {
        return client;
    }

    @Override
    public ElasticsearchAsyncClient getAsyncClient() {
        return asyncClient;
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        // 创建 CredentialsProvider
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        // 忽略 SSL 证书验证
        SSLContext sslContext = null;
        try {
            sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial((chain, authType) -> true).build();
        }  catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new RuntimeException("Failed to create SSL context", e);
        }
        final SSLContext fSslContext = sslContext;
        // Create the low-level client
        RestClient restClient = RestClient.builder(new HttpHost(host, port, "https"))
                .setRequestConfigCallback((build) -> build
                        .setConnectTimeout(20 * 1000)
                        .setConnectionRequestTimeout(20 * 1000))
                .setHttpClientConfigCallback(httpClientBuilder ->
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                                .setSSLContext(fSslContext)
                                .addInterceptorLast((HttpRequestInterceptor)(request, context) -> {
                                    Header[] headers = request.getHeaders("Content-Type");
                                    for (Header header : headers) {
                                        request.removeHeader(header);
                                    }
                                    request.addHeader("Content-Type", "application/json");
                                })
                                .addInterceptorLast((HttpResponseInterceptor)(request, context) -> {
                                    request.addHeader("X-Elastic-Product", "Elasticsearch");
                                })
                )
                .build();
        // Create the transport with a Jackson mapper
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        // And create the API client
        client = new ElasticsearchClient(transport);
        asyncClient = new ElasticsearchAsyncClient(transport);
    }
}
