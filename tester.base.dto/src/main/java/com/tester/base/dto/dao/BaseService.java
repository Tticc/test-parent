package com.tester.base.dto.dao;

import com.tester.base.dto.dao.BaseDomain;
import java.io.Serializable;

public interface BaseService<PK extends Serializable, E extends BaseDomain> {
    int save(E var1);

    int delete(PK var1);

    int update(E var1);

    E get(PK var1);
}