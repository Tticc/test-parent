package com.tester.testerwebapp.config;

import com.tester.testercommon.model.request.convert.TestConvertRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

/**
 * @Author 温昌营
 * @Date
 */


@Configuration
public class MyConfig {
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void configurePathMatch(PathMatchConfigurer configurer) {
                UrlPathHelper urlPathHelper = new UrlPathHelper();
                urlPathHelper.setRemoveSemicolonContent(false);
                configurer.setUrlPathHelper(urlPathHelper);
            }
            @Override
            public void addFormatters(FormatterRegistry registry){
                Converter<String, TestConvertRequest> converter = new Converter<String, TestConvertRequest>() {
                    @Override
                    public TestConvertRequest convert(String source) {
                        TestConvertRequest testConvertRequest = new TestConvertRequest();
                        if (!StringUtils.isEmpty(source)) {
                            String[] split = source.split(",");
                            testConvertRequest.setName(split[0]);
                            testConvertRequest.setAge(Integer.valueOf(split[1]));
                        }
                        return testConvertRequest;
                    }
                };
                registry.addConverter(converter);
            }
        };
    }
}
