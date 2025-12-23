package com.tester.testersearch.service;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.CreateResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import com.tester.base.dto.exception.BusinessException;
import com.tester.base.dto.model.request.PagerInfo;
import com.tester.testercommon.util.BeanCopyUtil;
import com.tester.testersearch.model.Knowledge;
import com.tester.testersearch.model.KnowledgePageRequest;
import com.tester.testersearch.model.KnowledgeRequest;
import com.tester.testersearch.model.KnowledgeResponse;
import com.tester.testersearch.service.helper.DocumentHelper;
import com.tester.testersearch.util.EsSearchHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author 温昌营
 * @Date 2022-10-25 14:18:51
 */
@Slf4j
@Service
public class SearchManager {

    @Autowired
    private DocumentHelper documentHelper;

    @Value("${my.es.specificAnalyser:}")
    private String specificAnalyser;


    public PagerInfo<KnowledgeResponse> search(KnowledgePageRequest request) throws BusinessException {
        try {
            SearchResponse<Knowledge> search = documentHelper.mySearch(s -> s
                    .index("test_knowledge")
//                    .query(q -> q.bool(q1 -> processIkSmart(q1, request))
                    .query(q -> q.bool(f1 -> f1
                                    // 将boost字段纳入对匹配范围内，作为优先级条件。权重定义在：EsSearchHelper.fieldBoostMap
                                    .should(l -> l.multiMatch(e -> e.fields(EsSearchHelper.listAllBoostFields()).type(TextQueryType.BestFields).query(request.getAll())))
                                    .filter(q1 -> q1.bool(q2 -> processIkSmart(q2, request)))
                                    .filter(t -> t.term(t1 -> processTerm(t1, request)))
                            )
//                                        .should(l -> l.multiMatch(e -> e.fields(Arrays.asList("description", "keyword")).query("转眼之间")))
                    )
                    // 先按权重分数排序，权重高在前面
                    .sort(q -> q.field(f -> f.field("_score").order(SortOrder.Desc)))
                    // 先按创建时间逆序
                    .sort(q -> q.field(f -> f.field("createdTime").order(SortOrder.Desc)))
                    .size(request.getPageSize()).from(request.getPageSize() * (request.getPageNum() - 1)), Knowledge.class);
            if(null == search){
                return new PagerInfo<KnowledgeResponse>();
            }
            List<Knowledge> collect = search.hits().hits().stream().map(e -> e.source()).collect(Collectors.toList());
            int sort = request.getPageSize() * (request.getPageNum() - 1);
            List<KnowledgeResponse> resList = new ArrayList<>(collect.size());
            for (Knowledge knowledge : collect) {
                KnowledgeResponse knowledgeResponse = new KnowledgeResponse();
//                CommonUtil.copyPropertiesIgnoreNull(knowledge, knowledgeResponse);
                BeanUtils.copyProperties(knowledge, knowledgeResponse);
                knowledgeResponse.setSort(getSortStr(++sort));
                resList.add(knowledgeResponse);
            }
            PagerInfo pagerInfo = new PagerInfo<KnowledgeResponse>();
            pagerInfo.setTotal(search.hits().total().value());
            pagerInfo.setPageNum(request.getPageNum());
            pagerInfo.setPageSize(request.getPageSize());
            pagerInfo.setHasNextPage(pagerInfo.getTotal() > request.getPageSize() * request.getPageNum());
            pagerInfo.setList(resList);
            return pagerInfo;
        } catch (Exception e) {
            log.error("查询异常。err:",e);
            throw new BusinessException(5000, e);
        }
    }

    private String getSortStr(int sort) {
        if (sort < 10) {
            return "00" + sort;
        } else if (sort < 100) {
            return "0" + sort;
        } else {
            return "" + sort;
        }
    }

    public String add(KnowledgeRequest model) throws BusinessException {
        if (StringUtils.isEmpty(model.getCode())) {
            model.setCode(UUID.randomUUID().toString().replace("-", ""));
        }
        if (StringUtils.isEmpty(model.getAuthor())) {
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
            log.error("新增异常。err:",e);
            throw new BusinessException(5000, e);
        }
    }

    public String update(KnowledgeRequest model) throws BusinessException {
        if (StringUtils.isEmpty(model.getCode())) {
            throw new BusinessException(5000L, "没有编码");
        }
        try {
            UpdateResponse createResponse = documentHelper.commonUpdate((e) -> {
                BeanCopyUtil.copyPropertiesIgnoreNull(model, e);
            });
            return createResponse.id();
        } catch (Exception e) {
            log.error("修改异常。err:",e);
            throw new BusinessException(5000, e);
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
                processField_ik_smart(queryBuilder, name, o, EsSearchHelper.fieldBoostMap.get(name), StringUtils.isEmpty(specificAnalyser) ? EsSearchHelper.fieldAnalyzerMap.get(name) : specificAnalyser);
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
                processField_term(queryBuilder, name, o, EsSearchHelper.fieldBoostMap.get(name), StringUtils.isEmpty(specificAnalyser) ? EsSearchHelper.fieldAnalyzerMap.get(name) : specificAnalyser);
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
}
