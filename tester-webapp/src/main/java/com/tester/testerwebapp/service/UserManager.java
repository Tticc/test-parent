package com.tester.testerwebapp.service;

import com.tester.testercommon.constant.ConstantList;
import com.tester.base.dto.exception.BusinessException;
import com.tester.base.dto.model.request.IdAndNameRequest;
import com.tester.base.dto.model.request.TextRequest;
import com.tester.testercommon.util.CommonUtil;
import com.tester.testerwebapp.dao.domain.UserDomain;
import com.tester.testerwebapp.dao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
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

    @Lazy
    @Autowired
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

        Mono<UserDomain> defer = Mono.defer(() -> Mono.justOrEmpty(userService.selectUserId(id)));
        return defer;
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

    @Transactional(rollbackFor = Exception.class, transactionManager = ConstantList.NORMAL_MANAGER)
    public void test_asyncTransaction(TextRequest request) throws BusinessException {
        UserDomain userDomain = new UserDomain();
        userDomain.setId(1L);
        userDomain.setNote(request.getText());
        userService.update(userDomain);
        CommonUtil.doAfterTransactionCommitted((e) -> {
            userManager.testAsync(request);
        });
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = ConstantList.NORMAL_MANAGER)
    public void test_onlyTransaction(TextRequest request) throws BusinessException {
        UserDomain userDomain = new UserDomain();
        userDomain.setId(1L);
        userDomain.setNote(request.getText());
        userService.update(userDomain);
        CommonUtil.doAfterTransactionCommitted((e) -> {
            userManager.finalUpdate(request);
        });
    }


    @Async("cusThreadPool")
    public void testAsync(TextRequest request) throws BusinessException {
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        userManager.finalUpdate(request);
    }



    @Transactional(rollbackFor = Exception.class, transactionManager = ConstantList.NORMAL_MANAGER)
    public void finalUpdate(TextRequest request) throws BusinessException {
        UserDomain userDomain = new UserDomain();
        userDomain.setId(2L);
        userDomain.setNote(request.getText());
        int update = userService.update(userDomain);
        System.out.println("updated = "+update);

        // 抛异常
        throw new BusinessException(1L);
    }



    /**
     * 如果子方法包含进了当前事务,而且子方法抛出了异常，且被捕捉了。那么
     * 整个方法都会报错：
     * <br/>
     * Transaction rolled back because it has been marked as rollback-only
     * @param request
     * @return void
     * @Date 17:33 2021/5/17
     * @Author 温昌营
     **/
    @Transactional(rollbackFor = Exception.class, transactionManager = ConstantList.NORMAL_MANAGER)
    public void test_catchException(TextRequest request) throws BusinessException {
        UserDomain userDomain = new UserDomain();
        userDomain.setId(1L);
        userDomain.setNote(request.getText());
        userService.update(userDomain);
        try {
            userManager.finalUpdate(request);
        }catch (Exception e){
            log.error("error:{}",e.getMessage());
        }
    }
}
