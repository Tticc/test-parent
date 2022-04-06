package com.tester.base.dto.dao;

import java.io.Serializable;


public interface BaseMapper <E extends BaseDomain, PK extends Serializable> {
    int save(E domain);

    int delete(PK id);

    int update(E domain);

    E get(PK id);
}
