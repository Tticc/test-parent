package com.tester.testersearch.config.schedule.binc;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testersearch.service.binc.strategy.MACrossWithTPSLStrategy;
import com.tester.testersearch.service.binc.strategy.TradeParam;
import com.tester.testersearch.util.BKeyEnum;
import com.tester.testersearch.util.StrategyEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 交易测试
 * wenc
 */
@Slf4j
//@Component
public class TradeTestJob {

    @Autowired
    private MACrossWithTPSLStrategy maCrossWithTPSLStrategy;

    @EventListener(ApplicationReadyEvent.class)
    public void runOnce() throws BusinessException, InterruptedException {
        List<StrategyEnum> strategyEnums = Arrays.asList(
//                StrategyEnum._1000310,
                StrategyEnum._1000151
        );
        for (StrategyEnum strategyEnum : strategyEnums) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("测试开始");
            TradeParam tradeParam = strategyEnum.getParam();
            tradeParam.setNeedSave(true)
                    .setFirst(true)
                    .setBKey(BKeyEnum.BTCUSDT.getCode());
//            maCrossWithTPSLStrategy.runOnce("20200101000000",  "20250122000000", tradeParam);
            maCrossWithTPSLStrategy.runOnce("20200101000000",  "20250516000000", tradeParam);
            stopWatch.stop();
            log.info("测试完成。耗时：{}", stopWatch.prettyPrint());
            System.out.println("strategy = " + tradeParam.getStrategyCode());
            TimeUnit.MINUTES.sleep(1);
        }
    }
}
