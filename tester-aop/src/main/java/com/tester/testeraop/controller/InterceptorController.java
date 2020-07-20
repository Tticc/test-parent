package com.tester.testeraop.controller;

import com.tester.testercommon.controller.BaseController;
import com.tester.testercommon.controller.RestResult;
import com.tester.testercommon.model.request.IdAndNameModel;
import com.tester.testercommon.model.request.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author 温昌营
 * @Date 2020-7-20
 */
@RestController
@RequestMapping("/interceptor")
@Slf4j
public class InterceptorController extends BaseController {

    @PostMapping(value = "/test_param")
    public RestResult test_param(@RequestBody @Valid UserModel model){
        log.info("userModel in controller method:{}",model);
        return success();
    }
}
