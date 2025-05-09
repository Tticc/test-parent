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

import java.math.BigDecimal;

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
        int skipAfterHuge = 10;
        int keepSkipAfterHuge = 10;
        BigDecimal skipTimes = new BigDecimal("0.012");
        BigDecimal slTimes = new BigDecimal("0.01");
        BigDecimal tpTimes = new BigDecimal("0.07");
        maCrossWithTPSLStrategy.runOnce("20250101000000",5, BarEnum._30m,"20260105000000", skipAfterHuge, keepSkipAfterHuge, skipTimes ,slTimes, tpTimes);
        log.info("测试完成");
    }
}
