package com.tester.microservice.starter.feign.util;

import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TraceContextUtil {
    private static String[] traceKeys = new String[]{"X-B3-TraceId", "X-B3-SpanId", "X-B3-ParentSpanId"};

    public static void injectTraceContext(RequestTemplate template) {
        String[] keys = traceKeys;
        int len = keys.length;

        for (int i = 0; i < len; ++i) {
            String traceName = keys[i];
            String traceVal = MDC.get(traceName);
            if (!StringUtils.isEmpty(traceVal)) {
                template.header(traceName, new String[]{traceVal});
            }
        }

    }

    public static void injectTraceContext(HttpHeaders headers) {
        String[] keys = traceKeys;
        int len = keys.length;

        for (int i = 0; i < len; ++i) {
            String traceKey = keys[i];
            String traceVal = MDC.get(traceKey);
            if (!StringUtils.isEmpty(traceVal)) {
                headers.add(traceKey, traceVal);
            }
        }

    }

    public static void injectTraceContext(HttpServletRequest request, HttpServletResponse response) {
        String[] keys = traceKeys;
        int len = keys.length;

        for (int i = 0; i < len; ++i) {
            String traceKey = keys[i];
            String traceVal = request.getHeader(traceKey);
            if (!StringUtils.isEmpty(traceVal)) {
                MDC.put(traceKey, traceVal);
                response.addHeader(traceKey, traceVal);
            }
        }

    }

    public static void remoteTraceContext() {
        String[] keys = traceKeys;
        int len = keys.length;

        for (int i = 0; i < len; ++i) {
            String traceKey = keys[i];
            MDC.remove(traceKey);
        }

    }
}
