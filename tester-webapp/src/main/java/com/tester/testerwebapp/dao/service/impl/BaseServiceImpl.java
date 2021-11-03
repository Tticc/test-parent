package com.tester.testerwebapp.dao.service.impl;

import com.tester.testercommon.dao.BaseDomain;
import com.tester.testercommon.dao.BaseMapper;
import com.tester.testercommon.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository("baseService")
public class BaseServiceImpl<PK extends Serializable, E extends BaseDomain> implements BaseService<PK, E> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseServiceImpl.class);
    private BaseMapper<E, PK> baseMapper;

    public BaseServiceImpl() {
    }

    public void setBaseMapper(BaseMapper<E, PK> baseMapper) {
        this.baseMapper = baseMapper;
    }

    public int save(E entity) {
        return this.baseMapper.save(entity);
    }

    public int delete(PK id) {
        return this.baseMapper.delete(id);
    }

    public int update(E entity) {
        return this.baseMapper.update(entity);
    }

    public E get(PK id) {
        return this.baseMapper.get(id);
    }
}
