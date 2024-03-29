package com.tester.testerwebapp.dao.service;

import com.tester.base.dto.model.request.IdAndNameRequest;
import com.tester.base.dto.dao.BaseService;
import com.tester.testerwebapp.dao.domain.UserDomain;

import java.util.List;
import java.util.Set;

/**
 * @Author 温昌营
 * @Date 2021-3-8 11:07:58
 */
public interface UserService extends BaseService<Long, UserDomain> {

    UserDomain selectUserId(Long id);

    List<UserDomain> listByIds(Set<Long> ids);

    List<UserDomain> listByName(IdAndNameRequest request);

    int batchSaveUser(List<UserDomain> list);
}
