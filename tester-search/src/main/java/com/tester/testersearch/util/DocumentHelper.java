package com.tester.testersearch.util;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.util.ObjectBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.function.Function;

/**
 * @Author 温昌营
 * @Date 2022-8-23 10:19:30
 */
@Slf4j
public class DocumentHelper {

    public static ElasticsearchClient client = ESClient.client;

    /**
     * @Date 15:27 2022/7/27
     * @Author 温昌营
     * @see ElasticsearchClient#search(java.util.function.Function, java.lang.Class)
     **/
    public final <TDocument> SearchResponse<TDocument> mySearch(
            Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>> fn, Class<TDocument> tDocumentClass)
            throws IOException, ElasticsearchException {
        SearchRequest build = fn.apply(new SearchRequest.Builder()).build();
        log.info("query request info:\n【{}】", build);
        SearchResponse<TDocument> search = client.search(build, tDocumentClass);
        log.info("query response info:\n【{}】", search);
        return search;
    }

    /**
     * @Date 9:49 2022/8/23
     * @Author 温昌营
     * @see ElasticsearchClient#create(java.util.function.Function)
     **/
    public final <TDocument> CreateResponse myCreate(
            Function<CreateRequest.Builder<TDocument>, ObjectBuilder<CreateRequest<TDocument>>> fn)
            throws IOException, ElasticsearchException {
        CreateRequest<TDocument> build = fn.apply(new CreateRequest.Builder<TDocument>()).build();
        log.info("create request info:\n【{}】", build);
        CreateResponse createResponse = client.create(build);
        log.info("create response info:\n【{}】", createResponse);
        return createResponse;
    }

    /**
     * @Date 9:49 2022/8/23
     * @Author 温昌营
     * @see ElasticsearchClient#update(java.util.function.Function, java.lang.Class)
     **/
    public final <TDocument, TPartialDocument> UpdateResponse<TDocument> myUpdate(
            Function<UpdateRequest.Builder<TDocument, TPartialDocument>, ObjectBuilder<UpdateRequest<TDocument, TPartialDocument>>> fn,
            Class<TDocument> tDocumentClass) throws IOException, ElasticsearchException {
        UpdateRequest<TDocument, TPartialDocument> build = fn.apply(new UpdateRequest.Builder<TDocument, TPartialDocument>()).build();
        log.info("update request info:\n【{}】", build);
        UpdateResponse<TDocument> update = client.update(build, tDocumentClass);
        log.info("update response info:\n【{}】", update);
        return update;
    }
}
