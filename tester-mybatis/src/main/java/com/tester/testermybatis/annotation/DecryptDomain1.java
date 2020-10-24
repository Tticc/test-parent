package com.tester.testermybatis.annotation;

import com.tester.testermybatis.config.MyImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 解密domain注解<br/>
 * 如果domain中有需要加解密的字段，如phone，加上此注解。
 * <br/>解析位置：com.tester.testermybatis.interceptor.ParamDecryptInterceptor
 * @Date 14:25 2020/8/21
 * @Author 温昌营
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(MyImportSelector.class)
public @interface DecryptDomain1 {
}