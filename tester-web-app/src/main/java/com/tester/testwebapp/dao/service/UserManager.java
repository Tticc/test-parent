package com.tester.testwebapp.dao.service;

import com.tester.testwebapp.dao.domain.UserDomain;
import com.tester.testwebapp.dao.mapper.UserMapper;
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
        return Mono.just(userMapper.selectUserById(id));
    }
}
