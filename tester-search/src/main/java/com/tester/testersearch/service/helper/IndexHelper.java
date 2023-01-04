package com.tester.testersearch.service.helper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.util.ObjectBuilder;
import com.tester.testersearch.util.ESClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Function;

/**
 * @Author 温昌营
 * @Date 2022-8-23 10:19:30
 */
@Slf4j
@Component
public class IndexHelper {

    @Autowired
    private ESClient esClient;
//    public static ElasticsearchClient client = ESClient.client;

//    public static void main(String[] args) throws Exception {
//        test_create_index("test_knowledge");
//        test_delete_index("test_knowledge");
//    }

    /**
     * 创建索引
     *
     * @Date 15:01 2022/10/25
     * @Author 温昌营
     **/
    public final CreateIndexResponse myCreate(Function<CreateIndexRequest.Builder, ObjectBuilder<CreateIndexRequest>> fn)
            throws IOException, ElasticsearchException {
        CreateIndexRequest build = fn.apply(new CreateIndexRequest.Builder()).build();
        log.info("index create request info:\n【{}】", build);
        CreateIndexResponse createResponse = esClient.getClient().indices().create(build);
        log.info("index create response info:\n【{}】", createResponse);
        return createResponse;
    }

    /**
     * 删除索引
     *
     * @Date 15:01 2022/10/25
     * @Author 温昌营
     **/
    public final DeleteIndexResponse myDelete(Function<DeleteIndexRequest.Builder, ObjectBuilder<DeleteIndexRequest>> fn)
            throws IOException, ElasticsearchException {
        DeleteIndexRequest build = fn.apply(new DeleteIndexRequest.Builder()).build();
        log.info("index delete request info:\n【{}】", build);
        DeleteIndexResponse response = esClient.getClient().indices().delete(build);
        log.info("index delete response info:\n【{}】", response);
        return response;
    }


    /**
     * 创建索引。<br/>
     * 也作为创建索引的案例
     *
     * @Date 14:59 2022/10/25
     * @Author 温昌营
     **/
    public void test_create_index(String index) throws IOException {
        CreateIndexResponse createIndexResponse = myCreate(req -> req
                .index(index)
                .settings(s -> s.index(i -> i.routing(r -> r.allocation(a -> a.include(in -> in.tierPreference("data_content")))).numberOfShards("1").numberOfReplicas("0")))
                .mappings(m ->
                        m.properties("belong", p -> p.text(t -> t))
                                .properties("code", p -> p.keyword(t -> t))
                                .properties("keyword", p -> p.text(t -> t.analyzer("ik_max_word").copyTo("all")))
                                .properties("title", p -> p.text(t -> t.analyzer("ik_max_word").copyTo("all")))
                                .properties("description", p -> p.text(t -> t.analyzer("ik_max_word").copyTo("all")))
                                .properties("detail", p -> p.text(t -> t.analyzer("ik_smart").copyTo("all")))
                                .properties("author", p -> p.text(t -> t.analyzer("ik_max_word").copyTo("all")))
                                .properties("priority", p -> p.integer(l -> l.nullValue(0)))
                                .properties("type", p -> p.integer(l -> l.nullValue(0)))
                                .properties("createdBy", p -> p.keyword(t -> t))
                                .properties("updatedBy", p -> p.keyword(t -> t))
                                .properties("createdTime", p -> p.long_(l -> l))
                                .properties("updatedTime", p -> p.long_(l -> l))
                                .properties("startTime", p -> p.long_(l -> l))
                                .properties("endTime", p -> p.long_(l -> l))
                                .properties("deleted", p -> p.integer(l -> l.nullValue(0)))
                                .properties("all", p -> p.text(t -> t.analyzer("ik_max_word")))
                )
        );
        System.out.println("createIndexResponse = " + createIndexResponse);
    }

    /**
     * 删除索引。<br/>
     * 也作为删除索引的案例
     *
     * @Date 15:00 2022/10/25
     * @Author 温昌营
     **/
    public void test_delete_index(String index) throws IOException {
        DeleteIndexResponse test_know1 = myDelete(e -> e.index(index));
        System.out.println("test_know1 = " + test_know1);
    }

}
