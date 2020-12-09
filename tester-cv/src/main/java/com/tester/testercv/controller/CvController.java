package com.tester.testercv.controller;


import com.tester.testercommon.controller.BaseController;
import com.tester.testercommon.controller.RestResult;
import com.tester.testercommon.model.request.IdAndNameModel;
import com.tester.testercommon.util.SpringBeanContextUtil;
import com.tester.testercv.config.OutBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/cv")
@Slf4j
public class CvController extends BaseController {

    @Autowired
    private OutBean outBean;
    @PostMapping(value = "/test_param")
    public RestResult test_param(@RequestBody @Valid IdAndNameModel model){
//        aopManager.test_param(model);
        outBean.doPrint();
        OutBean bean = SpringBeanContextUtil.getBean("containerBeanNamecontainerBeanName",OutBean.class);
        bean.doPrint();
        return success();
    }

}
