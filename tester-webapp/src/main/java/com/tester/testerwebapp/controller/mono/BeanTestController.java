package com.tester.testerwebapp.controller.mono;

import com.tester.testercommon.controller.BaseController;
import com.tester.testercommon.controller.RestResult;
import com.tester.testercommon.exception.BusinessException;
import com.tester.testerwebapp.service.UserManager;
import com.tester.testerwebapp.service.spring.aware.AwareService;
import com.tester.testerwebapp.service.spring.lifecycle.LifecycleService;
import com.tester.testerwebapp.service.spring.scope.PrototypeScopeService;
import com.tester.testerwebapp.service.spring.scope.RequestScopeService;
import com.tester.testerwebapp.service.spring.scope.SessionScopeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

/**
 * 测试bean
 *
 * @Author 温昌营
 * @Date 2021-7-5 15:56:13
 */
@Api(tags = "测试bean模块")
@RestController
@RequestMapping("/bean")
@Slf4j
public class BeanTestController extends BaseController implements ApplicationContextAware, InitializingBean {

    @Autowired
    private PrototypeScopeService prototypeScopeService;

    @Autowired
    private LifecycleService lifecycleService;

    @Autowired
    private UserManager userManager;
    @Autowired
    private AwareService awareService;

    @ApiOperation(value = "prototype", notes = "", httpMethod = "POST")
    @RequestMapping(value = "/prototype", method = RequestMethod.GET)
    public Mono<RestResult<String>> prototype() throws BusinessException {
        PrototypeScopeService prototypeService = this.applicationContext.getBean("prototypeScopeService", PrototypeScopeService.class);
        prototypeService.printMySelf();
        Mono<String> stringMono = Mono.justOrEmpty("success");
        return monoSuccess(stringMono);
    }

    @ApiOperation(value = "request", notes = "", httpMethod = "POST")
    @RequestMapping(value = "/request", method = RequestMethod.GET)
    public Mono<RestResult<String>> request() throws BusinessException {
        RequestScopeService requestScopeService = this.applicationContext.getBean("requestScopeService", RequestScopeService.class);
        System.out.println(requestScopeService);
        doPrint();
        Mono<String> stringMono = Mono.justOrEmpty("success");
        return monoSuccess(stringMono);
    }

    private void doPrint() {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        RequestScopeService requestScopeService = (RequestScopeService) attributes.getAttribute("requestScopeService", RequestAttributes.SCOPE_REQUEST);
        requestScopeService.printMySelf();
    }


    @RequestMapping(value = "/session", method = RequestMethod.GET)
    public Mono<RestResult<String>> session() throws BusinessException {
        SessionScopeService sessionScopeService = this.applicationContext.getBean("sessionScopeService", SessionScopeService.class);
        System.out.println(sessionScopeService);
        doPrintSession();
        Mono<String> stringMono = Mono.justOrEmpty("success");
        return monoSuccess(stringMono);
    }

    private void doPrintSession() {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        SessionScopeService sessionScopeService = (SessionScopeService) attributes.getAttribute("sessionScopeService", RequestAttributes.SCOPE_SESSION);
        sessionScopeService.printMySelf();
    }


    @RequestMapping(value = "/awareService", method = RequestMethod.GET)
    public Mono<RestResult<String>> awareService() throws BusinessException {
//        awareService.doPublishEvent();
        return monoSuccess(Mono.justOrEmpty("success"));
    }


    @Autowired
    private ApplicationContext applicationContext;

    public void setApplicationContext(
            ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
    }


    @PostConstruct
    public void post() {
        System.out.println("xxx");
//        System.out.println(requestScopeService);
        System.out.println(prototypeScopeService);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("xxx");
//        System.out.println(requestScopeService);
        System.out.println(prototypeScopeService);
    }
}
