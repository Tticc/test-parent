package com.tester.testermybatis.interceptor;

import com.tester.testercommon.enums.YesNoEnum;
import com.tester.testercommon.util.endecrypt.AesSecurityHex;
import com.tester.testermybatis.annotation.DecryptDomain;
import com.tester.testermybatis.annotation.DecryptField;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.*;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.*;

/**
 * @Author 温昌营
 * @Date 2020-8-21 14:05:43
 */

@Intercepts({
        @Signature(
                type = ResultSetHandler.class,
                method ="handleResultSets",
                args = Statement.class
        ),
        @Signature(
                type = Executor.class,
                method = "update",
                args ={MappedStatement.class,Object.class}
        )
})
@Slf4j
public class ParamDecryptInterceptor implements Interceptor {

    private static final String SET = "set";

    private static final String GET = "get";

    private static final String UNDERLINE = "_";
    /**
     * 启用开关. 1.开启 0.关闭
     */
    private boolean enable;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 不启动加密直接返回
        if(!enable){
            return invocation.proceed();
        }
        Object target = invocation.getTarget();

        // 判断target类型
        if(target instanceof ResultSetHandler){
            // 若为 ResultSetHandler 则进行参数解密
            return doDecrypt(invocation);
        } else if(target instanceof Executor){
            // 为 Executor 则进行加密
            return doEncrypt(invocation);
        }
        return invocation.proceed();
    }

    private Object doEncrypt(Invocation invocation) throws Throwable {
        Object parameter = invocation.getArgs()[1];
        if (parameter == null) {
            return invocation.proceed();
        }
        Class<?> clazz = parameter.getClass();
        // 判断是否存在加密注解
        DecryptDomain decryptDomain = clazz.getAnnotation(DecryptDomain.class);
        if(decryptDomain == null){
            return invocation.proceed();
        }

        // 迭代成员属性
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {

                Method getter = buildGetterMethod(clazz, field.getName());
                Object fieldValue = getter.invoke(parameter);
                if (!Objects.nonNull(fieldValue)) {
                    continue;
                }
                // 不存在加密注解的字段,continue
                DecryptField annotation = field.getAnnotation(DecryptField.class);
                if (null == annotation) {
                    continue;
                }
                // 执行字段加密
                Method setter = buildSetterMethod(clazz, field.getName(), field.getType());
                setter.invoke(parameter, AesSecurityHex.encrypt(fieldValue.toString()));
            }catch (Exception e){
                log.error("字段加密异常",e);
                throw e;
            }
        }
        return invocation.proceed();
    }

    private Object doDecrypt(Invocation invocation) throws Throwable {
        // 获取默认结果集处理器
        DefaultResultSetHandler defaultResultSetHandler = (DefaultResultSetHandler) invocation.getTarget();
        Field[] declaredFields = defaultResultSetHandler.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (!"mappedStatement".equals(declaredField.getName())) {
                continue;
            }
            declaredField.setAccessible(true);
            MappedStatement mappedStatement;
            try {
                // 获取mappedStatement
                mappedStatement = (MappedStatement) declaredField.get(defaultResultSetHandler);
                // 获取节点属性的集合
                List<ResultMap> resultMaps = mappedStatement.getResultMaps();
                if (CollectionUtils.isEmpty(resultMaps)) {
                    continue;
                }

                // 获取当前resultType的类型
                Class<?> resultType = resultMaps.get(0).getType();
                // 判断是否存在解密注解
                DecryptDomain decryptDomain = resultType.getAnnotation(DecryptDomain.class);
                if (null == decryptDomain) {
                    return invocation.proceed();
                }
                // 返回集合
                List<Object> resultList = new ArrayList<>();
                Statement statement = (Statement) invocation.getArgs()[0];
                try(ResultSet resultSet = statement.getResultSet()) {
                    if (null == resultSet) {
                        return resultList;
                    }
                    // 获得对应列名
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    // 列名集合
                    List<String> columnList = new ArrayList<>();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        columnList.add(metaData.getColumnName(i));
                    }
                    while (resultSet.next()) {
                        Object instance = resultType.newInstance();
                        for (String col : columnList) {
                            Class<?> tempResultType = resultType;
                            // 获取列名字段驼峰式命名
                            String fieldName = camelFormat(col);
                            // 获取属性值
                            Object obj = resultSet.getObject(col);
                            // 判断是否存在解密注解
                            Field field;
                            try {
                                try {
                                    field = tempResultType.getDeclaredField(fieldName);
                                } catch (NoSuchFieldException nfe) {
                                    // 继续查询baseDomain内容
                                    tempResultType = tempResultType.getSuperclass();
                                    field = tempResultType.getDeclaredField(fieldName);
                                }
                            } catch (Exception e) {
                                log.warn("参数解密异常，无此字段【{}】", fieldName);
                                continue;
                            }
                            DecryptField decryptField = field.getAnnotation(DecryptField.class);
                            // setter方法
                            Method setter = buildSetterMethod(tempResultType, fieldName, field.getType());
                            if (decryptField != null && obj != null) {
                                // 解密处理
                                setter.invoke(instance, AesSecurityHex.decrypt(obj.toString()));
                            } else {
                                setter.invoke(instance, paramHandle(obj));
                            }
                        }
                        resultList.add(instance);
                    }
                }catch (Exception e){
                    throw e;
                }
                return resultList;
            } catch (Exception e) {
                log.error("参数解密异常", e);
                throw e;
            }
        }
        return invocation.proceed();
    }

    public static String camelFormat(String targetStr) {
        String[] strArray = targetStr.split(UNDERLINE);
        return firstCharOnlyToLower(Arrays.stream(strArray).map(str -> firstCharOnlyToUpper(str)).reduce("", (x, y) -> x + y));
    }
    public static Method buildSetterMethod(Class<?> clazz, String fieldName, Class<?> filedType) throws NoSuchMethodException {
        return clazz.getDeclaredMethod(SET + firstCharOnlyToUpper(fieldName), filedType);
    }
    public static Method buildGetterMethod(Class<?> clazz, String fieldName) throws NoSuchMethodException {
        return clazz.getDeclaredMethod(GET + firstCharOnlyToUpper(fieldName));
    }
    private static String firstCharOnlyToLower(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    private static String firstCharOnlyToUpper(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
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
        if (className.equals(java.lang.Byte.class)) {
            return (byte) object & 0xff;
        } else if (className.equals(java.lang.Short.class)) {
            return Short.toUnsignedInt((Short) object);
        }
        return object;
    }



//    @Override
//    public Object plugin(Object target) {
//        return Plugin.wrap(target, this);
//    }

    /**
     * 设置开关
     * @param properties
     * @return void
     * @Date 11:22 2020/8/24
     * @Author 温昌营
     **/
    @Override
    public void setProperties(Properties properties) {
        enable = Boolean.valueOf(properties.getProperty("enable", "true"));
    }
}
