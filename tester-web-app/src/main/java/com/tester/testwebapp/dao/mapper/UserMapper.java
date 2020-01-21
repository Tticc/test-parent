package com.tester.testwebapp.dao.mapper;

import com.tester.testercommon.dao.mapper.BaseMapper;
import com.tester.testwebapp.dao.domain.UserDomain;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Author 温昌营
 * @Date
 */
public interface UserMapper extends BaseMapper<UserDomain, Long> {

    @Select("select * from u_person where id = #{id} and deleted = 0")
    UserDomain selectOne(@Param("id") Long id);

}
