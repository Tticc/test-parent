package com.tester.testerwebapp.myServlet;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 旧地址重定向。兼容现有客户端保存的旧地址<br/>
 * 所有已 /job 开头的请求都会被去掉 /job 后重定向，重新请求。
 *
 * @Date 15:22 2021/11/26
 * @Author 温昌营
 **/
@Slf4j
public class MyForwardFilter implements Filter {

    private static final String READINESS_STR = "/test-actuator/readiness";

    private static final String ORI_CONTEXT_PATH = "/job";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("MyForwardFilter 工作中");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        if (requestURI.startsWith(READINESS_STR)) {
            response.getWriter().println("{\"status\":\"UP\"}");
            return;
        }
        String newUri = requestURI;
        if (requestURI.startsWith(ORI_CONTEXT_PATH + "/") || ORI_CONTEXT_PATH.equals(requestURI)) {
            newUri = requestURI.substring(ORI_CONTEXT_PATH.length());
        }
        httpRequest.getRequestDispatcher(newUri).forward(request, response);
    }
}
