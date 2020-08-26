package com.tester.testerwebapp.dao.service;

import com.tester.testermybatis.constant.ConstantList;
import com.tester.testerwebapp.dao.domain.UserDomain;
import com.tester.testerwebapp.dao.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(rollbackFor = Exception.class, transactionManager = ConstantList.NORMAL_MANAGER)
    public Mono<UserDomain> selectUserById(Long id){
        return Mono.justOrEmpty(userMapper.selectUserById(id));
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = ConstantList.NORMAL_MANAGER)
    public Mono<Integer> insert(){
        UserDomain userDomain = new UserDomain().init();
        userDomain.setName("wenc").setCellphone("123498734892").setDataFrom(1).setEmployeeId("0001").setWechatid("1232");
        return Mono.justOrEmpty(userMapper.insert(userDomain));
    }

    public Mono<Integer> update(){
        return Mono.justOrEmpty(userMapper.updateByPrimaryKey(null));
    }
}
