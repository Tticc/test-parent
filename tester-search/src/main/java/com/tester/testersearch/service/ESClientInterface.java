package com.tester.testersearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;

/**
 * @Author 温昌营
 * @Date 2022-8-23 10:20:13
 */
public interface ESClientInterface {

    ElasticsearchClient getClient();

    ElasticsearchAsyncClient getAsyncClient();

    default String getSpecificAnalyzer() {
        return null;
    }

    ;
}
