package com.tester.testercommon.constant;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Author 温昌营
 * @Date
 */
public class ConstantList {

    /**
     * 分库分表数据源
     **/
    public static final String MY_MANAGER = "transactionManger-my";

    /**
     * 非 分库分表数据源
     **/
    public static final String NORMAL_MANAGER = "transactionManger-normal";




    public static final String LOCK_DEFAULT_KEY = "'lock:test:parent:default:key'+";



    /**
     * redis 锁默认重试次数
     **/
    public static final int defaultRetryTime = 10;
    /**
     * redis 锁默认重试间隔时间(ms)
     **/
    public static final int defaultInterval = 100;
    /**
     * redis 锁默认超时时间(ms)
     **/
    public static final int defaultTimeout = 50*1000;


    // StandardCharsets.UTF_8
    public static final String UTF_8 = "UTF-8";
    public static final Charset UTF_8_STAND = StandardCharsets.UTF_8;

    public static final String GBK = "GBK";

    public static final String IOS8859_1 = "iso8859-1";
    public static final Charset IOS8859_1_STAND = StandardCharsets.ISO_8859_1;

    public static final String MDC_TRACE_ID_KEY = "X-B3-TraceId";

}
