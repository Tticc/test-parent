package com.tester.testerwebapp.controller.mono;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tester.testercommon.controller.BaseController;
import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.exception.BusinessException;
import com.tester.base.dto.model.request.IdAndNameRequest;
import com.tester.base.dto.model.request.TextRequest;
import com.tester.testercommon.util.DateUtil;
import com.tester.testercommon.util.redis.RedisUtilValue;
import com.tester.testerwebapp.dao.domain.UserDomain;
import com.tester.testerwebapp.service.MyService;
import com.tester.testerwebapp.service.UserManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author 温昌营
 * @Date
 */
@Api(tags = "user模块")
@RestController
@RequestMapping("/demo")
@Slf4j
public class UserController extends BaseController {
    @Autowired
    private UserManager userManager;
    @Autowired
    private MyService myService;
    @Autowired
    private RedisUtilValue redisUtilValue;

    /*@RequestMapping(value = "/demoStart", method = RequestMethod.POST)
    public Mono<UserDomain> demoStart(@RequestParam("id")Long id, @RequestParam("name") String name) {
        log.info("controller start here.");
        Mono<UserDomain> userDomainMono = userManager.selectUserById(id);
        userDomainMono.map(e -> {
            return (new RestResult()).code(200L).message("success").putTimestamp().data(e);
        });
        return userDomainMono;
    }*/
    @ApiOperation(value = "demoStart", notes = "", httpMethod = "POST")
    @RequestMapping(value = "/demoStart", method = RequestMethod.POST)
    public Mono<RestResult<UserDomain>> demoStart(@RequestParam("id") Long id, @RequestParam("name") String name) {
        log.info("controller start here.");
        myService.print();
        Mono<UserDomain> userDomainMono = userManager.selectUserById(id);
        return monoSuccess(userDomainMono);
    }

    /**
     * 测试 MethodArgumentNotValidException，不符合@NotNull成功抛出此异常
     *
     * @param model
     * @return reactor.core.publisher.Mono<com.tester.base.dto.controller.RestResult < com.tester.testerwebapp.dao.domain.UserDomain>>
     * @Date 17:21 2021/1/7
     * @Author 温昌营
     **/
    @ApiOperation(value = "demoStart1", notes = "", httpMethod = "POST")
    @RequestMapping(value = "/demoStart1", method = RequestMethod.POST)
    public Mono<RestResult<UserDomain>> demoStart1(@Validated @RequestBody IdAndNameRequest model) {
        log.info("controller start here.");
        try {
            redisUtilValue.setValue("model1", "model1_test_" + DateUtil.dateFormat(new Date()));
        }catch (Exception e){
            log.error("set redis failed.err:",e);
        }
        Mono<UserDomain> userDomainMono = userManager.selectUserById(model.getId());
        // 注意，此时block已经脱离了userManager.selectUserById的事务范围
        // 因此，userManager.selectUserById里面的userService.selectUserId方法执行不在事务之内
        UserDomain block = userDomainMono.block();
        return monoSuccess(userDomainMono);
    }


    /**
     * 测试 ConstraintViolationException。没结果
     *
     * @param name
     * @return reactor.core.publisher.Mono<com.tester.base.dto.controller.RestResult < com.tester.testerwebapp.dao.domain.UserDomain>>
     * @Date 17:20 2021/1/7
     * @Author 温昌营
     **/
    @ApiOperation(value = "demoStart2", notes = "", httpMethod = "POST")
    @RequestMapping(value = "/demoStart2/{name}", method = RequestMethod.POST)
    public Mono<RestResult<UserDomain>> demoStart2(@Size(max = 10, min = 3, message = "name should have between 3 and 10 characters") @PathVariable("name") String name) {
        log.info("controller start here.");
        System.out.println(name);
        Mono<UserDomain> userDomainMono = userManager.selectUserById(1L);
        return monoSuccess(userDomainMono);
    }

    @ApiOperation(value = "insert", notes = "", httpMethod = "POST")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Mono<RestResult<Integer>> insert() {
        log.info("controller start here.");
        Mono<Integer> userDomainMono = userManager.insert();
        return monoSuccess(userDomainMono);
    }

    @ApiOperation(value = "login", notes = "", httpMethod = "POST")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Mono<RestResult<Serializable>> login() {
        redisUtilValue.setValue("user::login::1", "123231");
        return monoSuccess();
    }

    @ApiOperation(value = "glogin", notes = "", httpMethod = "POST")
    @RequestMapping(value = "glogin", method = RequestMethod.POST)
    public Mono<RestResult<Serializable>> getLogin() {
        Mono<Serializable> just = Mono.justOrEmpty(redisUtilValue.getValue("user::1"));
        return monoSuccess(just);
    }

    @ApiOperation(value = "listByName", notes = "", httpMethod = "POST")
    @RequestMapping(value = "listByName", method = RequestMethod.POST)
    public Mono<RestResult<Serializable>> listByName() {
//        PageInfo<UserDomain> result = new PageInfo<>();
        Page<UserDomain> page = PageHelper.startPage(2, 2);
        IdAndNameRequest request = new IdAndNameRequest();
        userManager.listByName(request.setName("name"));
        List<UserDomain> result = page.getResult();
        System.out.println();
        System.out.println();
        System.out.println("all list is:" + result);
        System.out.println("list.size:" + result.size());
        System.out.println("page.getTotal:" + page.getTotal());
        Page<UserDomain> page1 = PageHelper.startPage(2, 2);
        userManager.listByName(request.setName("name"));
        return monoSuccess();
    }

    @ApiOperation(value = "exTest", notes = "", httpMethod = "POST")
    @RequestMapping(value = "exTest", method = RequestMethod.POST)
    public Mono<RestResult<String>> exTest() throws BusinessException {
        if (true) {
            throw new BusinessException(11L);
        }
        return monoSuccess();
    }

    @ApiOperation(value = "test_asyncTransaction", notes = "", httpMethod = "POST")
    @RequestMapping(value = "test_asyncTransaction", method = RequestMethod.POST)
    public Mono<RestResult<String>> test_asyncTransaction(@Validated @RequestBody TextRequest request) throws BusinessException {
        userManager.test_asyncTransaction(request);
        return monoSuccess();
    }

    @ApiOperation(value = "test_onlyTransaction", notes = "", httpMethod = "POST")
    @RequestMapping(value = "test_onlyTransaction", method = RequestMethod.POST)
    public Mono<RestResult<String>> test_onlyTransaction(@Validated @RequestBody TextRequest request) throws BusinessException {
        userManager.test_onlyTransaction(request);
        return monoSuccess();
    }

    @ApiOperation(value = "test_catchException", notes = "", httpMethod = "POST")
    @RequestMapping(value = "test_catchException", method = RequestMethod.POST)
    public Mono<RestResult<String>> test_catchException(@Validated @RequestBody TextRequest request) throws BusinessException {
        userManager.test_catchException(request);
        return monoSuccess();
    }


}
