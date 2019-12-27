package com.tester.testeraop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
@Slf4j
public class DemoController {

    @RequestMapping(value = "/demoStart", method = RequestMethod.POST)
    public String demoStart(@RequestParam("id")Long id, @RequestParam("name") String name){
        log.info("controller start here.");
        return "success";
    }
}
