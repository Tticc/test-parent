
package com.tester.testeraop.interceptor;

import com.alibaba.fastjson.JSON;
import com.tester.testeraop.annotation.RequireLogin;
import com.tester.testercommon.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @Date 11:19 2020/7/20 
 * @Author 温昌营 
 **/
@Slf4j
@Component
public class RequireLoginInterceptor implements HandlerInterceptor {

    private static final String HEADER_TOKEN = "token";
    private static final String splitStr = ";";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private Environment environment;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof ResourceHttpRequestHandler || handler instanceof DefaultServletHttpRequestHandler) {
            // 如果是访问的静态资源不拦截
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (handlerMethod.getBean() instanceof BasicErrorController) {
            return true;
        }
        String s = parseParameter(request);
        System.out.println(s);


        //在方法或类上面寻找注解
        if (handlerMethod.getMethodAnnotation(RequireLogin.class) == null
                && handlerMethod.getBeanType().getAnnotation(RequireLogin.class) == null) {
            return true;
        }

        //验证
        validateLogin(request);

        return true;
    }

    /**
     * 
     * @param request 
     * @return void 
     * @Date 11:20 2020/7/20 
     * @Author 温昌营 
     **/
    private void validateLogin(HttpServletRequest request) throws BusinessException {
    }

    private String parseParameter(HttpServletRequest request){
        Map<String, String> reqMap = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        Set<Map.Entry<String, String[]>> entries = parameterMap.entrySet();
        Iterator<Map.Entry<String,String[]>> it = entries.iterator();
        while (it.hasNext()){
            Map.Entry<String, String[]> next = it.next();
            String key = next.getKey();
            String value = next.getValue()[0];
            reqMap.put(key,value);
        }
        String s = JSON.toJSONString(reqMap);
        return s;
    }
}
