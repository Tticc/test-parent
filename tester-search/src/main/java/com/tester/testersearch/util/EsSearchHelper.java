package com.tester.testersearch.util;

import com.tester.testersearch.model.Knowledge;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 温昌营
 * @Date 2022-10-10 11:58:53
 */
public class EsSearchHelper {
    public static Map<String, Float> fieldBoostMap = new HashMap<>();
    public static Map<String, String> fieldAnalyzerMap = new HashMap<>();
    private Knowledge knowledge;

    static {
        initBoost(fieldBoostMap);
        initAnalyzer(fieldAnalyzerMap);
    }

    private static void initBoost(Map<String, Float> map) {
        map.put("type", 1.0f);
        map.put("belong", 1.0f);
        map.put("keyword", 91.0f);
        map.put("title", 81.0f);
        map.put("description", 61.0f);
        map.put("detail", 2.0f);
        map.put("author", 1.0f);
        map.put("priority", 1.0f);
    }

    private static void initAnalyzer(Map<String, String> map) {
        map.put("keyword", "ik_max_word");
        map.put("title", "ik_max_word");
        map.put("description", "ik_max_word");
        map.put("detail", "ik_smart");
    }
}
