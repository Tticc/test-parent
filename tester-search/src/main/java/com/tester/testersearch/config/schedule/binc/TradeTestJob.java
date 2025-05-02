package com.tester.testersearch.config.schedule.binc;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testersearch.service.binc.strategy.MACrossStrategy;
import com.tester.testersearch.service.binc.strategy.MACrossWithTPSLStrategy;
import com.tester.testersearch.util.BarEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 交易测试
 * wenc
 */
@Slf4j
@Component
public class TradeTestJob {

    @Autowired
    private MACrossStrategy maCrossStrategy;

    @Autowired
    private MACrossWithTPSLStrategy maCrossWithTPSLStrategy;


    @EventListener(ApplicationReadyEvent.class)
    public void runOnce() throws BusinessException {
//        maCrossStrategy.runOnce("20230101000000",5, BarEnum._30m,"20250501000000");
        maCrossWithTPSLStrategy.runOnce("20210928000000",5, BarEnum._30m,"20220301000000");
        log.info("测试完成");
    }
}
