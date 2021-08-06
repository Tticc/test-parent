package com.tester.testerwebapp.service.beanPostProcess;

import com.tester.testercommon.model.request.TextRequest;
import com.tester.testerstarter.myBeanPostProcessor.annotation.MyAnnotationProxy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author 温昌营
 * @Date 2021-8-6 09:38:11
 */
@Service
public class MyAnnotationProxyManager {

    @Transactional
    @Async
    @MyAnnotationProxy
    public void test_anno(String name, TextRequest request) {
        System.out.println("nononono");
    }
}
