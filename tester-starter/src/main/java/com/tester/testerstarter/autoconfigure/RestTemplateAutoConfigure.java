package com.tester.testerstarter.autoconfigure;

import com.tester.testerstarter.interceptor.MyRestTemplateInterceptor;
import com.tester.testerstarter.language.LanguageUtil;
import com.tester.testerstarter.util.RestTemplateUtil;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动配置RestTemplate
 * @Date 11:18 2021/6/7
 * @Author 温昌营
 * @see org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration RestTemplateAutoConfiguration
 **/
@Configuration
@ConditionalOnProperty(prefix = "my.rest",name = {"connectTimeout"})
@ConfigurationProperties(prefix = "my.rest")
@AutoConfigureAfter(TesterStarterAutoConfigure.class)
public class RestTemplateAutoConfigure {
    @Autowired
    private LanguageUtil languageUtil;
    @Value("${spring.application.name:unknow}")
    private String applicationName;
    @Value("${my.rest.connectTimeout:2000}")
    private Integer connectTimeout = 2000;
    @Value("${my.rest.readTimeout:2000}")
    private Integer readTimeout = 2000;

    private MyRestTemplateInterceptor getMyRestTemplateInterceptor() {
        MyRestTemplateInterceptor myRestTemplateInterceptor = new MyRestTemplateInterceptor(this.languageUtil, this.applicationName);
        return myRestTemplateInterceptor;
    }

    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(this.simpleClientHttpRequestFactory());
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList(restTemplate.getInterceptors());
        interceptors.add(this.getMyRestTemplateInterceptor());
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

    @Bean(name = "restTemplateSSL")
    public RestTemplate restTemplateSSL() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (chain, authType) -> {
            return true;
        };
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial((KeyStore)null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        factory.setReadTimeout(this.readTimeout);
        factory.setConnectTimeout(this.connectTimeout);
        RestTemplate restTemplate = new RestTemplate(factory);
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList(restTemplate.getInterceptors());
        interceptors.add(this.getMyRestTemplateInterceptor());
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

    @Bean
    public RestTemplateUtil getRestTemplateUtil(){
        return new RestTemplateUtil();
    }

    /**
     * 设置连接超时时间
     * @param
     * @return org.springframework.http.client.ClientHttpRequestFactory
     * @Date 18:17 2021/1/19
     * @Author 温昌营
     **/
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(this.readTimeout);
        factory.setConnectTimeout(this.connectTimeout);
        return factory;
    }
}
