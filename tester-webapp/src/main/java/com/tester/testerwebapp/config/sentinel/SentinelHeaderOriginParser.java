package com.tester.testerwebapp.config.sentinel;

import com.alibaba.csp.sentinel.adapter.servlet.callback.RequestOriginParser;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Component
public class SentinelHeaderOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        // 1. 获取请求头
        String origin = httpServletRequest.getHeader("origin");
        // 2. 非空判断
        if (StringUtils.isEmpty(origin)) {
            return "blank";
        }
        return origin;
    }
}
