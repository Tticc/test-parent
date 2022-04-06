package com.tester.microservice.starter.feign.aop;

import com.tester.base.dto.exception.BusinessException;
import com.tester.base.dto.exception.BusinessExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallApiReturnNullDispose extends AbstractCallApiReturnDispose {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallApiReturnNullDispose.class);


    public void dispose(Object returnObject, String apiName) throws BusinessException {
        LOGGER.warn("Call api returned null; {}", apiName);
        throw new BusinessException(BusinessExceptionCode.CALL_API_NULL, new Object[]{apiName});
    }
}
