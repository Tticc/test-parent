package com.tester.testercommon.dao;

import tk.mybatis.mapper.common.special.InsertListMapper;

import java.io.Serializable;


public interface BaseMapper <E extends BaseDomain, K extends Serializable> extends tk.mybatis.mapper.common.BaseMapper<E> , InsertListMapper<E> {
}
