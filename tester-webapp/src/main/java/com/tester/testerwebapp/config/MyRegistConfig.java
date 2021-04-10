package com.tester.testerwebapp.config;


import com.tester.testerwebapp.myServlet.MyServlet;
import com.tester.testerwebapp.myServlet.MyServletListener;
import com.tester.testerwebapp.myServlet.MyWebFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRegistConfig {

    @Bean
    public ServletRegistrationBean myServlet(){
        MyServlet myServlet = new MyServlet();
        return new ServletRegistrationBean(myServlet,"/my","/my02");
    }

    @Bean
    public FilterRegistrationBean myFilter(){
        MyWebFilter myWebFilter = new MyWebFilter();
        // 方法1：
        return new FilterRegistrationBean(myWebFilter,myServlet());

        // 方法2：
//        FilterRegistrationBean filterBean = new FilterRegistrationBean(myWebFilter);
//        filterBean.setUrlPatterns(Arrays.asList("/my","/my02","/css/*"));
//        return filterBean;
    }

    @Bean
    public ServletListenerRegistrationBean myListener(){
        MyServletListener myServletListener = new MyServletListener();
        return new ServletListenerRegistrationBean(myServletListener);
    }

}
