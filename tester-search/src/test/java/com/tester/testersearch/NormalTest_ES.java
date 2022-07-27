package com.tester.testersearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.util.ObjectBuilder;
import com.tester.testersearch.esUtil.Knowledge;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.junit.Test;

import java.io.IOException;
import java.util.function.Function;


@Slf4j
public class NormalTest_ES {
    public static ElasticsearchClient client;

    static {
        // Create the low-level client
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200)).build();
        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());
        // And create the API client
        client = new ElasticsearchClient(transport);
    }


    @Test
    public void test_firstRequest() throws Exception {
        SearchResponse<Knowledge> search = mySearch(s -> s
                        .index("test_knowledge")
                        .query(q -> q
                                .bool(q1 -> q1.should(l -> l.match(m -> m.field("desc").query("转眼之间")))
//                                .term(t -> t
//                                        .field("desc")
//                                        .value(v -> v.stringValue("转眼之间"))
//                                )
                                )).size(10).from(0),
                Knowledge.class);

        for (Hit<Knowledge> hit : search.hits().hits()) {
            processProduct(hit.source());
        }
    }

    public void processProduct(Knowledge knowledge) {
        System.out.println("knowledge = " + knowledge);
    }


    /**
     * @Date 15:27 2022/7/27
     * @Author 温昌营
     * @see ElasticsearchClient#search(java.util.function.Function, java.lang.Class)
     **/
    public final <TDocument> SearchResponse<TDocument> mySearch(
            Function<SearchRequest.Builder, ObjectBuilder<SearchRequest>> fn, Class<TDocument> tDocumentClass)
            throws IOException, ElasticsearchException {
        SearchRequest build = fn.apply(new SearchRequest.Builder()).build();
        log.info("request info:{}", build);
        return client.search(build, tDocumentClass);
    }


}
