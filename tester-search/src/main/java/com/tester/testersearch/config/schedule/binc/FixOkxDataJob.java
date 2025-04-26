package com.tester.testersearch.config.schedule.binc;

import com.tester.testercommon.util.DateUtil;
import com.tester.testersearch.dao.domain.TradeDataBaseDomain;
import com.tester.testersearch.dao.service.TradeDataBaseService;
import com.tester.testersearch.service.binc.okx.OkxHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 修复交易数据定时任务
 * wenc
 */
@Slf4j
//@Component
public class FixOkxDataJob implements InitializingBean {


    @Autowired
    private TradeDataBaseService tradeDataBaseService;

    // 每隔 1分 执行一次
    @Scheduled(fixedRate = 60 * 1000)
    public void performTask() {
        // todo
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        long time = new Date().getTime();
        for (int i = 0; i < 10; i++) {
            List<TradeDataBaseDomain> okxKlineData = OkxHelper.getOKXKlineData("300", "1m", time, null);
            if (CollectionUtils.isEmpty(okxKlineData)) {
                continue;
            }
            tradeDataBaseService.batchSave(okxKlineData);
            time -= 290 * 60 * 1000;
        }
        Long minId = tradeDataBaseService.getMinId();
        long now = new Date().getTime();
        List<String> lackDate = new ArrayList<>();
        do {
            minId += 60 * 1000;
            TradeDataBaseDomain tradeDataBaseDomain = tradeDataBaseService.get(minId);
            if (null == tradeDataBaseDomain) {
                lackDate.add(DateUtil.dateFormat(new Date(minId)));
            }
        } while (now >= minId);
        System.out.println("lackDate = " + lackDate);

    }
}
