package com.tester.testerwebapp.dao.service;

import com.tester.testerwebapp.dao.domain.UserDomain;
import com.tester.testerwebapp.dao.mapper.UserMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @Author 温昌营
 * @Date
 */
@Service("userManager")
public class UserManager {
    @Resource
    private UserMapper userMapper;

    public Mono<UserDomain> selectUserById(Long id){
        return Mono.justOrEmpty(userMapper.selectUserById(id));
    }

    public Mono<Integer> insert(){
        UserDomain userDomain = new UserDomain();
        userDomain.setName("wenc").setCellphone("123498734892").setDataFrom(1).setEmployeeId("0001").setWechatid("1232");
        return Mono.justOrEmpty(userMapper.insert(userDomain));
    }

    public Mono<Integer> update(){
        return Mono.justOrEmpty(userMapper.updateByPrimaryKey(null));
    }
}
