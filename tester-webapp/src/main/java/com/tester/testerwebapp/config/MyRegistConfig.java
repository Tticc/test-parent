package com.tester.testerwebapp.config;


import com.tester.testerwebapp.myServlet.MyForwardFilter;
import com.tester.testerwebapp.myServlet.MyServlet;
import com.tester.testerwebapp.myServlet.MyServletListener;
import com.tester.testerwebapp.myServlet.MyWebFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import java.util.Arrays;

/**
 * 示例。<br/>
 * 注册filter，servlet，listener
 * @Date 9:16 2021/11/30
 * @Author 温昌营
 **/
@Configuration
public class MyRegistConfig {

    @Bean
    public ServletRegistrationBean myServlet() {
        MyServlet myServlet = new MyServlet();
        return new ServletRegistrationBean(myServlet, "/my", "/my02");
    }

    /**
     * 这是使用@Bean的方式注入filter，<br/>
     * 还有另外一种。在filter上加javax.servlet.annotation.WebFilter注解。<br/>
     * 同时在启动类加上 @ServletComponentScan("com.tester.testerwebapp.myServlet")注解<br/>
     * @see MyWebFilter
     * @Date 15:26 2021/11/26
     * @Author 温昌营
     **/
    @Bean
    public FilterRegistrationBean myFilter() {
        MyWebFilter myWebFilter = new MyWebFilter();
        // 方法1：
        return new FilterRegistrationBean(myWebFilter, myServlet());

        // 方法2：
//        FilterRegistrationBean filterBean = new FilterRegistrationBean(myWebFilter);
//        filterBean.setUrlPatterns(Arrays.asList("/my","/my02","/css/*"));
//        filterBean.setOrder(Integer.MIN_VALUE);
//        filterBean.setDispatcherTypes(DispatcherType.ASYNC,
//                DispatcherType.INCLUDE,
////                DispatcherType.FORWARD,  // 去掉forward，避免死循环
//                DispatcherType.REQUEST);
//        return filterBean;
    }

    @Bean
    public FilterRegistrationBean myForwardFilter() {
        MyForwardFilter myForwardFilter = new MyForwardFilter();
        // 方法2：
        FilterRegistrationBean filterBean = new FilterRegistrationBean(myForwardFilter);
        filterBean.setUrlPatterns(Arrays.asList("/*"));
        filterBean.setOrder(Integer.MIN_VALUE); // 最高优先级
        filterBean.setDispatcherTypes(DispatcherType.ASYNC,
                DispatcherType.INCLUDE,
//                DispatcherType.FORWARD,  // 去掉forward，避免死循环
                DispatcherType.REQUEST);
        return filterBean;
    }

    @Bean
    public ServletListenerRegistrationBean myListener() {
        MyServletListener myServletListener = new MyServletListener();
        return new ServletListenerRegistrationBean(myServletListener);
    }

}
