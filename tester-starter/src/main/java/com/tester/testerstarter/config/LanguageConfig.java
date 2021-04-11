package com.tester.testerstarter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class LanguageConfig {
    @Value("${tester.language.default:zh_cn}")
    private String defaultLanguage;
    @Value("${tester.language.support:zh_cn}")
    private String supportLanguage;

    public LanguageConfig() {
    }

    public String getDefaultLanguage() {
        return this.defaultLanguage;
    }

    public List<String> getSupportLanguages() {
        return Arrays.asList(this.supportLanguage.split(","));
    }
}
