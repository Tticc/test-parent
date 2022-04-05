package com.tester.testerasync.controller;

import com.tester.testerasync.service.AsyncMethodManager;
import com.tester.testercommon.controller.BaseController;
import com.tester.base.dto.controller.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @Author 温昌营
 * @Date
 */
@RestController
@Slf4j
@RequestMapping("/async")
public class AsyncDemoController extends BaseController {
    @Autowired
    private AsyncMethodManager asyncMethodManager;

    @PostMapping("sync")
    public Mono<RestResult<String>> sync() {
        asyncMethodManager.test_async();
        return monoSuccess();
    }
}
