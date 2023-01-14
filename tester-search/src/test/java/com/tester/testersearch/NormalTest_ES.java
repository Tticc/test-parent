package com.tester.testersearch;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.CreateResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggest;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.Suggestion;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.alibaba.fastjson.JSONObject;
import com.tester.testercommon.util.MyConsumer;
import com.tester.testersearch.model.Knowledge;
import com.tester.testersearch.model.KnowledgePageRequest;
import com.tester.testersearch.service.helper.DocumentHelper;
import com.tester.testersearch.service.helper.IndexHelper;
import com.tester.testersearch.util.EsSearchHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Slf4j
public class NormalTest_ES {


    private static ElasticsearchClient client;
    private static ElasticsearchAsyncClient asyncClient;
//    public static RestHighLevelClient hlrc;

    static {

        // Create the low-level client
        RestClient restClient = RestClient.builder(new HttpHost("127.0.0.1", 9200))
                .setRequestConfigCallback((build) -> build
                        .setConnectTimeout(20 * 1000)
                        .setConnectionRequestTimeout(20 * 1000))
                .build();
//                new HttpHost("192.168.31.149", 9200)).build();
        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        // And create the API client
        client = new ElasticsearchClient(transport);
        asyncClient = new ElasticsearchAsyncClient(transport);
//        hlrc = new RestHighLevelClientBuilder(restClient)
//                .setApiCompatibilityMode(true)
//                .build();
    }


    private DocumentHelper documentHelper = new DocumentHelper();

    private IndexHelper indexHelper = new IndexHelper();


    @Test
    public void test_update() throws Exception {
        Knowledge knowledge = new Knowledge();
        knowledge.setTitle("test document;tet doc").setUpdatedTime(new Date());
        documentHelper.myUpdate(client, e -> e.id("78266cd9834c4b2e96e1f0974afb5fcf").index("test_knowledge").doc(knowledge), Knowledge.class);
    }


    @Test
    public void test_firstRequest() throws Exception {
        String key = null;
        KnowledgePageRequest kn = new KnowledgePageRequest();
        kn.setType(2);
        kn.setTitle("黑暗");
        kn.setDescription("bb");
//        SearchResponse<Knowledge> search = documentHelper.mySearch(s -> s
//                        .index("test_knowledge")
//                        .query(q -> q
//                                .bool(q1 -> q1.should(l -> l.match(e -> e.field("detail").query("森林").analyzer("ik_smart").minimumShouldMatch("2").boost(1f)))
//                                        .should(l -> l.term(e -> e.field("keyword").value("欧莎").boost(99f)))
////                                        .should(l -> l.multiMatch(e -> e.fields(Arrays.asList("description", "keyword")).query("转眼之间")))
//                                )).size(10).from(0),
//        Knowledge.class);
        SearchResponse<Knowledge> search = documentHelper.mySearch(client, s -> s
                .index("test_knowledge")
//                .query(q -> q.bool(q1 -> baseProcess(q1, kn))
                .query(q -> q.bool(f1 -> f1
                                .filter(q1 -> q1.bool(q2 -> processIkSmart(q2, kn)))
                                .filter(t -> t.term(t1 -> processTerm(t1, kn)))
                        )
//                                        .should(l -> l.multiMatch(e -> e.fields(Arrays.asList("description", "keyword")).query("转眼之间")))
                ).size(10).from(0), Knowledge.class);

        for (Hit<Knowledge> hit : search.hits().hits()) {
            processKnowledge(hit.source());
        }
    }

    @Test
    public void test_aggregations() throws Exception {
        Knowledge kn = new Knowledge();
        kn.setType(2);
        kn.setTitle("黑暗");
        SearchResponse<Knowledge> search = documentHelper.mySearch(client, s -> s
                        .index("test_knowledge")
                        .size(0)
//                .suggest((su) -> su.suggesters("name_suggester", (se) -> se.completion(co -> co.skipDuplicates(true).field("name").size(10))).text("j"))
                        .aggregations("aggMap1", (ag) -> ag.terms(t -> t.field("type").size(10)).aggregations("score_stats", (st) -> st.stats(t -> t.field("type"))))
                , Knowledge.class);

        for (Hit<Knowledge> hit : search.hits().hits()) {
            processKnowledge(hit.source());
        }
        Map<String, Aggregate> aggregations = search.aggregations();
        Aggregate aggMap1 = aggregations.get("aggMap1");
        LongTermsAggregate lterms = aggMap1.lterms();
        Buckets<LongTermsBucket> buckets = lterms.buckets();
        List<LongTermsBucket> array = buckets.array();
        for (LongTermsBucket longTermsBucket : array) {
            System.out.println("longTermsBucket = " + longTermsBucket);
            long key = longTermsBucket.key();
            String s = longTermsBucket.keyAsString();
            Map<String, Aggregate> aggregations1 = longTermsBucket.aggregations();
            Aggregate score_stats = aggregations1.get("score_stats");
            StatsAggregate stats = score_stats.stats();
            double avg = stats.avg();
            double min = stats.min();
            System.out.println("min = " + min);
        }
    }

