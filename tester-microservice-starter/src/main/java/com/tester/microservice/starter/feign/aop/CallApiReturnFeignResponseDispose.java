package com.tester.microservice.starter.feign.aop;

import com.tester.base.dto.constant.BaseConstant;
import com.tester.base.dto.exception.BusinessException;
import com.tester.base.dto.exception.BusinessExceptionCode;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CallApiReturnFeignResponseDispose extends AbstractCallApiReturnDispose {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallApiReturnFeignResponseDispose.class);

    public void dispose(Object returnObject, String apiName) throws BusinessException {
        Response response = (Response) returnObject;
        if (BaseConstant.SUCCESS_CODE != response.status()) {
            String str = null;
            StringBuilder messageBuffer = new StringBuilder();

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8));
                Throwable throwable = null;
                try {
                    while ((str = reader.readLine()) != null) {
                        messageBuffer.append(str);
                    }
                } catch (Throwable t) {
                    throwable = t;
                    throw t;
                } finally {
                    if (reader != null) {
                        if (throwable != null) {
                            try {
                                reader.close();
                            } catch (Throwable var16) {
                                throwable.addSuppressed(var16);
                            }
                        } else {
                            reader.close();
                        }
                    }

                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw new BusinessException(BusinessExceptionCode.SYSTEM_ERROR, e);
            }

            LOGGER.error("Call api error; {}, {}", response.status(), messageBuffer.toString());
            throw new BusinessException(BusinessExceptionCode.CALL_API_ERROR, new Object[]{apiName});
        }
    }
}
