package com.tester.testerwebapp.dao.mapper;

import com.tester.testercommon.dao.BaseMapper;
import com.tester.testercommon.model.request.IdAndNameModel;
import com.tester.testerwebapp.dao.domain.UserDomain;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author 温昌营
 * @Date
 */
public interface UserMapper extends BaseMapper<UserDomain, Long> {

    UserDomain selectUserId(@Param("id") Long id);

    List<UserDomain> listByName(IdAndNameModel request);

    // todo 未完成
    int batchUpdate(@Param("list") List<UserDomain> list);

}
