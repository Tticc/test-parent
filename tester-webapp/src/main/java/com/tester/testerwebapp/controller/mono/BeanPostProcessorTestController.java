package com.tester.testerwebapp.controller.mono;

import com.tester.testercommon.controller.BaseController;
import com.tester.testercommon.controller.RestResult;
import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.model.request.TextRequest;
import com.tester.testerwebapp.service.beanPostProcess.MyAwareProxyManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 测试自定义的beanPostProcessor
 *
 * @Author 温昌营
 * @Date 2021-8-6 09:58:20
 */
@RestController
@RequestMapping("/beanPostProcessor")
@Slf4j
public class BeanPostProcessorTestController extends BaseController {

    @Autowired
    private MyAwareProxyManager myAwareProxyManager;


    @RequestMapping(value = "/aware", method = RequestMethod.GET)
    public Mono<RestResult<String>> prototype() throws BusinessException {
        TextRequest request = new TextRequest();
        request.setText("text");
        myAwareProxyManager.test_aware("cv", request);
        Mono<String> stringMono = Mono.justOrEmpty("success");
        return monoSuccess(stringMono);
    }

}
