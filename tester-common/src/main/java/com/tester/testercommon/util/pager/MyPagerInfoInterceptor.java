package com.tester.testercommon.util.pager;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.cache.Cache;
import com.github.pagehelper.cache.CacheFactory;
import com.github.pagehelper.dialect.AbstractHelperDialect;
import com.github.pagehelper.page.PageAutoDialect;
import com.github.pagehelper.util.MSUtils;
import com.tester.base.dto.model.request.PagerInfo;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;

@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
)})
public class MyPagerInfoInterceptor implements Interceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyPagerInfoInterceptor.class);
    protected Cache<String, MappedStatement> msCountMap = null;
    private PageAutoDialect autoDialect;
    private String countSuffix = "_COUNT";
    private static final List<ResultMapping> EMPTY_RESULT_MAPPING = new ArrayList(0);
    private Field additionalParametersField;


    public Object intercept(Invocation invocation) throws Throwable {
        AbstractHelperDialect dialect = null;

        Object var13;
        try {
            Object[] args = invocation.getArgs();
            MappedStatement ms = (MappedStatement) args[0];
            Object parameter = args[1];
            RowBounds rowBounds = (RowBounds) args[2];
            ResultHandler resultHandler = (ResultHandler) args[3];
            Executor executor = (Executor) invocation.getTarget();
            List resultList = null;
            CacheKey cacheKey;
            BoundSql boundSql;
            if (args.length == 4) {
                boundSql = ms.getBoundSql(parameter);
                cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
            } else {
                cacheKey = (CacheKey) args[4];
                boundSql = (BoundSql) args[5];
            }

            PagerInfo pager = this.getPagerInfo();
            if (pager != null) {
                this.autoDialect.initDelegateDialect(ms);
                dialect = this.autoDialect.getDelegate();
                PageHelper.startPage(pager.getPageNum(), pager.getPageSize());
                Map<String, Object> additionalParameters = (Map) this.additionalParametersField.get(boundSql);
                Long count = 0L;
                if (pager.getTotal() > 0L) {
                    count = pager.getTotal();
                } else {
                    count = this.getCount(pager, boundSql, executor, ms, parameter, additionalParameters, resultHandler, rowBounds, cacheKey);
                }

                if (count > 0L) {
                    pager.setTotal((long) count.intValue());
                    parameter = dialect.processParameterObject(ms, parameter, boundSql, cacheKey);
                    String pageSql = dialect.getPageSql(ms, boundSql, parameter, rowBounds, cacheKey);
                    BoundSql pageBound = new BoundSql(ms.getConfiguration(), pageSql, boundSql.getParameterMappings(), parameter);
                    Iterator var18 = additionalParameters.keySet().iterator();

                    while (var18.hasNext()) {
                        String key = (String) var18.next();
                        pageBound.setAdditionalParameter(key, additionalParameters.get(key));
                    }

                    resultList = executor.query(ms, parameter, RowBounds.DEFAULT, resultHandler, cacheKey, pageBound);
                } else {
                    pager.setTotal(0L);
                    resultList = new ArrayList(0);
                }

                pager.setList((List) resultList);
                Object var15 = resultList;
                return var15;
            }

            var13 = invocation.proceed();
        } finally {
            PagerHelper.clearPage();
            if (dialect != null) {
                dialect.afterAll();
            }

        }

        return var13;
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
        this.msCountMap = CacheFactory.createCache(properties.getProperty("msCountCache"), "ms", properties);
        this.autoDialect = new PageAutoDialect();

        try {
            this.additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
            this.additionalParametersField.setAccessible(true);
        } catch (NoSuchFieldException var3) {
            LOGGER.error("加载additionalParameters属性反射异常", var3);
        }

    }

    private PagerInfo getPagerInfo() {
        return PagerHelper.getLocalPage();
    }

    private Long getCount(PagerInfo pagerInfo, BoundSql boundSql, Executor executor, MappedStatement ms, Object parameter, Map<String, Object> additionalParameters, ResultHandler resultHandler, RowBounds rowBounds, CacheKey cacheKey) throws SQLException {
        MappedStatement countMs = null;
        BoundSql countBoundSql = null;
        String countMapperId = pagerInfo.getTotalMapperId();
        String key;
        if (countMapperId != null && !countMapperId.equals("")) {
            try {
                countMs = ms.getConfiguration().getMappedStatement(countMapperId, false);
            } catch (Throwable var16) {
                LOGGER.error(countMapperId + " MappedStatement不存在;", var16);
                throw new SQLException(countMapperId + "MappedStatement不存在;", var16);
            }

            countBoundSql = countMs.getBoundSql(parameter);
        } else {
            String countMsId = ms.getId() + this.countSuffix;
            countMs = (MappedStatement) this.msCountMap.get(countMsId);
            if (countMs == null) {
                countMs = MSUtils.newCountMappedStatement(ms, countMsId);
                this.msCountMap.put(countMsId, countMs);
            }

            key = this.autoDialect.getDelegate().getCountSql(countMs, boundSql, parameter, rowBounds, cacheKey);
            countBoundSql = new BoundSql(countMs.getConfiguration(), key, boundSql.getParameterMappings(), parameter);
        }

        Iterator var17 = additionalParameters.keySet().iterator();

        while (var17.hasNext()) {
            key = (String) var17.next();
            countBoundSql.setAdditionalParameter(key, additionalParameters.get(key));
        }

        CacheKey countKey = executor.createCacheKey(countMs, parameter, RowBounds.DEFAULT, boundSql);
        Object countResultList = executor.query(countMs, parameter, RowBounds.DEFAULT, resultHandler, countKey, countBoundSql);
        Long count = (Long) ((List) countResultList).get(0);
        return count;
    }
}


