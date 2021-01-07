package com.tester.testercommon.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * @Author 温昌营
 * @Date
 */
@Setter
@Getter
public class BusinessException extends Exception{
    private Object data;
    private Object[] params;
    private long exCode;
    private String exStack;
    private String exDesc;
    private ExceptionCode exceptionCode;

    public BusinessException(long exCode, String exDesc) {
        super(exDesc);
        this.setExCode(exCode);
        this.setExDesc(exDesc);
    }

    public BusinessException(long exCode, String exDesc, Throwable e) {
        super(String.valueOf(exCode), e instanceof UndeclaredThrowableException ? ((UndeclaredThrowableException)e).getUndeclaredThrowable() : e);
        if (e instanceof UndeclaredThrowableException) {
            Throwable targetThrowable = ((UndeclaredThrowableException)e).getUndeclaredThrowable();
            if (targetThrowable instanceof BusinessException) {
                BusinessException be = (BusinessException)targetThrowable;
                this.setExCode(be.getExCode());
                return;
            }
        }

        this.setExCode(exCode);
    }

    public BusinessException(long exCode, String exDesc, Object[] params) {
        super(String.valueOf(exCode));
        this.params = params;
        this.setExCode(exCode);
    }

    public BusinessException(long exCode, String exDesc, Throwable e, Object[] params) {
        super(String.valueOf(exCode), e instanceof UndeclaredThrowableException ? ((UndeclaredThrowableException)e).getUndeclaredThrowable() : e);
        if (e instanceof UndeclaredThrowableException) {
            Throwable targetThrowable = ((UndeclaredThrowableException)e).getUndeclaredThrowable();
            if (targetThrowable instanceof BusinessException) {
                BusinessException be = (BusinessException)targetThrowable;
                this.setExCode(be.getExCode());
                return;
            }
        }

        this.params = params;
        this.setExCode(exCode);
        this.setExStack(this.getStackTraceMessage(e));
    }

    public BusinessException(long exCode) {
        super(String.valueOf(exCode));
        this.setExCode(exCode);
    }

    public BusinessException(long exCode, Object[] params) {
        super(String.valueOf(exCode));
        this.setExCode(exCode);
        this.params = params;
    }

    public BusinessException(long exCode, Throwable e, Object[] params) {
        super(String.valueOf(exCode), e instanceof UndeclaredThrowableException ? ((UndeclaredThrowableException)e).getUndeclaredThrowable() : e);
        if (e instanceof UndeclaredThrowableException) {
            Throwable targetThrowable = ((UndeclaredThrowableException)e).getUndeclaredThrowable();
            if (targetThrowable instanceof BusinessException) {
                BusinessException be = (BusinessException)targetThrowable;
                this.setExCode(be.getExCode());
                return;
            }
        }

        this.setExCode(exCode);
        this.params = params;
        this.setExStack(this.getStackTraceMessage(e));
    }

    public BusinessException(long exCode, Throwable e) {
        super(String.valueOf(exCode), e instanceof UndeclaredThrowableException ? ((UndeclaredThrowableException)e).getUndeclaredThrowable() : e);
        if (e instanceof UndeclaredThrowableException) {
            Throwable targetThrowable = ((UndeclaredThrowableException)e).getUndeclaredThrowable();
            if (targetThrowable instanceof BusinessException) {
                BusinessException be = (BusinessException)targetThrowable;
                this.setExCode(be.getExCode());
                return;
            }
        }

        this.setExCode(exCode);
        this.setExStack(this.getStackTraceMessage(e));
    }




    public String getMessage() {
        String _exCode = super.getMessage();
        return _exCode;
    }

    private String getStackTraceMessage(Throwable e) {
        StringWriter sw = null;
        PrintWriter pw = null;

        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException var10) {
                }
            }

            if (pw != null) {
                pw.close();
            }

        }

        return sw.toString();
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
