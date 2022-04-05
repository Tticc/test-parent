package com.tester.testerwebapp.service.beanPostProcess;

import com.tester.base.dto.model.request.TextRequest;
import com.tester.testerstarter.myBeanPostProcessor.direct.MyDirectInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author 温昌营
 * @Date 2021-8-6 09:38:11
 */
@Service
public class MyDirectProxyManager implements MyDirectInterface {

    @Transactional(value = "transactionManger-normal")
    public void test_direct(String name, TextRequest request) {
        System.out.println("nononono");
    }
}
