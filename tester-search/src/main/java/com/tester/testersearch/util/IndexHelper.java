package com.tester.testersearch.util;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @Author 温昌营
 * @Date 2022-8-23 10:19:30
 */
@Slf4j
public class IndexHelper {

    public static ElasticsearchClient client = ESClient.client;

    public static void main(String[] args) throws Exception {
        test_create_index();
        test_delete_index();
    }


    public static void test_create_index() throws IOException {
        ElasticsearchIndicesClient indices = client.indices();
        CreateIndexResponse createIndexResponse = indices.create(req -> req
                .index("test_know1")
                .settings(s -> s.index(i -> i.routing(r -> r.allocation(a -> a.include(in -> in.tierPreference("data_content")))).numberOfShards("1").numberOfReplicas("0")))
                .mappings(m ->
                        m.properties("belong", p -> p.text(t -> t))
                                .properties("code", p -> p.text(t -> t))
                                .properties("keyword", p -> p.text(t -> t.analyzer("ik_max_word")))
                                .properties("title", p -> p.text(t -> t.analyzer("ik_max_word")))
                                .properties("description", p -> p.text(t -> t.analyzer("ik_max_word")))
                                .properties("detail", p -> p.text(t -> t.analyzer("ik_max_word")))
                                .properties("author", p -> p.text(t -> t))
                                .properties("priority", p -> p.long_(l -> l.nullValue(0L)))
                                .properties("type", p -> p.long_(l -> l.nullValue(0L)))
                                .properties("createdBy", p -> p.text(t -> t))
                                .properties("updatedBy", p -> p.text(t -> t))
                                .properties("createdTime", p -> p.long_(l -> l))
                                .properties("updatedTime", p -> p.long_(l -> l))
                                .properties("startTime", p -> p.long_(l -> l))
                                .properties("endTime", p -> p.long_(l -> l))
                                .properties("deleted", p -> p.long_(l -> l.nullValue(0L)))
                )
        );
        System.out.println("createIndexResponse = " + createIndexResponse);
    }

    public static void test_delete_index() throws IOException {
        ElasticsearchIndicesClient indices = client.indices();
        DeleteIndexResponse test_know1 = indices.delete(e -> e.index("test_know1"));
        System.out.println("test_know1 = " + test_know1);
    }
}
