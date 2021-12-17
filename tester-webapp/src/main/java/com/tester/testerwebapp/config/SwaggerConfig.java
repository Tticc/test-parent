package com.tester.testerwebapp.config;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.ClassUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;

/**
 * @Author 温昌营
 * @Date 2021-11-30 14:46:01
 */
@Profile({"local", "dev", "sit"})
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static HashSet<String> basePkgSet = new HashSet<>();

    static {
        basePkgSet.add("com.tester.testerwebapp.controller.mono");
    }


    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis((requestHandler) -> declaringClass(requestHandler).transform(handlerPackage()).or(true))
                .paths(PathSelectors.any())
                .build();
    }


    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }

    private static Function<Class<?>, Boolean> handlerPackage() {
        return (input) -> {
            boolean target = false;
            for (String basePackage : basePkgSet) {
                if (ClassUtils.getPackageName(input).startsWith(basePackage)) {
                    return true;
                }
            }
            return target;
        };
    }
}
