package com.tester.testermybatis.interceptor;

import com.tester.testercommon.constant.ConstantList;
import com.tester.testercommon.util.endecrypt.AesSecurityHex;
import com.tester.testermybatis.annotation.DecryptDomain;
import com.tester.testermybatis.annotation.DecryptField;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.Properties;

/**
 * 用于查询中, 对某些特殊字段进行入参加密
 */
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
)})
@Slf4j
public class ParamEncryptInterceptor implements Interceptor {
    /**
     * 启用开关. 1.开启 0.关闭
     */
    private boolean enable;


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if(!enable){
            return invocation.proceed();
        }

        Object target = invocation.getTarget();
        // 判断target类型
        if (target instanceof Executor) {
            // 为 Executor 则进行加密
            return doEncrypt(invocation);
        }

        return invocation.proceed();
    }

    /**
     * 兼容shardingjdbc结果归并 返回映射类型与解密反射类型不一致
     * @param object
     * @return
     */
    private Object paramHandle(Object object) {
        if (Objects.isNull(object)) {
            return object;
        }
        Class className = object.getClass();
        if (className.equals(Byte.class)) {
            return (byte) object & 0xff;
        } else if (className.equals(Short.class)) {
            return Short.toUnsignedInt((Short) object);
        }
        return object;
    }

    private Object doEncrypt(Invocation invocation) throws Throwable {
        Object parameter = invocation.getArgs()[1];
        if (parameter == null) {
            return invocation.proceed();
        }

        Class<?> clazz = parameter.getClass();

        // 判断是否存在加密注解
        DecryptDomain domainAnnotation = clazz.getAnnotation(DecryptDomain.class);
        if (domainAnnotation == null) {
            return invocation.proceed();
        }

        // 迭代成员属性
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if(field.getModifiers() != Modifier.PRIVATE){
                continue;
            }
            try {
                // 成员属性值为空, 则continue
                Method getter = ParamDecryptInterceptor.buildGetterMethod(clazz, field.getName());
                Object fieldValue = getter.invoke(parameter);
                if (!Objects.nonNull(fieldValue)) {
                    continue;
                }
                // 不存在加密注解的字段,continue
                DecryptField fieldAnnotation = field.getAnnotation(DecryptField.class);
                if (fieldAnnotation == null) {
                    continue;
                }
                // 执行字段加密
                Method setter = ParamDecryptInterceptor.buildSetterMethod(clazz, field.getName(), field.getType());
                setter.invoke(parameter, AesSecurityHex.encrypt(fieldValue.toString()));
            } catch (Exception e) {
                log.error("字段加密异常", e);
                throw e;
            }
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }


    /**
     * 设置开关
     * @param properties
     * @return void
     * @Date 11:22 2020/8/24
     * @Author 温昌营
     **/
    @Override
    public void setProperties(Properties properties) {
        enable = Objects.equals(Integer.valueOf(properties.getProperty("enable", "1")), ConstantList.ONE);
    }

}
