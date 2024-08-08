package com.tester.testersearch.util;

import com.tester.testersearch.model.Knowledge;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 温昌营
 * @Date 2022-10-10 11:58:53
 */
public class EsSearchHelper {
    public static Map<String, Float> fieldBoostMap = new HashMap<>();
    public static Map<String, String> fieldAnalyzerMap = new HashMap<>();
    public static Set<String> termField = new HashSet<>();
    private Knowledge knowledge;

    static {
        initBoost(fieldBoostMap);
        initAnalyzer(fieldAnalyzerMap);
        initTermField(termField);
    }

    public static List<String> listAllBoostFields(){
        return fieldBoostMap.entrySet().stream().map(e -> e.getKey()).collect(Collectors.toList());
    }

    private static void initTermField(Set<String> termField) {
        termField.add("type");
    }

    private static void initBoost(Map<String, Float> map) {
//        map.put("type", 1.0f);
//        map.put("belong", 1.0f);
        map.put("keyword", 91.0f);
        map.put("title", 81.0f);
        map.put("description", 61.0f);
        map.put("detail", 2.0f);
//        map.put("author", 1.0f);
//        map.put("priority", 1.0f);
    }

    private static void initAnalyzer(Map<String, String> map) {
        map.put("keyword", "ik_smart");
        map.put("title", "ik_smart");
        map.put("description", "ik_smart");
        map.put("detail", "ik_smart");
    }
}
