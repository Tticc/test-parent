package com.tester.testerwebapp.service;

import com.tester.testercommon.constant.ConstantList;
import com.tester.testercommon.model.request.IdAndNameModel;
import com.tester.testerwebapp.dao.domain.UserDomain;
import com.tester.testerwebapp.dao.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author 温昌营
 * @Date
 */
@Service("userManager")
public class UserManager {
    @Resource
    private UserService userService;



    @Transactional(rollbackFor = Exception.class, transactionManager = ConstantList.NORMAL_MANAGER)
    public Mono<UserDomain> selectUserById(Long id){
        boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization(){
            @Override
            public void afterCommit(){
                System.out.println("after commit");
            }
        });

        return Mono.justOrEmpty(userService.selectUserId(id));
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = ConstantList.NORMAL_MANAGER)
    public Mono<List<UserDomain>> listByName(IdAndNameModel request){
        boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization(){
            @Override
            public void afterCommit(){
                System.out.println("after commit");
            }
        });

        return Mono.justOrEmpty(userService.listByName(request));
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = ConstantList.NORMAL_MANAGER)
    public Mono<Integer> insert(){
        UserDomain userDomain = new UserDomain().init();
        userDomain.setName("wenc").setCellphone("123498734892").setDataFrom(1).setEmployeeId("0001").setWechatid("1232");
        return Mono.justOrEmpty(userService.save(userDomain));
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = ConstantList.NORMAL_MANAGER)
    public void insert_Test(UserDomain userDomain){
        userService.save(userDomain);
    }

    public Mono<Integer> update(){
        UserDomain userDomain = new UserDomain();
        userDomain.setId(1L);
        userDomain.setNote("update 11");
        return Mono.justOrEmpty(userService.update(userDomain));
    }
}
