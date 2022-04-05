package com.tester.testerwebapp.service.feignAdaptor;

import com.tester.base.dto.controller.RestResult;
import com.tester.base.dto.exception.BusinessException;
import com.tester.base.dto.model.request.TextRequest;
import com.tester.base.dto.model.response.KVResponse;
import com.tester.feign.client.async.Test1FeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Service
@Slf4j
public class Test1FeignClientManager {

    @Resource
    private Test1FeignClient test1FeignClient;

    public Mono<KVResponse<String, String>> get() throws BusinessException {
        TextRequest textRequest = new TextRequest();
        KVResponse<String, String> data = test1FeignClient.get(textRequest).getData();
        Mono<KVResponse<String, String>> res = Mono.justOrEmpty(data);
        return res;
    }
}
