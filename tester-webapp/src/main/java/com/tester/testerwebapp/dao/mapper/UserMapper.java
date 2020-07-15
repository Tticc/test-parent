package com.tester.testerwebapp.dao.mapper;

import com.tester.testercommon.dao.mapper.BaseMapper;
import com.tester.testerwebapp.dao.domain.UserDomain;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author 温昌营
 * @Date
 */
public interface UserMapper extends BaseMapper<UserDomain, Long> {

    @Select("select * from u_person where id = #{id} and deleted = 0")
    UserDomain selectUserById(@Param("id") Long id);

    // todo 未完成
    int batchUpdate(@Param("list") List<UserDomain> list);

}
