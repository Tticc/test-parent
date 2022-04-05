package com.tester.base.dto.exception;

/**
 * @Author 温昌营
 * @Date 2021-6-4 18:03:04
 */
public class BusinessExceptionCode {

    /**
     * 系统异常
     * lang.exception.system.error
     */
    public static final long SYSTEM_ERROR = 5000L;
    /**
     * 返回null
     * lang.exception.call.api.null
     */
    public static final long CALL_API_NULL = 5001L;
    /**
     * 服务调用异常
     * lang.exception.call.api.error
     */
    public static final long CALL_API_ERROR = 5002L;

    /**
     * 没有登录
     * lang.cloudoffice.staff.noLogin
     */
    public static final long NO_LOGIN = 11000111;


}
