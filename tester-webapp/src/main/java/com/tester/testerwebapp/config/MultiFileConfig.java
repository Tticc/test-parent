package com.tester.testerwebapp.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.IOException;

@Configuration
public class MultiFileConfig {

    private String uploadTempDir = "/abc/acb";

    /**
     * 配合yml配置：<br/>
     *   servlet:<br/>
     *     multipart:<br/>
     *       enabled: false<br/>
     * @return
     * @throws IOException
     */
    @Bean
    public CommonsMultipartResolver getCommonsMultipartResolver() throws IOException {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        Resource uploadTempDirResource = new FileSystemResource(this.uploadTempDir);
        resolver.setUploadTempDir(uploadTempDirResource);
        resolver.setMaxUploadSize(100*1024*1024);
        resolver.setMaxInMemorySize(5*1024*1000);
        return resolver;
    }
}
