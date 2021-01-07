package com.tester.testerstarter.language;

import lombok.Data;

import java.io.Serializable;

@Data
public class LanguageBean implements Serializable {
    private String language;
    private String key;
    private String text;
}
