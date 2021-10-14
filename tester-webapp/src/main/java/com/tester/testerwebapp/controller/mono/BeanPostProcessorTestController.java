package com.tester.testerwebapp.controller.mono;

import com.tester.testercommon.controller.BaseController;
import com.tester.testercommon.controller.RestResult;
import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.model.request.TextRequest;
import com.tester.testerwebapp.service.beanPostProcess.MyAnnotationProxyManager;
import com.tester.testerwebapp.service.beanPostProcess.MyDirectProxyManager;
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
    private MyAnnotationProxyManager myAnnotationProxyManager;
    @Autowired
    private MyDirectProxyManager myDirectProxyManager;


    @RequestMapping(value = "/anno", method = RequestMethod.GET)
    public Mono<RestResult<String>> test_anno() throws BusinessException {
        TextRequest request = new TextRequest();
        request.setText("text");
        myAnnotationProxyManager.test_anno("cv", request);
        Mono<String> stringMono = Mono.justOrEmpty("success");
        return monoSuccess(stringMono);
    }

    @RequestMapping(value = "/direct", method = RequestMethod.GET)
    public Mono<RestResult<String>> test_direct() throws BusinessException {
        TextRequest request = new TextRequest();
        request.setText("text");
        myDirectProxyManager.test_direct("cv", request);
        Mono<String> stringMono = Mono.justOrEmpty("success");
        return monoSuccess(stringMono);
    }

}
