package com.tester.testermybatis.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Author 温昌营
 * @Date
 */
public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{"com.tester.testermybatis.config.ThatShouldNotInBeanFactory1","com.tester.testermybatis.config.ThatShouldNotInBeanFactory2"};
    }
}
