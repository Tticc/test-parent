package com.tester.testermybatis.annotation;

import java.lang.annotation.*;

/**
 * 需要解密的字段注解。
 * <br/>例如手机号，在字段上加上这个注解。前提：类上已加上注解：DecryptDomain
 * <br/>解析位置：com.tester.testermybatis.interceptor.ParamDecryptInterceptor
 * @Date 14:24 2020/8/21
 * @Author 温昌营
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DecryptField {
}