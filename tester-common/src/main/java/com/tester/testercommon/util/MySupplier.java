/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package com.tester.testercommon.util;


import com.tester.testercommon.exception.BusinessException;

/**
 * 比自带的Supplier多了一个异常抛出声明
 * @Date 2020-6-23
 * @Author 温昌营
 **/
@FunctionalInterface
public interface MySupplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get() throws BusinessException;
}
