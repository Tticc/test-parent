package com.tester.testerwebapp.controller.common;

import com.tester.testercommon.controller.BaseController;
import com.tester.base.dto.controller.RestResult;
import com.tester.testercommon.model.response.ItemResponse;
import com.tester.testercommon.util.DateUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author 温昌营
 * @Date 2022-1-20 14:00:16
 */
@Api(tags = "DeferredResultController模块")
@RestController
@RequestMapping("/deferredResult")
@Slf4j
public class DeferredResultController extends BaseController {

    private final Map<String, DeferredResult> deferredResults = new HashMap<>();


    @RequestMapping(value = "/wait", method = RequestMethod.POST)
    public DeferredResult<RestResult<ItemResponse>> pollNotification() {

        DeferredResult<RestResult<ItemResponse>> result = new DeferredResult<>(60 * 1000L, fail(500L, "timeout"));
        result.onTimeout(() -> System.out.println("Apollo.LongPoll.TimeOutKeys"));
        System.out.println("Apollo.LongPoll.RegisteredKeys");
        Date date = new Date();
        String s = DateUtil.dateFormat(date);

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                ItemResponse itemResponse = new ItemResponse();
                Date date1 = new Date();
                String s1 = DateUtil.dateFormat(date1);
                itemResponse.setGiftCode("GC001")
                        .setItemCode("GC001")
                        .setOrderItemNo(1)
                        .setSkuName(s + " - " + s1);
                result.setResult(success(itemResponse));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        return result;
    }
}
