package com.tester.testergateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@Configuration
@ConfigurationProperties(prefix = "tester.gateway.visit.auth")
public class VisitAuthMapConfig {

    private Map<String, Set<String>> map;

    public Map<String, Set<String>> getMap() {
        return map;
    }

    public void setMap(Map<String, Set<String>> map) {
        this.map = map;
    }

}
