package com.tester.testerwebapp.controller.mono;

import com.tester.testercommon.controller.BaseController;
import com.tester.testercommon.controller.RestResult;
import com.tester.testercommon.model.request.IdAndNameRequest;
import com.tester.testerwebapp.dao.domain.UserDomain;
import com.tester.testerwebapp.dao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @Author 温昌营
 * @Date 2021-6-11 10:21:44
 */
@RestController
@RequestMapping("/mybatis")
@Slf4j
public class MybatisController extends BaseController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/select", method = RequestMethod.POST)
    public Mono<RestResult<UserDomain>> demoStart1(@RequestBody @Validated IdAndNameRequest model) {
        log.info("controller start here.");
        return monoSuccess(Mono.justOrEmpty(userService.selectUserId(model.getId())));
    }


    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Mono<RestResult<Integer>> insert() {
        log.info("controller start here.");
        UserDomain userDomain = new UserDomain().init();
        userDomain.setName("wenc").setCellphone("123498734892").setDataFrom(1).setEmployeeId("0001").setWechatid("1232");
        return monoSuccess(Mono.justOrEmpty(userService.save(userDomain)));
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Mono<RestResult<Integer>> update(@RequestBody @Validated IdAndNameRequest model) {
        log.info("controller start here.");
        UserDomain userDomain = new UserDomain();
        userDomain.setId(model.getId());
        userDomain.setName(model.getName());
        int update = userService.update(userDomain);
        return monoSuccess(Mono.justOrEmpty(update));
    }


}
