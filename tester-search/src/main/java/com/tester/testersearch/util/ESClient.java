package com.tester.testersearch.util;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

/**
 * @Author 温昌营
 * @Date 2022-8-23 10:20:13
 */
public class ESClient {

    public static final ElasticsearchClient client;
    public static final ElasticsearchAsyncClient asyncClient;
//    public static RestHighLevelClient hlrc;

    static {
        // Create the low-level client
        RestClient restClient = RestClient.builder(
//                new HttpHost("10.10.38.4", 9200)).build();
                new HttpHost("localhost", 9200)).build();
//                new HttpHost("192.168.31.149", 9200)).build();
        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());
        // And create the API client
        client = new ElasticsearchClient(transport);
        asyncClient = new ElasticsearchAsyncClient(transport);
//        hlrc = new RestHighLevelClientBuilder(restClient)
//                .setApiCompatibilityMode(true)
//                .build();
    }
}
