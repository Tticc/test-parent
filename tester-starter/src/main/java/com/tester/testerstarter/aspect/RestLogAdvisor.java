package com.tester.testerstarter.aspect;

import com.alibaba.fastjson.JSON;
import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.model.request.PagerInfo;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public class RestLogAdvisor extends AspectJExpressionPointcutAdvisor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestLogAdvisor.class);
    private static final String msg = "\r\nIP:{}\r\nURL:{}\r\nHTTP_METHOD:{}\r\nCLASS_METHOD:{}\r\nARGS:{}\r\nResult:{}\r\nConsuming:{}ms\r\n";
    @Value("${my.log.config.pointcut.expression:execution(public * com.tester..*Controller.*(..))}")
    private String logPointcutExpression;


    @PostConstruct
    public void init() {
        super.setExpression(this.logPointcutExpression);
        super.setAdvice(new MethodInterceptor() {
            public Object invoke(MethodInvocation mi) throws Throwable {
                if (!(mi instanceof ProxyMethodInvocation)) {
                    throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
                } else {
                    ProxyMethodInvocation pmi = (ProxyMethodInvocation) mi;
                    ProceedingJoinPoint pjp = new MethodInvocationProceedingJoinPoint(pmi);
                    return RestLogAdvisor.this.around(pjp);
                }
            }
        });
    }

    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object[] argsOrig = joinPoint.getArgs();
        StringBuilder args = new StringBuilder();
        boolean flag = true;

        for (int i = 0; i < argsOrig.length; ++i) {
            if (argsOrig[i] instanceof Collection && ((Collection) argsOrig[i]).size() > 10) {
                flag = false;
            }
        }

        if (flag) {
            Object[] var18 = argsOrig;
            int var8 = argsOrig.length;

            for (int var9 = 0; var9 < var8; ++var9) {
                Object arg = var18[var9];

                try {
                    if (this.canConvertJson(arg)) {
                        args.append(JSON.toJSONString(arg)).append(", ");
                    } else {
                        args.append(arg.toString()).append(", ");
                    }
                } catch (Exception var17) {
                    args.append(arg.toString()).append(", ");
                }
            }
        }

        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String url = "";
        String httpMethod = "";
        String ip = "";
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            url = request.getRequestURL().toString();
            httpMethod = request.getMethod();
            ip = this.getIpAddr(request);
        }

        Object result;
        long methodEnd;
        try {
            result = joinPoint.proceed();
        } catch (Exception var16) {
            methodEnd = System.currentTimeMillis();
            result = " Exception:" + var16.getMessage();
            LOGGER.error("\r\nIP:{}\r\nURL:{}\r\nHTTP_METHOD:{}\r\nCLASS_METHOD:{}\r\nARGS:{}\r\nResult:{}\r\nConsuming:{}ms\r\n", new Object[]{ip, url, httpMethod, classMethod, args.toString(), result, methodEnd - startTime, var16});
            throw var16;
        }

        methodEnd = System.currentTimeMillis();
        LOGGER.info("\r\nIP:{}\r\nURL:{}\r\nHTTP_METHOD:{}\r\nCLASS_METHOD:{}\r\nARGS:{}\r\nResult:{}\r\nConsuming:{}ms\r\n", new Object[]{ip, url, httpMethod, classMethod, args.toString(), this.getLoggerJson(result), methodEnd - startTime});
        return result;
    }

    private String getLoggerJson(Object result) {
        String loggerJson;
        try {
            if (result instanceof RestResult) {
                RestResult restResult = (RestResult) result;
                Object data = restResult.getData();
                if (data != null) {
                    if (data instanceof Collection) {
                        loggerJson = "Collection not LOGGER";
                    } else if (data instanceof PagerInfo) {
                        PagerInfo pagerInfo = (PagerInfo) data;
                        PagerInfo newPageInfo = new PagerInfo();
                        newPageInfo.setTotal(pagerInfo.getTotal());
                        newPageInfo.setPageNum(pagerInfo.getPageNum());
                        newPageInfo.setPageSize(pagerInfo.getPageSize());
                        loggerJson = JSON.toJSONString(newPageInfo);
                    } else {
                        loggerJson = JSON.toJSONString(result);
                    }
                } else {
                    loggerJson = JSON.toJSONString(result);
                }
            } else if (result instanceof Collection) {
                loggerJson = "Collection not LOGGER";
            } else {
                loggerJson = JSON.toJSONString(result);
            }
        } catch (Exception var7) {
            loggerJson = "日志转换错误：" + var7.getMessage() + "，输出原日志：" + JSON.toJSONString(result);
        }

        return loggerJson;
    }

    private boolean canConvertJson(Object arg) {
        return !(arg instanceof ServletRequest) && !(arg instanceof ServletResponse) && !(arg instanceof MultipartFile);
    }

    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
