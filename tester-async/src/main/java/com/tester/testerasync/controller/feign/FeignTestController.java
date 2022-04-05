package com.tester.testerasync.controller.feign;

import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.exception.BusinessException;
import com.tester.feign.client.async.Test1FeignClient;
import com.tester.testercommon.controller.BaseController;
import com.tester.base.dto.model.request.TextRequest;
import com.tester.base.dto.model.response.KVResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 温昌营
 * @Date
 */
@RestController
@Slf4j
@RequestMapping("/inner-service/api/test1")
public class FeignTestController extends BaseController implements Test1FeignClient {

    @Override
    @PostMapping("/get")
    @ApiOperation(value = "测试feign", notes = "", httpMethod = "POST")
    public RestResult<KVResponse<String, String>> get(TextRequest request) throws BusinessException {
        KVResponse<String, String> res = new KVResponse<String, String>()
                .setKey("key")
                .setValue("value")
                .setDesc("hello world");
        return success(res);
    }
}
