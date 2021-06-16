package com.tester.testerwebapp.dao.service;

import com.tester.testercommon.model.request.IdAndNameRequest;
import com.tester.testercommon.service.BaseService;
import com.tester.testerwebapp.dao.domain.UserDomain;

import java.util.List;

/**
 * @Author 温昌营
 * @Date 2021-3-8 11:07:58
 */
public interface UserService extends BaseService<Long, UserDomain> {

    UserDomain selectUserId(Long id);

    List<UserDomain> listByName(IdAndNameRequest request);

}
