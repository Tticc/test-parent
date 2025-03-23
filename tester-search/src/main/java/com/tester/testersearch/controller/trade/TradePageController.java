package com.tester.testersearch.controller.trade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
@RequestMapping("/page/trade")
public class TradePageController {

    @GetMapping("/trade")
    public String getInit(HttpServletRequest req) {
        req.setAttribute("content", "something");
        req.setAttribute("someAttr", "here is the someAttr");
        return "trade/trade";
    }
}
