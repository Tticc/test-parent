package com.tester.testerwebapp.dao.service;

import com.tester.testercommon.constant.ConstantList;
import com.tester.testerwebapp.dao.domain.UserDomain;
import com.tester.testerwebapp.dao.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
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
        boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization(){
            @Override
            public void afterCommit(){
                System.out.println("after commit");
            }
        });

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
