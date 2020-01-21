package com.tester.testwebapp.controller;

import com.tester.testwebapp.dao.domain.UserDomain;
import com.tester.testwebapp.dao.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author 温昌营
 * @Date
 */
@RestController
@RequestMapping("/demo")
@Slf4j
public class UserController {

    @Resource
    private UserMapper userMapper;
    @RequestMapping(value = "/demoStart", method = RequestMethod.POST)
    public String demoStart(@RequestParam("id")Long id, @RequestParam("name") String name){
        log.info("controller start here.");
        UserDomain userDomain = userMapper.selectOne(id);
        System.out.println(userDomain);
        return "success";
    }
}
