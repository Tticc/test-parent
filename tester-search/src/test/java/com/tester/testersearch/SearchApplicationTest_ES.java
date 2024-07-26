package com.tester.testersearch;

import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.CreateResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggest;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.Suggestion;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tester.testercommon.util.MyConsumer;
import com.tester.testercommon.util.file.TxtWrite;
import com.tester.testersearch.model.Knowledge;
import com.tester.testersearch.service.helper.DocumentHelper;
import com.tester.testersearch.service.helper.IndexHelper;
import com.tester.testersearch.util.EsSearchHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TesterSearchApplication.class)
@Slf4j
public class SearchApplicationTest_ES {

    @Autowired
    private DocumentHelper documentHelper;
    @Autowired
    private IndexHelper indexHelper;

    @Test
    public void test_createIndex() throws IOException {
        indexHelper.test_create_index("test_knowledge");
    }

    @Test
    public void import_from_json() throws IOException {
        String s = TxtWrite.file2String("D:\\desktop\\test_knowledge_new.json");
        String[] split = s.split("\n");
        for (String s1 : split) {
            if(s1.startsWith("{\"index")){
                continue;
            }
            Knowledge knowledge = JSON.parseObject(s1, Knowledge.class);
//            documentHelper.myCreate(e -> e.id(knowledge.getCode()).index("test_knowledge").document(knowledge));
        }
    }

    @Test
    public void test_update() throws Exception {
        Knowledge knowledge = new Knowledge();
        knowledge.setCode("00000002").setTitle("镖车运输时间点_2022-8-23 10:23:36").setUpdatedTime(new Date());
        documentHelper.myUpdate(e -> e.id("78266cd9834c4b2e96e1f0974afb5fcf").index("test_knowledge").doc(knowledge), Knowledge.class);
    }

    @Test
    public void test_data_init() throws Exception {
        documentHelper.test_data_init();
    }

    @Test
    public void test_firstRequest() throws Exception {
        String key = null;
        Knowledge kn = new Knowledge();
        kn.setType(2);
        kn.setTitle("黑暗");
//        SearchResponse<Knowledge> search = documentHelper.mySearch(s -> s
//                        .index("test_knowledge")
//                        .query(q -> q
//                                .bool(q1 -> q1.should(l -> l.match(e -> e.field("detail").query("森林").analyzer("ik_smart").minimumShouldMatch("2").boost(1f)))
//                                        .should(l -> l.term(e -> e.field("keyword").value("欧莎").boost(99f)))
////                                        .should(l -> l.multiMatch(e -> e.fields(Arrays.asList("description", "keyword")).query("转眼之间")))
//                                )).size(10).from(0),
//        Knowledge.class);
        SearchResponse<Knowledge> search = documentHelper.mySearch(s -> s
                .index("test_knowledge")
                .query(q -> q.bool(q1 -> baseProcess(q1, kn))
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
        SearchResponse<Knowledge> search = documentHelper.mySearch(s -> s
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
        SearchResponse<JSONObject> search = documentHelper.mySearch(s -> s
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

    public BoolQuery.Builder baseProcess(BoolQuery.Builder queryBuilder, Knowledge knowledge) {
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
            processField_ik_smart(queryBuilder, name, o, EsSearchHelper.fieldBoostMap.get(name), EsSearchHelper.fieldAnalyzerMap.get(name));
        }
        return queryBuilder;
    }


    public BoolQuery.Builder processField_ik_smart(BoolQuery.Builder queryBuilder, String fieldName, String fieldValue, Float boost, String analyzer) {
        if (StringUtils.isEmpty(fieldValue)) {
            return queryBuilder;
        }
        if (null == boost) {
            boost = 1.0f;
        }
        final Float finalBoost = boost;
        return queryBuilder.should(l -> l.match(e -> e.field(fieldName).query(fieldValue).analyzer("ik_smart").minimumShouldMatch("2").boost(finalBoost)));
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
        return documentHelper.myCreate(e -> e.id(knowledge.getCode()).index("test_knowledge").document(knowledge));
    }


}
