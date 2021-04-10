package com.tester.testerwebapp.myServlet;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Slf4j
//@WebListener
public class MyServletListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("wenc -- servlet 监听了项目初始化完成");
    }
}
