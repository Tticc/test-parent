package com.tester.testerwebapp.controller.mono;

import com.tester.testercommon.controller.BaseController;
import com.tester.testercommon.controller.RestResult;
import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.model.request.TextRequest;
import com.tester.testerwebapp.service.beanPostProcess.MyAnnotationProxyManager;
import com.tester.testerwebapp.service.beanPostProcess.MyDirectProxyManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "测试自定义的beanPostProcessor")
@RestController
@RequestMapping("/beanPostProcessor")
@Slf4j
public class BeanPostProcessorTestController extends BaseController {

    @Autowired
    private MyAnnotationProxyManager myAnnotationProxyManager;
    @Autowired
    private MyDirectProxyManager myDirectProxyManager;


    @ApiOperation(value = "anno", notes = "", httpMethod = "POST")
    @RequestMapping(value = "/anno", method = RequestMethod.GET)
    public Mono<RestResult<String>> test_anno() throws BusinessException {
        TextRequest request = new TextRequest();
        request.setText("text");
        myAnnotationProxyManager.test_anno("cv", request);
        Mono<String> stringMono = Mono.justOrEmpty("success");
        return monoSuccess(stringMono);
    }

    @ApiOperation(value = "direct", notes = "", httpMethod = "POST")
    @RequestMapping(value = "/direct", method = RequestMethod.GET)
    public Mono<RestResult<String>> test_direct() throws BusinessException {
        TextRequest request = new TextRequest();
        request.setText("text");
        myDirectProxyManager.test_direct("cv", request);
        Mono<String> stringMono = Mono.justOrEmpty("success");
        return monoSuccess(stringMono);
    }

}
