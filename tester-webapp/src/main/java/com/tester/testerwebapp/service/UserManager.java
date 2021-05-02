package com.tester.testerwebapp.service;

import com.tester.testercommon.constant.ConstantList;
import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.model.request.IdAndNameRequest;
import com.tester.testerwebapp.dao.domain.UserDomain;
import com.tester.testerwebapp.dao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
@Slf4j
@Service("userManager")
public class UserManager {
    @Resource
    private UserService userService;
    @Resource
    private UserManager userManager;



    @Transactional(rollbackFor = Exception.class, transactionManager = ConstantList.NORMAL_MANAGER,propagation = Propagation.REQUIRED)
    public Mono<UserDomain> selectUserById_required(Long id) throws BusinessException {
        UserDomain userDomain = new UserDomain().init();
        userDomain.setName("20210418_2").setCellphone("123498734892").setDataFrom(1).setEmployeeId("0001").setWechatid("1232");
        userService.save(userDomain);
        try {
            Mono<UserDomain> userDomainMono = userManager.selectUserById_nested(id);
        }catch (Exception e){
            log.error("error");
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = ConstantList.NORMAL_MANAGER,propagation = Propagation.REQUIRES_NEW)
    public Mono<UserDomain> selectUserById_nested(Long id) throws BusinessException {
        UserDomain userDomain = userService.selectUserId(id);
        if(userDomain != null){
            throw new BusinessException(10);
        }
        return Mono.justOrEmpty(userDomain);
    }


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
    public Mono<List<UserDomain>> listByName(IdAndNameRequest request){
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
