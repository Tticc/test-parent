package com.tester.base.dto.exception;

import java.util.Map;

/**
 * @Author 温昌营
 * @Date
 */
public class ExceptionCode {
    public static final long REJECT_JOIN_SECRET_KEY_ERROR = 1001L;
    public static final long BAD_REQUEST_PARAMS = 3000L;
    public static final long ERROR_INPUT_DATA = 3001L;
    public static final long DEVELOPMENT_ENVIRONMENT_ONLY = 3001L;
    public static final long SYSTEM_EXCEPTION = 5000L;
    public static final long CALL_API_RETURN_NULL = 5001L;
    public static final long CALL_API_EXCEPTION = 5002L;
    public static final long SERVICE_URL_NOT_FOUND = 5003L;
    public static final long BAD_REQUEST_MEDIA_TYPE = 5004L;
    public static final long PARAM_BIND_EXCEPTION = 5005L;
    public static final long PARSE_JSON_EXCEPTION = 5006L;
    public static final long BATCH_TASK_TIMEOUT = 5007L;
    public static final long GET_LOCK_FILED = 5008L;
    public static final long REJECT_VISIT = 5009L;
    public static final long NETWORK_EXCEPTION = 5010L;
    public static Map<String, String> EX_MAP;
    private Map<String, String> exception;

    public ExceptionCode() {
    }

    public void setException(Map<String, String> exception) {
        EX_MAP = exception;
        this.exception = exception;
    }

    public Map<String, String> getException() {
        return this.exception;
    }

}
