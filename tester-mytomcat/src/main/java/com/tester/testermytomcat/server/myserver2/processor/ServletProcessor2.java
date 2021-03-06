package com.tester.testermytomcat.server.myserver2.processor;

import com.tester.testermytomcat.server.CommonMethod;
import com.tester.testermytomcat.server.Constants;
import com.tester.testermytomcat.server.base.MyHttpRequest;
import com.tester.testermytomcat.server.base.MyHttpResponse;
import com.tester.testermytomcat.server.base.dto.MyRequestFacade;
import com.tester.testermytomcat.server.base.dto.MyResponseFacade;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

/**
 * servlet处理器
 */
@Slf4j
public class ServletProcessor2 {

    public void process(MyHttpRequest request, MyHttpResponse response){
        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        URLClassLoader loader = null;

        try {
            // create a URLClassLoader
            // 配置路径为webroot
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(Constants.WEB_ROOT);
            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
            urls[0] = new URL(null,repository,streamHandler);
            loader = new URLClassLoader(urls);
        } catch (IOException e) {
            log.error("something went wrong in ServletProcessor2",e);
        }
        Class myClass = null;
        try {
            // 读入webroot路径下的servletName.class
            myClass = loader.loadClass(servletName);
        } catch (ClassNotFoundException e) {
            log.error("加载servlet类失败。servletName：{}",servletName,e);
        }
        Servlet servlet = null;
        MyRequestFacade myRequestFacade = new MyRequestFacade(request);
        MyResponseFacade myResponseFacade = new MyResponseFacade(response);
        try {
            servlet = (Servlet)myClass.newInstance();
            PrintWriter writer = ((ServletResponse) myResponseFacade).getWriter();
            writer.println(CommonMethod.successHead());
            servlet.service((ServletRequest) myRequestFacade, (ServletResponse) myResponseFacade);
        } catch (Exception e) {
            log.error("实例化servlet失败。servletName：{}",servletName,e);
        } catch (Throwable e) {
            log.error("实例化servlet失败。servletName：{}",servletName,e);
        }
    }
}
