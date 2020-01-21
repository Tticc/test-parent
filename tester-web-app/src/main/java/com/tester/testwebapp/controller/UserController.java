package com.tester.testwebapp.controller;

import com.tester.testercommon.controller.BaseController;
import com.tester.testercommon.controller.RestResult;
import com.tester.testwebapp.dao.domain.UserDomain;
import com.tester.testwebapp.dao.mapper.UserMapper;
import com.tester.testwebapp.dao.service.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @Author 温昌营
 * @Date
 */
@RestController
@RequestMapping("/demo")
@Slf4j
public class UserController extends BaseController {
    @Autowired
    private UserManager userManager;

    /*@RequestMapping(value = "/demoStart", method = RequestMethod.POST)
    public Mono<UserDomain> demoStart(@RequestParam("id")Long id, @RequestParam("name") String name) {
        log.info("controller start here.");
        Mono<UserDomain> userDomainMono = userManager.selectUserById(id);
        userDomainMono.map(e -> {
            return (new RestResult()).code(200L).message("success").putTimestamp().data(e);
        });
        return userDomainMono;
    }*/
    @RequestMapping(value = "/demoStart", method = RequestMethod.POST)
    public Mono<RestResult<UserDomain>> demoStart(@RequestParam("id")Long id, @RequestParam("name") String name) {
        log.info("controller start here.");
        Mono<UserDomain> userDomainMono = userManager.selectUserById(id);
        return monoSuccess(userDomainMono);
    }
}
