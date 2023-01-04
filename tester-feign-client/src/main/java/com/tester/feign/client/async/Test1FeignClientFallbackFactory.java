package com.tester.feign.client.async;

import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.exception.BusinessException;
import com.tester.base.dto.model.request.TextRequest;
import com.tester.base.dto.model.response.KVResponse;
import feign.hystrix.FallbackFactory;

public class Test1FeignClientFallbackFactory implements FallbackFactory<Test1FeignClient> {
    @Override
    public Test1FeignClient create(Throwable throwable) {
        return new Test1FeignClient() {
            @Override
            public RestResult<KVResponse<String, String>> get(TextRequest request) throws BusinessException {
                return null;
            }
        };
    }
}
