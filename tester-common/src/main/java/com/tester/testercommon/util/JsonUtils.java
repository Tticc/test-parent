
package com.tester.testercommon.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * json工具类
 * @Date 9:51 2021/3/8
 * @Author 温昌营
 **/

public final class JsonUtils {

    private JsonUtils(){

    }

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static{
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        // 如果为空则不输出
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        // 对于空的对象转json的时候不抛出错误
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 禁用序列化日期为timestamps
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 禁用遇到未知属性抛出异常
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 视空字符传为null
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        // 低层级配置
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        // 允许属性名称没有引号
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许单引号
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 取消对非ASCII字符的转码
        objectMapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, false);

        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

        SimpleModule simpleModule = new SimpleModule();
        objectMapper.registerModule(simpleModule);
    }

    /**
     * json字符串转成对象
     * @Date 9:51 2021/3/8
     * @Author 温昌营
     **/
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return fromJsonThrowable(json, clazz);
        } catch (IOException e) {
            logger.error("convert json to object exception : " , e);
        }
        return null;
    }

    /**
     * 将对象转成json字符串
     * @Date 9:52 2021/3/8
     * @Author 温昌营
     **/
    public static <T> String toJson(T src) {
        try {
            return toJsonThrowable(src);
        } catch (IOException e) {
            logger.error("convert object to json exception : " , e);
        }
        return null;
    }

    /**
     * object转换成json字符串
     * @Date 9:52 2021/3/8
     * @Author 温昌营
     **/
    public static String toJson2(Object object) {
        return JSON.toJSONString(object,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteNonStringKeyAsString);
    }

    /**
     * List对象
     * @Date 9:52 2021/3/8
     * @Author 温昌营
     **/
    public static <T> List<T> toList(String jsonStr, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonStr)) {
            return null;
        }
        return JSON.parseArray(jsonStr, clazz);
    }



    private static <T> String toJsonThrowable(T src) throws IOException {
        return objectMapper.writeValueAsString(src);
    }



    private static <T> T fromJsonThrowable(String json, Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }
}
