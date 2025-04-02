package com.tester.testersearch;

import com.github.pagehelper.PageInfo;
import com.tester.testercommon.util.DateUtil;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.dao.model.TradeDataBasePageRequest;
import com.tester.testersearch.dao.service.TradeDataBaseService;
import com.tester.testersearch.util.BarEnum;
import com.tester.testersearch.util.binance.CombineCandle;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TesterSearchApplication.class)
@Slf4j
public class BinanceTest {
    @Autowired
    private TradeDataBaseService tradeDataBaseService;

    @Test
    public void test_createIndex() throws IOException {

        LocalDateTime localDateTime = DateUtil.getLocalDateTime("20250301000000");
        Date startDate = DateUtil.getDateFromLocalDateTime(localDateTime);
        int pageSize = 600;
        Long minId = startDate.getTime();
        TradeDataBasePageRequest req = new TradeDataBasePageRequest();
        req.setId(minId);
        req.setPageNum(1);
        req.setPageSize(pageSize);
        PageInfo<TradeSignDTO> pageInfo = tradeDataBaseService.listPage(req);
        if (null != pageInfo && !CollectionUtils.isEmpty(pageInfo.getList())) {
            List<TradeSignDTO> combine = CombineCandle.combine(null, BarEnum._5m, pageInfo.getList());
        }
    }
}