    @Test
    public void test_suggest() throws Exception {
        SearchResponse<JSONObject> search = documentHelper.mySearch(client, s -> s
                        .index("my_suggester")
                        .suggest((su) -> su.suggesters("name_suggester", (se) -> se.completion(co -> co.skipDuplicates(true).field("name").size(10))).text("j"))
                , JSONObject.class);
        Map<String, List<Suggestion<JSONObject>>> suggest = search.suggest();
        List<Suggestion<JSONObject>> name_suggester = suggest.get("name_suggester");
        for (Suggestion<JSONObject> jsonObjectSuggestion : name_suggester) {
            CompletionSuggest<JSONObject> completion = jsonObjectSuggestion.completion();
            for (CompletionSuggestOption<JSONObject> option : completion.options()) {
                String text = option.text();
                System.out.println("text = " + text);
            }
        }
    }

    private BoolQuery.Builder processIkSmart(BoolQuery.Builder queryBuilder, KnowledgePageRequest knowledge) {
        Field[] fields = knowledge.getClass().getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            field.setAccessible(true);
            String o = null;
            try {
                Object o1 = field.get(knowledge);
                if (null == o1) {
                    continue;
                }
                o = String.valueOf(field.get(knowledge));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (!EsSearchHelper.termField.contains(name)) {
                processField_ik_smart(queryBuilder, name, o, EsSearchHelper.fieldBoostMap.get(name), EsSearchHelper.fieldAnalyzerMap.get(name));
            }
        }
        return queryBuilder;
    }


    private BoolQuery.Builder processField_ik_smart(BoolQuery.Builder queryBuilder, String fieldName, String fieldValue, Float boost, String analyzer) {
        if (StringUtils.isEmpty(fieldValue)) {
            return queryBuilder;
        }
        if (null == boost) {
            boost = 1.0f;
        }
        final Float finalBoost = boost;
        return queryBuilder.should(l -> l.match(e -> e.field(fieldName).query(fieldValue).analyzer(analyzer).minimumShouldMatch("2").boost(finalBoost)));
    }


    private TermQuery.Builder processTerm(TermQuery.Builder queryBuilder, KnowledgePageRequest knowledge) {
        Field[] fields = knowledge.getClass().getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            field.setAccessible(true);
            String o = null;
            try {
                Object o1 = field.get(knowledge);
                if (null == o1) {
                    continue;
                }
                o = String.valueOf(field.get(knowledge));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (EsSearchHelper.termField.contains(name)) {
                processField_term(queryBuilder, name, o, EsSearchHelper.fieldBoostMap.get(name), EsSearchHelper.fieldAnalyzerMap.get(name));
            }
        }
        return queryBuilder;
    }

    private TermQuery.Builder processField_term(TermQuery.Builder queryBuilder, String fieldName, String fieldValue, Float boost, String analyzer) {
        if (StringUtils.isEmpty(fieldValue)) {
            return queryBuilder;
        }
        return queryBuilder.field(fieldName).value(fieldValue);
    }


    private void processKnowledge(Knowledge knowledge) {
        System.out.println("knowledge = " + knowledge);
    }


    public CreateResponse commonCreate(MyConsumer<Knowledge> consumer) throws Exception {
        Date date = new Date();
        Knowledge knowledge = new Knowledge();
        knowledge
//                .setType(2)
//                .setCode("00000001")
                .setBelong("wenc")
//                .setKeyword("挪威的森林")
//                .setTitle("挪威的森林")
//                .setDescription("挪威的森林")
//                .setDetail("每个人都有属于自己的一片森林，也许我们 从来不曾去过，但它一直在那里，总会在那里。迷失的人迷失了，相逢的人会再相逢。")
//                .setAuthor("村上村树")
                .setPriority(1)
                .setDeleted(0)
                .setStartTime(DocumentHelper.getVeryStartTime())
                .setEndTime(DocumentHelper.getVeryEndTime())
                .setCreatedBy("wenc")
                .setCreatedTime(date)
                .setUpdatedBy("wenc")
                .setUpdatedTime(date);
        consumer.accept(knowledge);
        return documentHelper.myCreate(client, e -> e.id(knowledge.getCode()).index("test_knowledge").document(knowledge));
    }


}
