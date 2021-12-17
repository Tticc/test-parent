package com.tester.testerwebapp.controller.mono;

import com.tester.testercommon.controller.BaseController;
import com.tester.testercommon.controller.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Api(tags = "content模块")
@Profile({"dev", "local", "sit"})
@RestController
@Slf4j
@RequestMapping("/content")
public class ContentController extends BaseController {

    private static String static_content = "";

    @ApiOperation(value = "update", notes = "", httpMethod = "POST")
    @PostMapping("update")
    public Mono<RestResult<Object>> updateContent(@RequestParam String content) {
        static_content = content;
        return monoSuccess(Mono.justOrEmpty(content));
    }

    @ApiOperation(value = "sync", notes = "", httpMethod = "POST")
    @PostMapping("sync")
    public Mono<RestResult<String>> sync() {
        return monoSuccess(Mono.justOrEmpty(static_content));
    }

}
