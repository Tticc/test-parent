package com.tester.testerstarter.interceptor;

import com.tester.testerstarter.language.LanguageUtil;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class MyRestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private LanguageUtil languageUtil;
    private String applicationName;

    public MyRestTemplateInterceptor(LanguageUtil languageUtil, String applicationName) {
        this.languageUtil = languageUtil;
        this.applicationName = applicationName;
    }

    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add("x-http-lang", this.languageUtil.getCurrentLanguage());
        headers.add("log-uuid", MDC.get("X-B3-TraceId"));
        headers.add("x-source-application-name", this.applicationName);
        return execution.execute(request, body);
    }
}
