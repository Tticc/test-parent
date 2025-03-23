package com.tester.testersearch.util.okx;

import com.tester.base.dto.exception.BusinessException;
import com.tester.testercommon.util.BeanCopyUtil;
import com.tester.testersearch.dao.domain.TradeDataBaseDomain;
import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.service.okx.OkxHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class DetectTradeSign15_20min {
    private static Map<Long, TradeSignDTO> dataInfoList = new LinkedHashMap<>();


    public static void main(String[] args) {
        String bar = "15m";
        System.out.println("bar = " + bar);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                createChart(bar);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 5, 10, TimeUnit.SECONDS);
    }

    public static Map<Long, TradeSignDTO> getOKXKlineData(String bar) {
        Map<Long, TradeSignDTO> res = new LinkedHashMap<>();
        List<TradeDataBaseDomain> okxKlineData;
        if (CollectionUtils.isEmpty(dataInfoList)) {
            okxKlineData = OkxHelper.getOKXKlineData("40", bar);
        } else {
            okxKlineData = OkxHelper.getOKXKlineData("2", bar);
        }
        for (TradeDataBaseDomain value : okxKlineData) {
            TradeSignDTO tradeSignDTO = new TradeSignDTO();
            TradeSignDTO oriData = dataInfoList.get(value.getTimestamp());
            if (null != oriData) {
                BeanCopyUtil.copyPropertiesIgnoreNull(oriData, tradeSignDTO);
            }
            BeanCopyUtil.copyPropertiesIgnoreNull(value, tradeSignDTO);
            dataInfoList.put(value.getTimestamp(), tradeSignDTO);
        }
        return res;
    }

    /**
     * todo 优化。将买卖信号放进蜡烛信息里，买入设置为"buy"，卖出设置为"sale"，否则设置为空
     */
    private static void createChart(String bar) throws BusinessException {
        getOKXKlineData(bar);
        if (CollectionUtils.isEmpty(dataInfoList)) {
            return;
        }
        List<TradeSignDTO> tradeSignList = dataInfoList.values().stream().collect(Collectors.toList());
        OkxCommon.calculateAndSetMA(tradeSignList);

        if (tradeSignList.get(0).getTradeSign() == null) {
            OkxCommon.initTradeSignM5M20(tradeSignList);
        }

        Boolean lastBuySign = null;
        for (TradeSignDTO value : tradeSignList) {
            if (Objects.equals(value.getTradeSign(), 1)) {
                lastBuySign = true;
            } else if (Objects.equals(value.getTradeSign(), -1)) {
                lastBuySign = false;
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
        if (tradeSignDTO.getMa5().compareTo(tradeSignDTO.getMa20()) > 0 && !lastBuySign) {
            // 如果上穿，且最近一次信号是sell。设置此次信号为BUY
            tradeSignDTO.setTradeSign(OkxCommon.BUY_SIGN);
            tradeSignDTO.setTradePrice(tradePrice);
            tradeSignDTO.setTradeTime(new Date());
            tradeSignCome = true;
        } else if (tradeSignDTO.getMa5().compareTo(tradeSignDTO.getMa20()) < 0 && lastBuySign) {
            // 如果下穿，且最近一次信号是buy。设置此次信号为SELL
            tradeSignDTO.setTradeSign(OkxCommon.SELL_SIGN);
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
        OkxCommon.printProfits(tradeSignList);
    }


}
