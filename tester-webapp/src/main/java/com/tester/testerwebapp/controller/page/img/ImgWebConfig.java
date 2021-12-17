package com.tester.testerwebapp.controller.page.img;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置指定请求路径的资源走指定的文件位置
 * @Author 温昌营
 * @Date 2021-11-18 10:34:40
 */
@Component
public class ImgWebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(ImgCommon.IMG_HTTP_URL_PREFIX+"/**").addResourceLocations(ImgCommon.IMG_ROOT_PATH);
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }
}
