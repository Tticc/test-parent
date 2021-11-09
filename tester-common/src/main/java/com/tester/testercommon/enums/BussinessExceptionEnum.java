package com.tester.testercommon.enums;

import org.springframework.util.StringUtils;

/**
 *
 * @Date 2020/6/24
 * @Author 温昌营
 **/
public enum BussinessExceptionEnum {


    /************************** common - 10001xxxxx *********************************************/
    /**
     * 通用异常
     * lang.card.platform.common.dbError
     */
    ERROR(100010000L, "BussinessExceptionEnum.ERROR", "异常"),
    DB_ERROR(100010001L, "BussinessExceptionEnum.DB_ERROR", "数据库异常"),
    DATA_ERROR(100010002L, "BussinessExceptionEnum.DATA_ERROR", "数据异常，枚举异常"),
    PARAM_ERROR(100010003L, "BussinessExceptionEnum.PARAM_ERROR", "参数异常"),
    NOT_SUPPORTED_OPE(100010004L, "BussinessExceptionEnum.NOT_SUPPORTED_OPE", "不支持的操作"),

    IO_ERROR(100010011L, "BussinessExceptionEnum.IO_ERROR", "IO流读取异常"),
    HTTP_REQUEST_ERROR(100010012L, "BussinessExceptionEnum.HTTP_REQUEST_ERROR", "HTTP请求异常"),
    ;


    /**
     * 异常编码 600010001-600090001
     */
    private long code;

    /**
     * 国际化资源key
     */
    private String langKey;

    /**
     * 中文描述
     */
    private String desc;

    BussinessExceptionEnum(long code, String langKey, String desc) {
        this.code = code;
        this.langKey = langKey;
        this.desc = desc;
    }

    BussinessExceptionEnum(long code, String langKey) {
        this.code = code;
        this.langKey = langKey;
    }

    public long getCode() {
        return code;
    }

    public String getLangKey() {
        return langKey;
    }

    public String getDesc() {
        return desc;
    }

    public static void main(String[] args) {
        String common = "common";
        String order = "order";
        String point = "point";
        String member = "member";
        String payment = "payment";
        String invoice = "invoice";
        String orderGift = "orderGift";
        generateLanguageKey(ERROR,member);
//        generateCommonExceptionConfig();
//        generateChineseLanguageConfig();
    }

    private static void generateLanguageKey(BussinessExceptionEnum enums, String module){
        StringBuilder sb = new StringBuilder();
        String languageKey = "lang.card.platform."+module+"."+camelName(enums.name());
        sb.append("\r\n")
                .append("/**").append("\r\n")
                .append("* ").append(enums.getDesc()).append("\r\n")
                .append("*/").append("\r\n")
                .append("public static final String ").append(enums.name()).append(" = ").append("\"").append(languageKey).append("\";");
        System.out.println(sb.toString());
    }
    public static String camelName(String name) {
        StringBuilder result = new StringBuilder();
        if (StringUtils.isEmpty(name)) {
            return "";
        }
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel :  camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }
    /**
     * 生成异常(common-exception)配置
     */
    private static void generateCommonExceptionConfig() {
        BussinessExceptionEnum[] orderExceptionEnums = BussinessExceptionEnum.values();
        for (BussinessExceptionEnum orderExceptionEnum : orderExceptionEnums) {
            System.out.println("dubhe.exception." + orderExceptionEnum.code + " = " + orderExceptionEnum.langKey);
        }
    }

    /**
     * 生成国际化中文(zh_cn)配置
     */
    private static void generateChineseLanguageConfig() {
        BussinessExceptionEnum[] orderExceptionEnums = BussinessExceptionEnum.values();
        for (BussinessExceptionEnum orderExceptionEnum : orderExceptionEnums) {
            System.out.println(orderExceptionEnum.langKey + " = " + orderExceptionEnum.desc);
        }
    }

    public static String getLangKeyByCode(long code) {
        for (BussinessExceptionEnum exceptionEnum : BussinessExceptionEnum.values()) {
            if (exceptionEnum.getCode() == code) {
                return exceptionEnum.getLangKey();
            }
        }
        return "";
    }
}
