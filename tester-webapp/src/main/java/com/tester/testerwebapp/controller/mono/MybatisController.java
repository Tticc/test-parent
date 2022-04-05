package com.tester.testerwebapp.controller.mono;

import com.tester.testercommon.constant.ConstantList;
import com.tester.testercommon.controller.BaseController;
import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.model.request.IdAndNameRequest;
import com.tester.testerwebapp.dao.domain.UserDomain;
import com.tester.testerwebapp.dao.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 温昌营
 * @Date 2021-6-11 10:21:44
 */
@Api(tags = "Mybatis测试模块")
@RestController
@RequestMapping("/mybatis")
@Slf4j
public class MybatisController extends BaseController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "select", notes = "", httpMethod = "POST")
    @RequestMapping(value = "/select", method = RequestMethod.POST)
    @Transactional(transactionManager = ConstantList.NORMAL_MANAGER, rollbackFor = Exception.class)
    public Mono<RestResult<UserDomain>> demoStart1(@RequestBody @Validated IdAndNameRequest model) {
        log.info("controller start here.");
        UserDomain userDomain = userService.selectUserId(model.getId());
        UserDomain userDomain1 = userService.selectUserId(model.getId());
        return monoSuccess(Mono.justOrEmpty(userDomain));
    }


    @ApiOperation(value = "batchSave", notes = "", httpMethod = "POST")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Mono<RestResult<Integer>> insert() {
        log.info("controller start here.");
        UserDomain userDomain = new UserDomain().init();
        userDomain.setName("wenc").setCellphone("123498734892").setDataFrom(1).setEmployeeId("0001").setWechatid("1232");
        return monoSuccess(Mono.justOrEmpty(userService.save(userDomain)));
    }

    @ApiOperation(value = "batchSave", notes = "", httpMethod = "POST")
    @RequestMapping(value = "/batchSave", method = RequestMethod.POST)
    public Mono<RestResult<Integer>> batchSave() {
        log.info("controller start here.");
        List<UserDomain> list = new ArrayList<>();
        list.add(new UserDomain().init().setName("wenc").setCellphone("123498734892").setDataFrom(1).setEmployeeId("0001").setWechatid("1232"));
        list.add(new UserDomain().init().setName("wencc").setCellphone("1234987342").setDataFrom(1).setEmployeeId("0001").setWechatid("1232"));
        list.add(new UserDomain().init().setName("wenccc").setCellphone("1234987341").setDataFrom(1).setEmployeeId("0001").setWechatid("1232"));
        int i = userService.batchSaveUser(list);
        return monoSuccess(Mono.justOrEmpty(i));
    }


    @ApiOperation(value = "update", notes = "", httpMethod = "POST")
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
