package com.tester.microservice.starter.feign.aop;

import com.tester.base.dto.constant.BaseConstant;
import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallApiReturnRestResultDispose extends AbstractCallApiReturnDispose {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallApiReturnRestResultDispose.class);

    public void dispose(Object returnObject, String apiName) throws BusinessException {
        RestResult restResult = (RestResult) returnObject;
        if (restResult.getCode() != BaseConstant.SUCCESS_CODE) {
            LOGGER.warn("Call api returned error; {}, {}, {}", new Object[]{apiName, restResult.getCode(), restResult.getMessage()});
            BusinessException businessException = new BusinessException(restResult.getCode());
            businessException.setExDesc(restResult.getMessage());
            Object data = restResult.getData();
            if (null != data) {
                businessException.setData(data);
            }
            throw businessException;
        }
    }
}
