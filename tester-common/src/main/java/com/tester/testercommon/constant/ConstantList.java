package com.tester.testercommon.constant;

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



    public static final String TRACE_ID_KEY = "X-B3-TraceId";




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
}
