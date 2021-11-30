package com.tester.testerwebapp.myServlet;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 记录的两种filter注入方式之一
 * @Date 15:26 2021/11/26
 * @Author 温昌营
 **/
@Slf4j
//@WebFilter(filterName = "resetUriFilter", urlPatterns = "/*")
public class MyWebFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("MyWebFilter 工作中");
        chain.doFilter(request,response);
    }
}
