package com.tester.testersearch.config.schedule.binc;

import com.github.pagehelper.PageInfo;
import com.tester.base.dto.exception.BusinessException;
import com.tester.testersearch.dao.domain.TradeCandleDataDomain;
import com.tester.testersearch.dao.model.TradeCandleDataPageRequest;
import com.tester.testersearch.dao.service.TradeCandleDataService;
import com.tester.testersearch.service.binc.strategy.MACrossWithTPSLStrategy;
import com.tester.testersearch.service.binc.strategy.TradeParam;
import com.tester.testersearch.util.BKeyEnum;
import com.tester.testersearch.util.BarEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;

/**
 * 测试
 * wenc
 */
@Slf4j
@Component
public class Testing {

    @Autowired
    private TradeCandleDataService tradeCandleDataService;

    @EventListener(ApplicationReadyEvent.class)
    public void runOnce() throws BusinessException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("测试开始");
        TradeCandleDataPageRequest request = new TradeCandleDataPageRequest();
//        request.setBKey(BKeyEnum.BTCUSDT.getCode());
        request.setBKey(BKeyEnum.ETHUSDT.getCode());
        request.setBar(BarEnum._30m.getCode());
        PageInfo<TradeCandleDataDomain> tradeCandleDataDomainPageInfo = tradeCandleDataService.listPage(request);
        System.out.println("tradeCandleDataDomainPageInfo = " + tradeCandleDataDomainPageInfo);
        stopWatch.stop();
        log.info("测试完成。耗时：{}", stopWatch.prettyPrint());
    }
}
