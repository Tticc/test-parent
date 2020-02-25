package com.tester.testercommon.dao.mapper;

import com.tester.testercommon.dao.domain.BaseDomain;
import org.mybatis.spring.annotation.MapperScan;

import java.io.Serializable;


public interface BaseMapper <E extends BaseDomain, K extends Serializable> extends tk.mybatis.mapper.common.BaseMapper<E>{
}