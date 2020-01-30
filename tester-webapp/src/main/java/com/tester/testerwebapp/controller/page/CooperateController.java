package com.tester.testerwebapp.controller.page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
@RequestMapping("/coor")
public class CooperateController {
	private static final String COOPCONTENT = "coopContent";
	private StringBuffer sb = new StringBuffer("init");

	@GetMapping("/init")
	public String getPage(HttpServletRequest req) {
		String initContent = COOPCONTENT;
		this.sb.replace(0, this.sb.length(), initContent);
		req.setAttribute("content", initContent);
		return "coor";
	}
	@GetMapping("/ind")
	public String index(HttpServletRequest req) {
		return "index";
	}
	@GetMapping("demo")
	public String demo(HttpServletRequest req) {
		return "demo";
	}

}
