package com.tester.testerrpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 这个注解应该放到ServiceImpl class上面。<br/>
 * 放到class上面代表这个class是一个RpcService class，里面所有的public 方法都会是rpcService。<br/>
 * rpcService方法必须要放在被这个注解注释的class里面，否则无法被扫描到。
 */
@Retention(RUNTIME)
//@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Target(value = {ElementType.TYPE})
@Inherited
public @interface RpcServiceMe {

}
