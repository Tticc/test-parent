package com.tester.testerwebapp.dao.mapper;

import com.tester.testercommon.dao.BaseMapper;
import com.tester.testercommon.model.request.IdAndNameRequest;
import com.tester.testerwebapp.dao.domain.UserDomain;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Author 温昌营
 * @Date
 */
public interface UserMapper extends BaseMapper<UserDomain, Long> {

    UserDomain selectUserId(@Param("id") Long id);

    List<UserDomain> listByName(IdAndNameRequest request);

    List<UserDomain> listByName2(IdAndNameRequest request);

    List<UserDomain> listByIds(@Param("ids") Set<Long> ids);

    int batchSaveUser(List<UserDomain> list);
}
