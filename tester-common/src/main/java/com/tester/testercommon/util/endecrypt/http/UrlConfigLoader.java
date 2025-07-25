package com.tester.testercommon.util.endecrypt.http;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UrlConfigLoader {

    private UrlConfigLoader() {
    }

    private static final Map<String, Map<String, String>> CONFIG_MAP;

    static {
        try (InputStream inputStream = UrlConfigLoader.class.getResourceAsStream("/url.yaml")) {
            if (inputStream == null) {
                // 初始化一个默认值
                CONFIG_MAP = new HashMap<>();
                initServiceUrl(CONFIG_MAP);
                // throw new IllegalStateException("Failed to load url.yaml resource");
            } else {
                Yaml yaml = new Yaml();
                CONFIG_MAP = yaml.load(inputStream);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading URL configuration", e);
        }
    }

    private static void initServiceUrl(Map<String, Map<String, String>> configMap) {
        // 接口映射map
        HashMap<String, String> serviceMap = new HashMap<>();
        serviceMap.put("search", "/api/search/search");
        // 服务映射map
        configMap.put("tester-search", serviceMap);
    }

    public static String getUrl(String service, String method) {
        if (CONFIG_MAP != null && CONFIG_MAP.containsKey(service)) {
            Map<String, String> methods = CONFIG_MAP.get(service);
            if (methods != null && methods.containsKey(method)) {
                return methods.get(method);
            }
        }
        return null;
    }

}