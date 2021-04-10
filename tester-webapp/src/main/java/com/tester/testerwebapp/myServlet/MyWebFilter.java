package com.tester.testerwebapp.myServlet;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

@Slf4j
//@WebFilter("/css/*")
public class MyWebFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("MyWebFilter 工作中");
        chain.doFilter(request,response);
    }
}
