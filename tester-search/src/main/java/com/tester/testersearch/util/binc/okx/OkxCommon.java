package com.tester.testersearch.util.binc.okx;

import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.util.binc.tradeSign.TradeSignEnum;

import java.util.*;

public class OkxCommon {

    /**
     * 检查是否为交易信号
     * @param tradeSignDTO
     * @return
     */
    public static boolean checkIfHasTradeSign(TradeSignDTO tradeSignDTO) {
        return checkIfHasMATradeSign(tradeSignDTO) || checkIfHasTPLSTradeSign(tradeSignDTO);
    }

    /**
     * 检查是否为MA交易信号
     * @param tradeSignDTO
     * @return
     */
    public static boolean checkIfHasMATradeSign(TradeSignDTO tradeSignDTO) {
        return checkIfMABuySign(tradeSignDTO) || checkIfMASellSign(tradeSignDTO);
    }

    /**
     * 检查是否为止盈止损交易信号
     * @param tradeSignDTO
     * @return
     */
    public static boolean checkIfHasTPLSTradeSign(TradeSignDTO tradeSignDTO) {
        return checkIfTPSLBuySign(tradeSignDTO) || checkIfTPSLSellSign(tradeSignDTO);
    }

    /**
     * 检查是否为buy交易信号
     * @param tradeSignDTO
     * @return
     */
    public static boolean checkIfBuySign(TradeSignDTO tradeSignDTO) {
        return Objects.equals(tradeSignDTO.getTradeSign(), TradeSignEnum.BUY_SIGN.getCode())
                || Objects.equals(tradeSignDTO.getTradeSign(), TradeSignEnum.SL_BUY_SIGN.getCode())
                || Objects.equals(tradeSignDTO.getTradeSign(), TradeSignEnum.TP_BUY_SIGN.getCode());
    }

    /**
     * 检查是否为sell交易信号
     * @param tradeSignDTO
     * @return
     */
    public static boolean checkIfSellSign(TradeSignDTO tradeSignDTO) {
        return Objects.equals(tradeSignDTO.getTradeSign(), TradeSignEnum.SELL_SIGN.getCode())
                || Objects.equals(tradeSignDTO.getTradeSign(), TradeSignEnum.SL_SELL_SIGN.getCode())
                || Objects.equals(tradeSignDTO.getTradeSign(), TradeSignEnum.TP_SELL_SIGN.getCode());
    }

    /**
     * 检查是否为MA buy交易信号
     * @param tradeSignDTO
     * @return
     */
    public static boolean checkIfMABuySign(TradeSignDTO tradeSignDTO) {
        return Objects.equals(tradeSignDTO.getTradeSign(), TradeSignEnum.BUY_SIGN.getCode());
    }

    /**
     * 检查是否为MA sell交易信号
     * @param tradeSignDTO
     * @return
     */
    public static boolean checkIfMASellSign(TradeSignDTO tradeSignDTO) {
        return Objects.equals(tradeSignDTO.getTradeSign(), TradeSignEnum.SELL_SIGN.getCode());
    }

    /**
     * 检查是否为止盈止损buy交易信号
     * @param tradeSignDTO
     * @return
     */
    public static boolean checkIfTPSLBuySign(TradeSignDTO tradeSignDTO) {
        return Objects.equals(tradeSignDTO.getTradeSign(), TradeSignEnum.TP_BUY_SIGN.getCode())
                || Objects.equals(tradeSignDTO.getTradeSign(), TradeSignEnum.SL_BUY_SIGN.getCode());
    }

    /**
     * 检查是否为止盈止损sell交易信号
     * @param tradeSignDTO
     * @return
     */
    public static boolean checkIfTPSLSellSign(TradeSignDTO tradeSignDTO) {
        return Objects.equals(tradeSignDTO.getTradeSign(), TradeSignEnum.SL_SELL_SIGN.getCode())
                || Objects.equals(tradeSignDTO.getTradeSign(), TradeSignEnum.TP_SELL_SIGN.getCode());
    }


}
