package com.tester.testerstarter.language;

import com.tester.testercommon.exception.ExceptionCode;
import com.tester.testerstarter.config.LanguageConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 温昌营
 * @Date 2021-1-7 16:04:39
 */
@Component
public class LanguageUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(LanguageUtil.class);
    @Autowired
    private LanguageConfig languageConfig;
    private static final Map<String, Map<String, LanguageBean>> LANGUAGE_RESOURCE_CACHE_MAP = new HashMap();
    private static final ThreadLocal<String> LANGUAGE_LOCAL = new ThreadLocal();

    public LanguageUtil() {
    }

    public void setLanguageLocal(String language) {
        LANGUAGE_LOCAL.set(language);
    }

    public void removeLanguageLocal() {
        LANGUAGE_LOCAL.remove();
    }

    public String getCurrentLanguage() {
        String language = (String)LANGUAGE_LOCAL.get();
        if (language == null || "".equals(language.trim())) {
            language = this.languageConfig.getDefaultLanguage();
        }

        return language;
    }

    public synchronized void putLanguage(String language, Map<String, LanguageBean> languageBeanMap) {
        Map<String, LanguageBean> oldLanguageBeanMap = (Map)LANGUAGE_RESOURCE_CACHE_MAP.get(language);
        if (oldLanguageBeanMap == null) {
            oldLanguageBeanMap = new HashMap();
            LANGUAGE_RESOURCE_CACHE_MAP.put(language, oldLanguageBeanMap);
        }

        ((Map)oldLanguageBeanMap).putAll(languageBeanMap);
    }

    public void removeLanguage(String language, String key) {
        Map<String, LanguageBean> languageBeanMap = (Map)LANGUAGE_RESOURCE_CACHE_MAP.get(language);
        if (languageBeanMap != null) {
            languageBeanMap.remove(key);
        }

    }

    public String getExceptionText(long exCode) {
        return this.getExceptionText(exCode, String.valueOf(exCode));
    }

    public String getExceptionText(long exCode, String defaultText) {
        String exDesc;
        if (ExceptionCode.EX_MAP != null) {
            exDesc = (String)ExceptionCode.EX_MAP.get(String.valueOf(exCode));
        } else {
            exDesc = String.valueOf(exCode);
        }

        return this.getText(exDesc, defaultText);
    }

    public String getExceptionText(long exCode, Object[] params) {
        String text = this.getExceptionText(exCode);
        return MessageFormat.format(text, params);
    }

    public String getText(String key) {
        return this.getText(key, key);
    }

    public String getText(String key, String defaultText) {
        return this.getText(this.getCurrentLanguage(), key, defaultText);
    }

    public String getText(String key, String defaultText, Object[] params) {
        String text = this.getText(this.getCurrentLanguage(), key, defaultText);
        if (params != null) {
            text = MessageFormat.format(text, params);
        }

        return text;
    }

    public String getText(String key, Object[] params) {
        String text = this.getText(key);
        if (params != null) {
            text = MessageFormat.format(text, params);
        }

        return text;
    }

    public String getText(String language, String key, String defaultText) {
        Map<String, LanguageBean> languageBeanMap = (Map)LANGUAGE_RESOURCE_CACHE_MAP.get(language);
        if (languageBeanMap == null) {
            LOGGER.warn("Not found language resource; {}", language);
            return defaultText;
        } else {
            LanguageBean languageBean = (LanguageBean)languageBeanMap.get(key);
            if (languageBean == null) {
                LOGGER.warn("Not found language key; {}, {}", language, key);
                return defaultText;
            } else {
                String text = languageBean.getText();
                if (text != null && !"".equals(text.trim())) {
                    return text;
                } else {
                    LOGGER.debug("The language key was empty; {}, {}", language, key);
                    return defaultText;
                }
            }
        }
    }

    public LanguageBean getLanguageBean(String key, String language) {
        language = this.getLanguage(language);
        Map<String, LanguageBean> languageBeanMap = (Map)LANGUAGE_RESOURCE_CACHE_MAP.get(language);
        if (languageBeanMap == null) {
            LOGGER.warn("Not found language resource; {}", language);
            return null;
        } else {
            LanguageBean languageBean = (LanguageBean)languageBeanMap.get(key);
            if (languageBean == null) {
                LOGGER.warn("Not found language key; {}, {}", language, key);
                return null;
            } else {
                return languageBean;
            }
        }
    }

    public Map<String, LanguageBean> getLanguageBeanMap(String language) {
        return (Map)LANGUAGE_RESOURCE_CACHE_MAP.get(language);
    }

    public String getLanguage(String language) {
        if (language == null || "".equals(language.trim())) {
            language = this.languageConfig.getDefaultLanguage();
        }

        language = language.toLowerCase();
        if (!this.languageConfig.getSupportLanguages().contains(language)) {
            LOGGER.debug("Application no support this language; {}", language);
            language = this.languageConfig.getDefaultLanguage();
        }

        return language;
    }
}
