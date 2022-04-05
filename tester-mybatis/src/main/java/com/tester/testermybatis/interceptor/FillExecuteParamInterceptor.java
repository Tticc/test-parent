package com.tester.testermybatis.interceptor;

import com.tester.base.dto.dao.BaseDomain;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * sql参数填充拦截
 * @Author 温昌营
 * @Date 2020-8-24 11:08:39
 */
@Intercepts(@Signature(
        type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class}
))
@Slf4j
public class FillExecuteParamInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object parameter = invocation.getArgs()[1];
        MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        this.setAttr(parameter, sqlCommandType);
        return invocation.proceed();
    }

    private void setAttr(Object parameter, SqlCommandType sqlCommandType) {
        if(parameter instanceof BaseDomain){
            BaseDomain baseDomain = (BaseDomain)parameter;
            this.commonSetAttr(baseDomain, sqlCommandType);
        }else if(parameter instanceof Map){
            Map parameterMap = (Map)parameter;
            Set<Map.Entry> parameterMapEntrySet = parameterMap.entrySet();
            Iterator<Map.Entry> parameterMapEntryIt = parameterMapEntrySet.iterator();
            Map.Entry parameterMapEntry = null;
            while(parameterMapEntryIt.hasNext()) {
                parameterMapEntry = (Map.Entry)parameterMapEntryIt.next();
                this.setAttr(parameterMapEntry.getValue(), sqlCommandType);
            }

        }
    }

    private void commonSetAttr(BaseDomain baseDomain, SqlCommandType sqlCommandType) {
        Date sysDate = new Date();
        if (baseDomain.getUpdateTime() == null) {
            baseDomain.setUpdateTime(sysDate);
        }
        if (sqlCommandType == SqlCommandType.INSERT) {
            if (baseDomain.getCreateTime() == null) {
                baseDomain.setCreateTime(sysDate);
            }
        }
    }
}
