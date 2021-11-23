package com.tester.testerwebapp.config.ssl;

import com.tester.testercommon.constant.ConstantList;
import io.undertow.servlet.api.SecurityConstraint;
import io.undertow.servlet.api.SecurityInfo;
import io.undertow.servlet.api.TransportGuaranteeType;
import io.undertow.servlet.api.WebResourceCollection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * sslContext配置
 *
 * @Date 15:26 2021/11/23
 * @Author 温昌营
 **/
@Configuration
public class SslConfigurer {

    @Value("${server.https.localhsot:0.0.0.0}")
    private String localhsot;
    @Value("${server.port}")
    private int serverPort;

    @Value("${server.ssl.key-store:classpath:mykeystore.jks}")
    private String keyStoreName;
    @Value("${server.ssl.key-store-password:changeit}")
    private String keyStorePassword;
    @Value("${server.ssl.key-store-type:JKS}")
    private String keyStoreType;

    @Value("${server.http.port:}")
    private Integer httpPort;
    @Value("${server.https.additional.ports:}")
    private String additionalHttpsPorts;


    @Bean
    public UndertowServletWebServerFactory servletContainer() {
        UndertowServletWebServerFactory undertowFactory = new UndertowServletWebServerFactory();
        // 将http端口转交给https端口处理
        addHttpPortToHttpsPort(undertowFactory, httpPort, serverPort);

        // 增加额外的监听端口
        if (!StringUtils.isEmpty(additionalHttpsPorts.trim())) {
            String[] split = additionalHttpsPorts.split(ConstantList.COMMON_SPLIT_STR);
            List<Integer> ports = new ArrayList<>();
            for (String s : split) {
                try {
                    ports.add(Integer.valueOf(s.trim()));
                } catch (NumberFormatException e) {
                }
            }
            addAdditionalHttpsPort(undertowFactory, ports);
        }
        return undertowFactory;
    }

    /**
     * 监听http端口，并将请求转到https端口处理
     *
     * @Date 15:25 2021/11/23
     * @Author 温昌营
     **/
    private void addHttpPortToHttpsPort(UndertowServletWebServerFactory undertowFactory, Integer httpPort, Integer httpsPort) {
        if (!checkPort(httpPort)) {
            return;
        }
        // 将http的端口转到https的端口
        undertowFactory.addBuilderCustomizers((builder) -> {
            builder.addHttpListener(httpPort, localhsot);
        });
        undertowFactory.addDeploymentInfoCustomizers((deploymentInfo) -> {
            deploymentInfo.addSecurityConstraint(new SecurityConstraint()
                    .addWebResourceCollection(new WebResourceCollection().addUrlPattern("/*"))
                    .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
                    .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT)
            ).setConfidentialPortManager(exchange -> httpsPort);
        });
    }

    /**
     * 监听额外的https端口
     *
     * @Date 15:26 2021/11/23
     * @Author 温昌营
     **/
    private void addAdditionalHttpsPort(UndertowServletWebServerFactory undertowFactory, List<Integer> ports) {
        if (CollectionUtils.isEmpty(ports)) {
            return;
        }
        undertowFactory.addBuilderCustomizers((builder) -> {
            SSLContext sslContext = getSslContext();
            if (null == sslContext) {
                return;
            }
            for (Integer port : ports) {
                if (!checkPort(port)) {
                    continue;
                }
                builder.addHttpsListener(port, localhsot, sslContext);
            }
        });
    }

    /**
     * 获取sslContext
     *
     * @Date 15:25 2021/11/23
     * @Author 温昌营
     **/
    private SSLContext getSslContext() {
        try {
            return SslContextFactory.createSslContext(keyStoreName, keyStoreType, keyStorePassword);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检查端口号范围
     *
     * @param httpPort
     * @return boolean
     * @Date 15:34 2021/11/23
     * @Author 温昌营
     **/
    private boolean checkPort(Integer httpPort) {
        if (httpPort == null || httpPort <= 0 || httpPort > 65536) {
            return false;
        }
        return true;
    }

}