package com.tester.testersearch.util.binance;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.util.okx.OkxCommon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class DetectTradeSign15_25min_anti {

    /**
     * todo 优化。将买卖信号放进蜡烛信息里，买入设置为"buy"，卖出设置为"sale"，否则设置为空
     */
    public static void createChart(List<TradeSignDTO> tradeSignList) throws BusinessException {
        if (CollectionUtils.isEmpty(tradeSignList)) {
            return;
        }
        OkxCommon.calculateAndSetMA(tradeSignList,5,10,20);

        if (tradeSignList.get(0).getTradeSign() == null) {
            OkxCommon.initTradeSignM5M20(tradeSignList);
        }

        Boolean lastBuySign = null;
        TradeSignDTO lastTradeSignDTO = null;
        for (TradeSignDTO value : tradeSignList) {
            if (Objects.equals(value.getTradeSign(), 1)) {
                lastBuySign = true;
                lastTradeSignDTO = value;
            } else if (Objects.equals(value.getTradeSign(), -1)) {
                lastBuySign = false;
                lastTradeSignDTO = value;
            }
        }
        TradeSignDTO tradeSignDTO = tradeSignList.get(tradeSignList.size() - 1);
        // 如果当前蜡烛已经出现过交易信号，立即返回（避免在同一个蜡烛内反复买卖）
        if (OkxCommon.checkIfHasTradeSign(tradeSignDTO)) {
            return;
        }
        // 默认使用最新价格作为成交价格
        // BigDecimal tradePrice = tradeSignDTO.getOpen();
        BigDecimal tradePrice = tradeSignDTO.getClose();

        // 交易信号到来
        boolean tradeSignCome = false;
        if (tradeSignDTO.getMa5().compareTo(tradeSignDTO.getMa20()) > 0 && lastBuySign) {
            // 如果上穿，且最近一次信号是buy。设置此次信号为SELL
            tradeSignDTO.setTradeSign(OkxCommon.SELL_SIGN);
            tradeSignDTO.setTradePrice(tradePrice);
            tradeSignDTO.setTradeTime(new Date());
            tradeSignCome = true;
        } else if (tradeSignDTO.getMa5().compareTo(tradeSignDTO.getMa20()) < 0 && !lastBuySign) {
            // 如果下穿，且最近一次信号是sell。设置此次信号为BUY
            tradeSignDTO.setTradeSign(OkxCommon.BUY_SIGN);
            tradeSignDTO.setTradePrice(tradePrice);
            tradeSignDTO.setTradeTime(new Date());
            tradeSignCome = true;
        } else {
            tradeSignDTO.setTradeSign(OkxCommon.NONE_SIGN);
        }
        List<TradeSignDTO> tradeList = tradeSignList.stream().filter(e -> OkxCommon.checkIfHasTradeSign(e)).collect(Collectors.toList());
        if (!tradeSignCome) {
            return;
        }
        OkxCommon.printProfits_anti(tradeSignList);
    }


}
