package com.tester.testerstarter.exception;

import com.tester.testercommon.controller.RestResult;
import com.tester.testercommon.exception.BusinessException;
import com.tester.testercommon.exception.ExceptionCode;
import com.tester.testerstarter.language.LanguageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Iterator;
import java.util.List;

@ControllerAdvice
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BusinessExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(BusinessExceptionHandler.class);

    @Autowired
    private LanguageUtil languageUtil;


    public BusinessExceptionHandler() {
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public RestResult handleException(Exception exception) {
        BusinessException businessException = new BusinessException(ExceptionCode.SYSTEM_EXCEPTION, exception);
        LOG.error(businessException.getMessage(), exception);
        RestResult baseResult = new RestResult();
        baseResult.code(businessException.getExCode()).message(businessException.getExDesc());
        return baseResult;
    }

    /**
     * 未捕获异常
     * @param exception
     * @return com.tester.testercommon.controller.RestResult
     * @Date 16:44 2021/1/7
     * @Author 温昌营
     **/
    @ExceptionHandler({UndeclaredThrowableException.class})
    @ResponseBody
    public RestResult handleUndeclaredThrowableException(UndeclaredThrowableException exception) {
        Throwable ex = exception.getUndeclaredThrowable();
        return ex instanceof BusinessException ? this.handleBusinessException((BusinessException)ex) : this.handleException((Exception)ex);
    }

    /**
     * 方法参数校验异常
     * <br/>例如@NotNull校验没有通过
     * @param ex
     * @return com.tester.testercommon.controller.RestResult
     * @Date 16:43 2021/1/7
     * @Author 温昌营
     **/
    @ExceptionHandler({MethodArgumentNotValidException.class, MissingServletRequestParameterException.class})
    @ResponseBody
    public RestResult handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        LOG.error(ex.getMessage());
        String errorMessage = "";
        String br = "\n";
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        ObjectError objectError;
        if (!CollectionUtils.isEmpty(errors)) {
            for(Iterator var5 = errors.iterator(); var5.hasNext(); errorMessage = errorMessage + this.languageUtil.getText(objectError.getDefaultMessage()) + br) {
                objectError = (ObjectError)var5.next();
            }
        }

        if (StringUtils.isEmpty(errorMessage)) {
            errorMessage = "lang.common.platform.requestParamError";
        } else if (errorMessage.endsWith(br)) {
            errorMessage = errorMessage.substring(0, errorMessage.length() - br.length());
        }

        RestResult baseResult = new RestResult();
        baseResult.code(ExceptionCode.ERROR_INPUT_DATA).message(errorMessage);
        return baseResult;
    }

    /**
     * 请求方法异常
     * @param ex
     * @return com.tester.testercommon.controller.RestResult
     * @Date 16:43 2021/1/7
     * @Author 温昌营
     **/
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class, HttpMediaTypeNotAcceptableException.class, HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public RestResult handleHttpMediaTypeNotSupportedException(Exception ex) {
        BusinessException businessException = new BusinessException(ExceptionCode.BAD_REQUEST_MEDIA_TYPE, ex);
        RestResult baseResult = new RestResult();
        baseResult.code(businessException.getExCode()).message(businessException.getExDesc());
        LOG.error(ex.getMessage(), ex);
        return baseResult;
    }

    /**
     * 文件上传异常
     * @param ex
     * @return com.tester.testercommon.controller.RestResult
     * @Date 16:42 2021/1/7
     * @Author 温昌营
     **/
    @ExceptionHandler({MultipartException.class})
    @ResponseBody
    public RestResult handleMultipartException(Exception ex) {
        LOG.error(ex.getMessage());
        BusinessException businessException = new BusinessException(ExceptionCode.PARAM_BIND_EXCEPTION, ex);
        RestResult baseResult = new RestResult();
        baseResult.code(businessException.getExCode()).message(businessException.getExDesc());
        return baseResult;
    }

    /**
     * 参数绑定异常
     * @param ex
     * @return com.tester.testercommon.controller.RestResult
     * @Date 16:42 2021/1/7
     * @Author 温昌营
     **/
    @ExceptionHandler({BindException.class})
    @ResponseBody
    public RestResult handleBindException(BindException ex) {
        String errorMesssage = null;
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        ObjectError objectError;
        if (!CollectionUtils.isEmpty(errors)) {
            for(Iterator var4 = errors.iterator(); var4.hasNext(); errorMesssage = errorMesssage + this.languageUtil.getText(objectError.getDefaultMessage()) + "\n") {
                objectError = (ObjectError)var4.next();
            }
        }
        RestResult baseResult = new RestResult();
        if (null != errorMesssage) {
            baseResult.code(ExceptionCode.BAD_REQUEST_PARAMS).message(errorMesssage);
        } else {
            BusinessException be = new BusinessException(ExceptionCode.PARAM_BIND_EXCEPTION);
            baseResult.code(be.getExCode()).message(be.getExDesc());
        }
        LOG.error(ex.getMessage(), ex);
        return baseResult;
    }

    /**
     * 参数异常
     * @param ex
     * @return com.tester.testercommon.controller.RestResult
     * @Date 16:41 2021/1/7
     * @Author 温昌营
     **/
    @ExceptionHandler({BusinessException.class})
    @ResponseBody
    public RestResult handleBusinessException(BusinessException ex) {
        if (ex.getExCode() == ExceptionCode.SYSTEM_EXCEPTION) {
            LOG.error(ex.getMessage(), ex);
        } else {
            LOG.warn(ex.getMessage());
        }

        RestResult baseResult = new RestResult();
        baseResult.code(ex.getExCode()).message(ex.getExDesc(), ex.getParams());
        baseResult.setData(ex.getData());
        return baseResult;
    }

    /**
     * 业务异常
     * @param ex
     * @return com.tester.testercommon.controller.RestResult
     * @Date 16:41 2021/1/7
     * @Author 温昌营
     **/
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    @ResponseBody
    public RestResult handlerErrorInput(Exception ex) {
        LOG.warn(ex.getMessage());
        BusinessException businessException = new BusinessException(ExceptionCode.ERROR_INPUT_DATA, ex);
        RestResult baseResult = new RestResult();
        baseResult.code(businessException.getExCode()).message(businessException.getExDesc());
        return baseResult;
    }

    /**
     * 参数校验不通过
     * <br/>注：暂时不知道什么情况下会抛出这个异常
     * @param ex
     * @return com.tester.testercommon.controller.RestResult
     * @Date 16:40 2021/1/7
     * @Author 温昌营
     **/
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public RestResult ConstraintViolationExceptionHandler(ConstraintViolationException ex) {
        LOG.warn(ex.getMessage());
        BusinessException businessException = new BusinessException(ExceptionCode.BAD_REQUEST_PARAMS, ex);
        RestResult baseResult = new RestResult();
        baseResult.code(businessException.getExCode()).message(businessException.getExDesc());
        return baseResult;
    }
}
