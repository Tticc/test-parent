package com.tester.testersearch.config.schedule;

import com.tester.testersearch.dao.domain.TradeDataBaseDomain;
import com.tester.testersearch.dao.service.TradeDataBaseService;
import com.tester.testersearch.service.okx.OkxHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 获取交易数据定时任务
 * wenc
 */
@Slf4j
@Component
public class FetchOkxDataJob {


    @Autowired
    private TradeDataBaseService tradeDataBaseService;

    // 每隔 20秒 执行一次
    @Scheduled(fixedRate = 20 * 1000)
    public void performTask() {
        try {
            List<TradeDataBaseDomain> okxKlineData = OkxHelper.getOKXKlineData("5", "1m");
            if (CollectionUtils.isEmpty(okxKlineData)) {
                return;
            }
            tradeDataBaseService.batchSave(okxKlineData);
        } catch (Exception e) {

        }
    }
}
