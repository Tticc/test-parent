package com.tester.testerwebapp.config;

import com.tester.testercommon.model.request.convert.TestConvertRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

import java.util.List;

/**
 * 这时一个可以修改springMVC比较核心配置的方式。
 * 通过集成WebMvcConfigurer并重写方法来实现自定义springMVC
 * <br/>
 *
 * @Author 温昌营
 * @Date 2021-4-10 16:14:15
 */

// @EnableWebMvc 配置
// 接管整个SpringMVC，springMVC的默认配置将不会生效。
// 因为这个注解导入了DelegatingWebMvcConfiguration，
// 而这个实例会使WebMVCAutoConfiguration失效。
// 一般不会使用
//@EnableWebMvc
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }

    /**
     * 自定义请求参数转换
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
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

    /**
     * 自定义返回数据转换器
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MyMessageConverter());
    }
}
