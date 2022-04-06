package com.tester.microservice.starter.feign.interceptor;

import com.tester.base.dto.language.LanguageUtil;
import com.tester.microservice.starter.feign.util.TraceContextUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class FeignRequestInterceptor implements RequestInterceptor {
    @Value("${spring.application.name:unknow}")
    private String applicationName;
    @Autowired
    private LanguageUtil languageUtil;


    public void apply(RequestTemplate template) {
        template.header("x-source-application-name", new String[]{this.applicationName});
        template.header("log-uuid", new String[]{MDC.get("X-B3-TraceId")});
        TraceContextUtil.injectTraceContext(template);
        template.header("x-http-lang", new String[]{this.languageUtil.getCurrentLanguage()});
    }
}
