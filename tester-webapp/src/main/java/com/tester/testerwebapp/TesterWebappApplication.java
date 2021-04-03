package com.tester.testerwebapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;
import java.util.Set;

@MapperScan("com.tester.**.mapper")
@EnableAsync
//@SpringBootApplication(scanBasePackages = {"com.tester.*"})
@SpringBootApplication(scanBasePackages = {"com.tester.testercommon.*","com.tester.testerwebapp.*","com.tester.testermybatis.*"})
//@SpringBootApplication(scanBasePackages = {"com.aeon.dmc.cloud.office", "com.lingzhi"}, exclude = {DataSourceAutoConfiguration.class})
public class TesterWebappApplication implements CommandLineRunner, ApplicationRunner {

    public static void main(String[] args) {
//        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "C:\\Users\\Admin\\Desktop\\class1");
        SpringApplication.run(TesterWebappApplication.class, args);
    }

    @Autowired
    private ApplicationContext applicationContext;
    public void run(String... args) throws Exception {
        String properties = "spring.aop.proxy-target-class";
        String xxx = applicationContext.getEnvironment().getProperty(properties);
        System.out.println(properties + ": " + xxx);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Set<String> optionNames = args.getOptionNames();
        for (String optionName : optionNames) {
            List<String> optionValues = args.getOptionValues(optionName);
            System.out.println("optionName: "+optionName+", optionValues: "+optionValues);
        }
    }
}
