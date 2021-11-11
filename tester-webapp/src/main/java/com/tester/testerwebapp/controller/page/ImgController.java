package com.tester.testerwebapp.controller.page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 温昌营
 * @Date 2021-11-11 18:27:57
 */
@Controller
@Slf4j
@RequestMapping("/img")
public class ImgController {
    @GetMapping("/home")
    public String getHome(HttpServletRequest req) {
        return "img/home";
    }
}
