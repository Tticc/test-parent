package com.tester.testerwebapp.controller.mono;

import com.tester.testercommon.controller.BaseController;
import com.tester.testercommon.controller.RestResult;
import com.tester.testercommon.model.request.IdAndNameModel;
import com.tester.testercommon.util.redis.RedisUtilValue;
import com.tester.testerwebapp.dao.domain.UserDomain;
import com.tester.testerwebapp.dao.service.UserManager;
import com.tester.testerwebapp.service.ExcelManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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




}
