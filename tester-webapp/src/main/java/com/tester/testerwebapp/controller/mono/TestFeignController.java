package com.tester.testerwebapp.controller.mono;

import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.exception.BusinessException;
import com.tester.base.dto.model.response.KVResponse;
import com.tester.testercommon.controller.BaseController;
import com.tester.testerwebapp.service.feignAdaptor.Test1FeignClientManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @Author 温昌营
 * @Date 2022-4-5 18:19:01
 */
@Api(tags = "testfeign模块")
@RestController
@RequestMapping("/testfeign")
@Slf4j
public class TestFeignController extends BaseController {
    @Autowired
    private Test1FeignClientManager test1FeignClientManager;

    @ApiOperation(value = "get", notes = "", httpMethod = "POST")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public Mono<RestResult<KVResponse<String, String>>> get() throws BusinessException {
        log.info("controller start here.");
        return monoSuccess(test1FeignClientManager.get());
    }


}
