package com.tester.testergateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/gw")
@Slf4j
public class TestGatewayController {
    @RequestMapping(value = "/in", method = RequestMethod.POST)
    public String test_in() throws Exception {
        log.info("controller start here.");
        return "success";
    }

    public void testCon(){
        AtomicReference<String> found = new AtomicReference<>(null);
        // 如果found是null那么update为，否则不做
        found.compareAndSet(null, "strAfterComp");
        System.out.println(found.get());
    }
}
