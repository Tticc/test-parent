package com.tester.feign.client.async;

import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.exception.BusinessException;
import com.tester.microservice.starter.constant.FeignConstant;
import com.tester.base.dto.model.request.TextRequest;
import com.tester.base.dto.model.response.KVResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Date 2022-4-5 17:52:22
 * @Author 温昌营
 **/
@ApiIgnore
@FeignClient(
        name = "tester-async",
        url = "${ms-url.tester-async:}",
        path = FeignConstant.INNER_SERVICE_URI_PREFIX + "/api/test1"
)
public interface Test1FeignClient {

    /**
     * get测试
     *
     * @Date 2022-4-5 17:52:34
     * @Author 温昌营
     **/
    @PostMapping("/get")
    RestResult<KVResponse<String, String>> get(@RequestBody @Validated TextRequest request) throws BusinessException;
}
