package com.tester.testerstarter.autoconfigure;

import com.tester.testerstarter.config.LanguageConfig;
import com.tester.testerstarter.exception.BusinessExceptionHandler;
import com.tester.testerstarter.language.LanguageUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author 温昌营
 * @Date 2020-12-10 16:34:44
 */

@Configuration
//@EnableConfigurationProperties(RocketMQProperties.class)
//@ConditionalOnClass({ MQAdmin.class, ObjectMapper.class })
//@ConditionalOnProperty(prefix = "rocketmq", value = "name-server")
@Import({ LanguageConfig.class})
//@AutoConfigureAfter(JacksonAutoConfiguration.class)
public class TesterStarterAutoConfigure {


    @Bean
    @ConditionalOnMissingBean(name = {"businessExceptionHandler"})
    public BusinessExceptionHandler getBusinessExceptionHandler(){
        return new BusinessExceptionHandler();
    }
    @Bean
    @ConditionalOnMissingBean(name = {"languageUtil"})
    public LanguageUtil getLanguageUtil(){
        return new LanguageUtil();
    }
}
