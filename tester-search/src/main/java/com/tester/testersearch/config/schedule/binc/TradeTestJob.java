package com.tester.testersearch.config.schedule.binc;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testersearch.service.binc.strategy.MACrossWithTPSLStrategy;
import com.tester.testersearch.service.binc.strategy.TradeParam;
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
    private MACrossWithTPSLStrategy maCrossWithTPSLStrategy;


    @EventListener(ApplicationReadyEvent.class)
    public void runOnce() throws BusinessException {
        TradeParam tradeParam = new TradeParam();
        tradeParam.setSkipAfterHuge(10)
                .setKeepSkipAfterHuge(10)
                .setSlTimes(new BigDecimal("0.005"))
                .setTpTimes(new BigDecimal("0.07"))
                .setReverseSlTimes(new BigDecimal("0.005"))
                .setReverseTpTimes(new BigDecimal("0.01"))
                .setReverseSkipNum(2)
                .setReverseTakeNum(3)
                .setSkipTimes(new BigDecimal("0.012"));
        maCrossWithTPSLStrategy.runOnce("20250101000000",5, BarEnum._30m,"20260105000000", tradeParam);
        log.info("测试完成");
    }
}
