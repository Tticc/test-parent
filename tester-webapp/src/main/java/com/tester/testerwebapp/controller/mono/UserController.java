package com.tester.testerwebapp.controller.mono;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tester.testercommon.controller.BaseController;
import com.tester.testercommon.controller.RestResult;
import com.tester.testercommon.model.request.IdAndNameModel;
import com.tester.testercommon.util.redis.RedisUtilValue;
import com.tester.testerwebapp.dao.domain.UserDomain;
import com.tester.testerwebapp.service.ExcelManager;
import com.tester.testerwebapp.service.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Size;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

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
    @Autowired
    private RedisUtilValue redisUtilValue;
    @Autowired
    private ExcelManager excelManager;
    @PostMapping(value = "/uploadExcel")
    public RestResult<String> uploadExcel(@RequestPart("file") MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        String filename = file.getOriginalFilename();
        try {
            excelManager.parseExcel(inputStream, filename);
        }finally {
            if(null != inputStream){
                inputStream.close();
            }
        }
        return success();
    }
    @PostMapping(value = "/uploadForDifSource")
    public RestResult<Void> uploadForDifSource(@RequestPart("file") MultipartFile file,@ModelAttribute IdAndNameModel model) throws UnsupportedEncodingException {
        String originalFileName = file.getOriginalFilename();
        System.out.println(originalFileName);
        originalFileName = URLDecoder.decode(originalFileName, "UTF-8");
        System.out.println(originalFileName);
        System.out.println(model);
        return success();
    }

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

    /**
     * 测试 MethodArgumentNotValidException，不符合@NotNull成功抛出此异常
     * @param model
     * @return reactor.core.publisher.Mono<com.tester.testercommon.controller.RestResult<com.tester.testerwebapp.dao.domain.UserDomain>>
     * @Date 17:21 2021/1/7
     * @Author 温昌营
     **/
    @RequestMapping(value = "/demoStart1", method = RequestMethod.POST)
    public Mono<RestResult<UserDomain>> demoStart1(@Validated @RequestBody IdAndNameModel model) {
        log.info("controller start here.");
        Mono<UserDomain> userDomainMono = userManager.selectUserById(model.getId());
        return monoSuccess(userDomainMono);
    }


    /**
     * 测试 ConstraintViolationException。没结果
     * @param name
     * @return reactor.core.publisher.Mono<com.tester.testercommon.controller.RestResult<com.tester.testerwebapp.dao.domain.UserDomain>>
     * @Date 17:20 2021/1/7
     * @Author 温昌营
     **/
    @RequestMapping(value = "/demoStart2/{name}", method = RequestMethod.POST)
    public Mono<RestResult<UserDomain>> demoStart2(@Size(max = 10, min = 3, message = "name should have between 3 and 10 characters") @PathVariable("name") String name) {
        log.info("controller start here.");
        System.out.println(name);
        Mono<UserDomain> userDomainMono = userManager.selectUserById(1L);
        return monoSuccess(userDomainMono);
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Mono<RestResult<Integer>> insert() {
        log.info("controller start here.");
        Mono<Integer> userDomainMono = userManager.insert();
        return monoSuccess(userDomainMono);
    }

    @RequestMapping(value="login", method = RequestMethod.POST)
    public Mono<RestResult<Serializable>> login(){
        redisUtilValue.setValue("user::login::1","123231");
        return monoSuccess();
    }

    @RequestMapping(value="glogin", method = RequestMethod.POST)
    public Mono<RestResult<Serializable>> getLogin(){
        Mono<Serializable> just = Mono.justOrEmpty(redisUtilValue.getValue("user::1"));
        return monoSuccess(just);
    }

    @RequestMapping(value="listByName", method = RequestMethod.POST)
    public Mono<RestResult<Serializable>> listByName(){
//        PageInfo<UserDomain> result = new PageInfo<>();
        Page<UserDomain> page = PageHelper.startPage(2, 2);
        IdAndNameModel request = new IdAndNameModel();
        userManager.listByName(request.setName("name"));
        List<UserDomain> result = page.getResult();
        System.out.println();
        System.out.println();
        System.out.println("all list is:"+result);
        System.out.println("list.size:"+result.size());
        System.out.println("page.getTotal:"+page.getTotal());
        Page<UserDomain> page1 = PageHelper.startPage(2, 2);
        userManager.listByName(request.setName("name"));
        return monoSuccess();
    }




}
