package com.tester.testercommon.controller;

import java.io.Serializable;

/**
 * @Author 温昌营
 * @Date
 */
public class RestResult<T> implements Serializable {
    // "状态码；200：成功， 非200：失败"
    protected long code = 200L;
    // "响应消息"
    protected String message = "执行成功！";
    // "时间戳"
    protected Long timestamp = System.currentTimeMillis();
    // "返回数据"
    protected T data;


    public RestResult() {
    }

    public long getCode() {
        return this.code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public RestResult<T> code(long code) {
        this.code = code;
        return this;
    }

    public RestResult<T> message(String message) {
        this.message = message;
        return this;
    }

    public RestResult<T> putTimestamp() {
        this.timestamp = System.currentTimeMillis();
        return this;
    }

    public RestResult<T> data(T data) {
        this.data = data;
        return this;
    }
}
