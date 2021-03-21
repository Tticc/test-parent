package com.tester.testeraop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;

//@EnableFeignClients(basePackages = {"com.aeon.dmc.cloud.office", "com.lingzhi"})
//@EnableSwagger2
//@MapperScan("com.aeon.dmc.cloud.office.**.mapper")
//@SpringBootApplication(scanBasePackages = {"com.tester.testeraop", "com.tester"}, exclude = {DataSourceAutoConfiguration.class})
//@EnableTransactionManagement
//@EnableDiscoveryClient
@ServletComponentScan
@SpringBootApplication
public class TesterAopApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TesterAopApplication.class, args);
    }

    @Autowired
    private ApplicationContext applicationContext;
    @Override
    public void run(String... args) throws Exception {
        String property = applicationContext.getEnvironment().getProperty("propji.jifo.sjifo");
        System.out.println(property);
    }
}
