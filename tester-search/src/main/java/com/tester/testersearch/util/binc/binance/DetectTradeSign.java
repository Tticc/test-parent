package com.tester.testersearch.util.binc.binance;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.util.binc.okx.OkxCommon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DetectTradeSign {

    /**
     * 买入卖出信号有入参传入，这里只做计算用
     * @author wenc
     */
    public static void createChart(List<TradeSignDTO> tradeSignList) throws BusinessException {
        if (CollectionUtils.isEmpty(tradeSignList)) {
            return;
        }
        TradeSignDTO last = tradeSignList.get(tradeSignList.size() - 1);
        if(!Objects.equals(last.getTradeSign(), OkxCommon.SELL_SIGN) && !Objects.equals(last.getTradeSign(), OkxCommon.BUY_SIGN)){
            return;
        }
        List<TradeSignDTO> tradeList = tradeSignList.stream().filter(e -> OkxCommon.checkIfHasTradeSign(e)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tradeList)) {
            return;
        }
        OkxCommon.printProfits(tradeSignList);
    }
}
