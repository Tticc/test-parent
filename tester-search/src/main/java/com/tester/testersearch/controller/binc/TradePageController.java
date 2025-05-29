package com.tester.testersearch.controller.binc;

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
    public String getTrade(HttpServletRequest req) {
        req.setAttribute("content", "something");
        req.setAttribute("someAttr", "here is the someAttr");
        return "trade/trade";
    }

    @GetMapping("/trade2")
    public String getTrade2(HttpServletRequest req) {
        req.setAttribute("content", "something");
        req.setAttribute("someAttr", "here is the someAttr");
        return "trade/trade2";
    }

    @GetMapping("/trade3")
    public String getTrade3(HttpServletRequest req) {
        req.setAttribute("content", "something");
        req.setAttribute("someAttr", "here is the someAttr");
        return "trade/trade3";
    }

    @GetMapping("/trade4")
    public String getTrade4(HttpServletRequest req) {
        req.setAttribute("content", "something");
        req.setAttribute("someAttr", "here is the someAttr");
        return "trade/trade4";
    }

    @GetMapping("/trade5")
    public String getTrade5(HttpServletRequest req) {
        req.setAttribute("content", "something");
        req.setAttribute("someAttr", "here is the someAttr");
        return "trade/trade5";
    }
}
