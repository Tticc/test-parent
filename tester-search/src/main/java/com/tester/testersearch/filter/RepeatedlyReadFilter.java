package com.tester.testersearch.filter;

import com.tester.testercommon.util.endecrypt.http.Constants;
import com.tester.testercommon.util.endecrypt.http.EccCryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 可重复读流
 *
 * @Author 温昌营
 * @Date
 */
@Slf4j
//@WebFilter(filterName = "repeatedlyReadFilter",urlPatterns = "/sign/*")
@WebFilter(filterName = "repeatedlyReadFilter", urlPatterns = "/*")
@Order(1)
public class RepeatedlyReadFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("filterConfig.getFilterName() " + filterConfig.getFilterName());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            // 非 HTTP 请求，直接透传
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        RequestWrapper requestWrapper = new RequestWrapper(httpRequest);
        // 检查是否启用 ECC 加密
        boolean shouldDecrypt = "true".equalsIgnoreCase(httpRequest.getHeader("Ecc-Crypto-Enable"));
        boolean shouldEncrypt = "application/octet-stream".equalsIgnoreCase(httpRequest.getHeader("Accept"));
        ServletRequest finalRequest = requestWrapper;
        if (shouldDecrypt) {
            // 解密密文请求体
            byte[] encryptedBody = requestWrapper.getBodyBytes();
            String json = EccCryptoUtil.decrypt(encryptedBody, Constants.clientPrivateKey);

            // 替换请求体为解密后的明文
            finalRequest = new DecryptedRequestWrapper(requestWrapper, json);
        }
        ByteArrayResponseWrapper responseWrapper = new ByteArrayResponseWrapper(httpResponse);
        chain.doFilter(finalRequest, responseWrapper);
        byte[] responseBytes = responseWrapper.getBytes();
        if (shouldEncrypt) {// 用 ECC 加密响应体
            String responseBody = responseWrapper.getBodyAsString();
            byte[] encryptedResp = EccCryptoUtil.encrypt(responseBody, Constants.serverPublicKey);
            // 设置响应头为二进制流
            httpResponse.setContentType("application/octet-stream");
            httpResponse.setContentLength(encryptedResp.length);
            // 写入加密数据到响应流
            ServletOutputStream out = httpResponse.getOutputStream();
            out.write(encryptedResp);
            out.flush();
        } else {
            String originalContentType = responseWrapper.getContentType();
            if (originalContentType != null) {
                httpResponse.setContentType(originalContentType);
            }
            httpResponse.setCharacterEncoding(responseWrapper.getCharacterEncoding());
            httpResponse.setContentLength(responseBytes.length);
            ServletOutputStream out = httpResponse.getOutputStream();
            out.write(responseBytes);
            out.flush();
        }
    }

    @Override
    public void destroy() {

    }
}
