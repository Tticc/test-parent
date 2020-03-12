package com.tester.testeraop.service.impl;

import com.tester.testeraop.controller.StackTraceAnnotation;
import com.tester.testeraop.service.DaoService;
import org.springframework.stereotype.Service;

/**
 * @Author 温昌营
 * @Date
 */
@Service("daoService")
@StackTraceAnnotation(servicesToBeRecorded = "com.tester.testeraop.service.DemoManager",mapper = "fjioaifjodjaiof")
public class DaoServiceImpl implements DaoService {

    @Override
    public void doNothing(Long id, String name) {

    }
}
