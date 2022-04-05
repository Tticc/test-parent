package com.tester.testeraop.controller;


import com.tester.testeraop.service.AopManager;
import com.tester.testercommon.controller.BaseController;
import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.model.request.IdAndNameRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/aop")
@Slf4j
public class AopController extends BaseController {

    @Autowired
    private AopManager aopManager;

    @PostMapping(value = "/test_param")
    public RestResult test_param(@RequestBody @Valid IdAndNameRequest model){
//        aopManager.test_param(model);
        return success();
    }

}
