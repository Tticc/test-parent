package com.tester.testercv;

import com.tester.testercommon.util.SpringBeanContextUtil;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication//(scanBasePackages = {"com.tester.testercv", "com.tester.testercommon"})
public class TesterCvApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(TesterCvApplication.class, args);
        String properties = "spring.datasource.url";
        String xxx = run.getEnvironment().getProperty(properties);
        System.out.println("TesterCvApplication: "+properties + ": " + xxx);

        ConfigurableListableBeanFactory beanFactory = run.getBeanFactory();
        System.out.println("scopes is what? "+beanFactory);
    }
}
