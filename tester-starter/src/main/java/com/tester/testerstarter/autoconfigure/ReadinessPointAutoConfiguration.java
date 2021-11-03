package com.tester.testerstarter.autoconfigure;

import com.tester.testerstarter.point.ReadinessPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.endpoint.EndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * readiness
 * //  http://localhost:8004/actuator/readiness
 * @Author 温昌营
 * @Date 2021-7-12 15:55:11
 */
@Slf4j
@Configuration
@AutoConfigureAfter(EndpointAutoConfiguration.class)
@AutoConfigureBefore(WebEndpointAutoConfiguration .class)
@EnableConfigurationProperties(WebEndpointProperties.class)
public class ReadinessPointAutoConfiguration {
    @Bean
    public ReadinessPoint readinessPoint(HealthEndpoint health, WebEndpointProperties properties) {
        ReadinessPoint readinessPoint = new ReadinessPoint(health);

        /**
         * 启动即强制将readiness加入include列表，使之可用
         * 或者通过属性配置方式配置（各个服务自己配置）：
         * management:
         *   endpoints:
         *     web:
         *       exposure:
         *         include: readiness,health,info
         * 
         **/
        WebEndpointProperties.Exposure exposure = properties.getExposure();
        Set<String> include = exposure.getInclude();
        include.add("readiness");
        include.add("info"); // 如果不配置，默认会有
        include.add("health"); // 如果不配置，默认会有

        return readinessPoint;
    }
}
