package com.tester.thirdparty.xxljob;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 温昌营
 * @Date 2021-11-2 13:47:42
 */
@Data
@Configuration
public class JobConfigurer {


    @Value("${data.tools.auto.release.job.url:http://xx-job.xx.com/job}")
    private String jobUrl;
    @Value("${data.tools.auto.release.job.usr:admin}")
    private String jobUsr;
    @Value("${data.tools.auto.release.job.pwd:admin}")
    private String jobPwd;
}
