package com.tester.testergateway.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


@Configuration
public class GatewayConfig {

    /**
     * 我是日志
     */
    private Logger LOGGER = LoggerFactory.getLogger(GatewayConfig.class);

    /** 截取domain的正则表达式*/
    private static Pattern pattern = Pattern.compile("http.*://(.+?)[:|/].*");

    @Value("${tester.gateway.whitelist.enable}")
    private Integer whiteListEnable;
    @Value("${tester.gateway.whitelist.global:}")
    private String whiteList;
    @Value("${tester.gateway.whitelist.all.tag:all}")
    private String allServiceTag;
    /** 是否开启访问权限校验*/
    @Value("${tester.gateway.visit.auth.enable:0}")
    private Integer gatewayVisitAuthEnable;

    @Autowired
    private VisitAuthMapConfig visitAuthMapConfig;






    public String getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(String whiteList) {
        this.whiteList = whiteList;
    }

    @Bean
    @Order(-1)
    public WebFilter apiPrefixFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getRawPath();
            List<PathContainer.Element> elementList = request.getPath().pathWithinApplication().elements();
            String serverName = elementList.get(3).value();
            if (whiteListEnable == 1) {
                if (!ignoreAuthentication(serverName)) {
                    return badResponse(exchange);
                }
            }
            if(gatewayVisitAuthEnable == 1){
                String hostName = request.getURI().getHost();
                if(!haveVisitAuthorizeOrNoConfig(hostName, serverName)){
                    LOGGER.info("block it, hostName：【{}】，path:【{}】", hostName, path);
                    return badResponse(exchange);
                }
                LOGGER.info("keep going, hostName：【{}】，path:【{}】", hostName, path);
            }
            String contentType = request.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);

            String newPath = path.replace("/api","").replace("tester-","");
            ServerHttpRequest newRequest = request.mutate().path(newPath).build();

            return chain.filter(exchange.mutate().request(newRequest).build());
        };
    }

    /** 检查是否为合法的请求*/
    private boolean haveVisitAuthorizeOrNoConfig(String hostName, String serverName){
        Map<String, Set<String>> whiteMap = visitAuthMapConfig.getMap();
        if(CollectionUtils.isEmpty(whiteMap)){
            return true;
        }
        Set<String> strings = whiteMap.get(hostName);
        if(null == strings || strings.contains(allServiceTag) || strings.contains(serverName)){
            return true;
        }
        return false;
    }
    /** 截取url中的domain*/
    private String getDomain(String url){
        Matcher mc = pattern.matcher(url);
        while (mc.find()){
            return mc.group(1).trim();
        }
        return url;
    }

    private Mono<Void> badResponse(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.NOT_FOUND);

        DataBuffer buffer = response.bufferFactory().wrap(HttpStatus.NOT_FOUND.getReasonPhrase().getBytes());
        return response.writeWith(Mono.just(buffer));
    }





    private boolean ignoreAuthentication(String url) {
        return Stream.of(this.whiteList.split(",")).anyMatch(ignoreUrl -> url.startsWith(StringUtils.trim(ignoreUrl)));
    }
}

