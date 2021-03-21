package com.tester.testeraop.intercepts.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 可重复读流
 * @Author 温昌营
 * @Date
 */
@Slf4j
//@WebFilter(filterName = "repeatedlyReadFilter",urlPatterns = "/sign/*")
@WebFilter(filterName = "repeatedlyReadFilter",urlPatterns = "/*")
@Order(1)
public class RepeatedlyReadFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("filterConfig.getFilterName() "+filterConfig.getFilterName());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestWrapper requestWrapper = null;
        if(request instanceof HttpServletRequest){
            requestWrapper = new RequestWrapper((HttpServletRequest) request);
        }
        if(null != requestWrapper){
            chain.doFilter(requestWrapper,response);
        }else{
            chain.doFilter(request,response);
        }
    }

    @Override
    public void destroy() {

    }
}
