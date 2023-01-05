package com.tester.testersearch.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.CreateResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.tester.base.dto.exception.BusinessException;
import com.tester.base.dto.model.request.PagerInfo;
import com.tester.testersearch.model.Knowledge;
import com.tester.testersearch.model.KnowledgePageRequest;
import com.tester.testersearch.model.KnowledgeRequest;
import com.tester.testersearch.service.helper.DocumentHelper;
import com.tester.testersearch.util.EsSearchHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author 温昌营
 * @Date 2022-10-25 14:18:51
 */
@Service
public class SearchManager {

    @Autowired
    private DocumentHelper documentHelper;


    public PagerInfo<Knowledge> search(KnowledgePageRequest request) throws BusinessException {
        try {
            SearchResponse<Knowledge> search = documentHelper.mySearch(s -> s
                    .index("test_knowledge")
                    .query(q -> q.bool(q1 -> baseProcess(q1, request))
//                                        .should(l -> l.multiMatch(e -> e.fields(Arrays.asList("description", "keyword")).query("转眼之间")))
                    ).size(request.getPageSize()).from(request.getPageSize()*(request.getPageNum()-1)), Knowledge.class);
            List<Knowledge> collect = search.hits().hits().stream().map(e -> e.source()).collect(Collectors.toList());
            PagerInfo pagerInfo = new PagerInfo<Knowledge>();
            pagerInfo.setTotal(search.hits().total().value());
            pagerInfo.setPageNum(request.getPageNum());
            pagerInfo.setPageSize(request.getPageSize());
            pagerInfo.setHasNextPage(pagerInfo.getTotal() > request.getPageSize()*request.getPageNum());
            pagerInfo.setList(collect);
            return pagerInfo;
        } catch (Exception e) {
            throw new BusinessException(5000, e);
        }
    }

    public String add(KnowledgeRequest model) throws BusinessException {
        if(StringUtils.isEmpty(model.getCode())){
            model.setCode(UUID.randomUUID().toString().replace("-",""));
        }
        if(StringUtils.isEmpty(model.getAuthor())){
            model.setAuthor("wenc");
        }
        try {
            CreateResponse createResponse = documentHelper.commonCreate((e) -> {
                e.setType(model.getType())
                        .setCode(model.getCode())
                        .setKeyword(model.getKeyword())
                        .setTitle(model.getTitle())
                        .setDescription(model.getDescription())
                        .setDetail(model.getDetail())
                        .setAuthor(model.getAuthor());
            });
            return createResponse.id();
        } catch (Exception e) {
            throw new BusinessException(5000, e);
        }
    }

    private BoolQuery.Builder baseProcess(BoolQuery.Builder queryBuilder, KnowledgePageRequest knowledge) {
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


    private BoolQuery.Builder processField_ik_smart(BoolQuery.Builder queryBuilder, String fieldName, String fieldValue, Float boost, String analyzer) {
        if (StringUtils.isEmpty(fieldValue)) {
            return queryBuilder;
        }
        if (null == boost) {
            boost = 1.0f;
        }
        final Float finalBoost = boost;
        return queryBuilder.should(l -> l.match(e -> e.field(fieldName).query(fieldValue).analyzer("ik_smart").minimumShouldMatch("2").boost(finalBoost)));
    }
}
