package com.tester.testercommon.constant;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @Date 15:50 2021/12/17
 * @Author 温昌营
 **/
public class ExcelConstant {
    public static final Map<String, String> EXCEL_CONTENT_TYPE_MAP = new HashMap<String, String>();

    static {
        EXCEL_CONTENT_TYPE_MAP.put(ExcelConstant.EXCEL_2003_SUFFIX, ExcelConstant.EXCEL_2003_CONTENT_TYPE);
        EXCEL_CONTENT_TYPE_MAP.put(ExcelConstant.EXCEL_2007_SUFFIX, ExcelConstant.EXCEL_2007_CONTENT_TYPE);
    }

    public static final String IMPORT_PROCESS_BEAN_PREFIX = "_import_process_";
    /**
     * excel导入解析Bean前缀
     */
    public static final String EXCEL_IMPORT_PARSE_BEAN_PREFIX = "_excel_parse_";

    public static final String EXCEL_CELL_TYPE_STRING = "string";
    public static final String EXCEL_CELL_TYPE_BOOLEAN = "boolean";
    public static final String EXCEL_CELL_TYPE_INTEGER = "integer";
    public static final String EXCEL_CELL_TYPE_LONG = "long";
    public static final String EXCEL_CELL_TYPE_DOUBLE = "double";
    public static final String EXCEL_CELL_TYPE_DATE = "date";
    public static final String EXCEL_CELL_TYPE_ENUM = "enum";
    public static final String EXCEL_CELL_TYPE_BIG_DECIMAL = "BigDecimal";

    public static final String EXCEL_2003_SUFFIX = "xls";
    public static final String EXCEL_2007_SUFFIX = "xlsx";
    public static final String EXCEL_2007_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8";
    public static final String EXCEL_2003_CONTENT_TYPE = "application/vnd.ms-excel";

    //public static final String ENUM_CELL_ERROR_VALUE_BOX_MESSAG = "lang.common.platform.excel.cellEnumValueRangeError";
    //public static final String ENUM_CELL_ERROR_VALUE_BOX_DEFAULT_MESSAG_TEXT = "错误值.";

    //校验
    /*
     * notNull: 非空校验
     * length: 长度
     * range: 数值范围校验
     * email: 邮箱地址
     * regex: 正则表达式
     * number: 数字（整数、小数）
     * integer: 整数
     * custom: 自定义校验
     */
    /**
     * 非空校验
     */
    public static final String VALIDATE_TYPE_NOT_NULL = "notNull";
    /**
     * 长度校验
     */
    public static final String VALIDATE_TYPE_LENGTH = "length";
    /**
     * 数值范围校验
     */
    public static final String VALIDATE_TYPE_RANGE = "range";
    /**
     * 邮箱格式校验
     */
    public static final String VALIDATE_TYPE_EMAIL = "email";
    /**
     * 正则校验
     */
    public static final String VALIDATE_TYPE_REGEX = "regex";
    /**
     * 数字校验
     */
    public static final String VALIDATE_TYPE_NUMBER = "number";
    /**
     * 整数校验
     */
    public static final String VALIDATE_TYPE_INTEGER = "integer";
    /**
     * 自定义校验校验
     */
    public static final String VALIDATE_TYPE_CUSTOM = "custom";
    /**
     * 重复数据校验
     */
    public static final String VALIDATE_TYPE_REPETITION = "repetition";

    //导入模式 1：同步导入、2：异步导入
    /**
     * 1：同步导入
     */
    public static final int EXCEL_MODE_SYNC = 1;
    /**
     * 2：异步导入
     */
    public static final int EXCEL_MODE_ASYNC = 2;

    //导入状态 0：初始化、1：处理中、2：全部成功、3：部分成功、4：失败
    /**
     * 0：初始化
     */
    public static final int IMPORT_STATUS_INIT = 0;
    /**
     * 1：处理中
     */
    public static final int IMPORT_STATUS_PROCESSING = 1;
    /**
     * 2：全部成功
     */
    public static final int IMPORT_STATUS_ALL_SUCCESS = 2;
    /**
     * 3：部分成功
     */
    public static final int IMPORT_STATUS_PART_SUCCESS = 3;
    /**
     * 4：失败
     */
    public static final int IMPORT_STATUS_FAILED = 4;

    /**
     * 导入根目录名
     */
    public static final String IMPORT_BASE_DIC = "excel" + File.separator + "import";
    /**
     * 导出根目录名
     */
    public static final String EXPORT_BASE_DIC = "excel" + File.separator + "export";
    /**
     * 导入错误文件根目录名
     */
    public static final String IMPORT_ERROR_BASE_DIC = "error";

    /**
     * 导入业务编码默认值
     */
    public static final String IMPORT_DEFAULT_BUSINESS_CODE = "Default";

    private static final String EXCEL_RABBIT_MQ_EXCHANGE_NAME = "excel.exchange";
    private static final String EXCEL_RABBIT_MQ_QUEUE_PREFIX = "excel.queue.";
    private static final String EXCEL_RABBIT_MQ_ROUTE_KEY_PREFIX = "excelRKey.";

    public static final String EXCEL_ROCKET_MO_TOPIC_PREFIX = "excel-";
    public static final String EXCEL_ROCKET_MO_TOPIC_SUFFIX = "-topic";
    public static final String EXCEL_ROCKET_MO_GROUP_SUFFIX = "-group";

    public static final String ASYNC_EXCEL_LISTENER_BEAN_NAME = "ASyncExcelListener";

    /**
     * 异步导入
     */
    public static final int ASYNC_EXCEL_TYPE_IMPORT = 1;
    /**
     * 异步导出
     */
    public static final int ASYNC_EXCEL_TYPE_EXPORT = 2;

    public static final String EXCEL_DATA_PROCESS_TASK_BEAN_NAME = "ExcelDataProcessTask";

    public static final String ERROR_COLUMN_TITLE = "lang.common.excel.errorColumnTitle";
    public static final String ERROR_COLUMN_DEFAULT_TITLE = "错误信息";

    /*public static String getRabbitExcelExchangeName(String environment) {
        return EXCEL_RABBIT_MQ_EXCHANGE_NAME + "-" + environment;
    }

    public static String getRabbitExcelQueueName(String environment, String appliactionName) {
        return EXCEL_RABBIT_MQ_QUEUE_PREFIX + appliactionName + "-" + environment;
    }

    public static String getRabbitExcelRouteKeyName(String environment, String appliactionName) {
        return EXCEL_RABBIT_MQ_ROUTE_KEY_PREFIX + appliactionName + "-" + environment;
    }*/
}
