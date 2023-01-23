package com.tester.testerspring.config.helper;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyImportSelector implements ImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata annotationMetadata) {
            System.out.println("MyImportSelector中的参数annotationMetadata的类为："+annotationMetadata.getClassName());
            return new String[]{"com.tester.testerspring.service.Demo"};
        }
    }