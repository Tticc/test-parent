package com.tester.testersearch.util.binc.okx;

import com.tester.testersearch.dao.domain.TradeSignDTO;
import com.tester.testersearch.util.binc.tradeSign.TradeSignEnum;

import java.util.*;

public class OkxCommon {

    /**
     * 检查是否为交易信号
     * @param tradeInfo
     * @return
     */
    public static boolean checkIfHasTradeSign(TradeSignDTO.TradeInfo tradeInfo) {
        if(null == tradeInfo){
            return false;
        }
        return checkIfHasMATradeSign(tradeInfo) || checkIfHasTPLSTradeSign(tradeInfo);
    }

    /**
     * 检查是否为MA交易信号
     * @param tradeInfo
     * @return
     */
    public static boolean checkIfHasMATradeSign(TradeSignDTO.TradeInfo tradeInfo) {
        if(null == tradeInfo){
            return false;
        }
        return checkIfMABuySign(tradeInfo) || checkIfMASellSign(tradeInfo);
    }

    /**
     * 检查是否为止盈止损交易信号
     * @param tradeInfo
     * @return
     */
    public static boolean checkIfHasTPLSTradeSign(TradeSignDTO.TradeInfo tradeInfo) {
        if(null == tradeInfo){
            return false;
        }
        return checkIfTPSLBuySign(tradeInfo) || checkIfTPSLSellSign(tradeInfo);
    }

    /**
     * 检查是否为buy交易信号
     * @param tradeInfo
     * @return
     */
    public static boolean checkIfBuySign(TradeSignDTO.TradeInfo tradeInfo) {
        if(null == tradeInfo){
            return false;
        }
        return Objects.equals(tradeInfo.getTradeSign(), TradeSignEnum.BUY_SIGN.getCode())
                || Objects.equals(tradeInfo.getTradeSign(), TradeSignEnum.SL_BUY_SIGN.getCode())
                || Objects.equals(tradeInfo.getTradeSign(), TradeSignEnum.TP_BUY_SIGN.getCode());
    }

    /**
     * 检查是否为sell交易信号
     * @param tradeInfo
     * @return
     */
    public static boolean checkIfSellSign(TradeSignDTO.TradeInfo tradeInfo) {
        if(null == tradeInfo){
            return false;
        }
        return Objects.equals(tradeInfo.getTradeSign(), TradeSignEnum.SELL_SIGN.getCode())
                || Objects.equals(tradeInfo.getTradeSign(), TradeSignEnum.SL_SELL_SIGN.getCode())
                || Objects.equals(tradeInfo.getTradeSign(), TradeSignEnum.TP_SELL_SIGN.getCode());
    }

    /**
     * 检查是否为MA buy交易信号
     * @param tradeInfo
     * @return
     */
    public static boolean checkIfMABuySign(TradeSignDTO.TradeInfo tradeInfo) {
        if(null == tradeInfo){
            return false;
        }
        return Objects.equals(tradeInfo.getTradeSign(), TradeSignEnum.BUY_SIGN.getCode());
    }

    /**
     * 检查是否为MA sell交易信号
     * @param tradeInfo
     * @return
     */
    public static boolean checkIfMASellSign(TradeSignDTO.TradeInfo tradeInfo) {
        if(null == tradeInfo){
            return false;
        }
        return Objects.equals(tradeInfo.getTradeSign(), TradeSignEnum.SELL_SIGN.getCode());
    }

    /**
     * 检查是否为止盈止损buy交易信号
     * @param tradeInfo
     * @return
     */
    public static boolean checkIfTPSLBuySign(TradeSignDTO.TradeInfo tradeInfo) {
        if(null == tradeInfo){
            return false;
        }
        return Objects.equals(tradeInfo.getTradeSign(), TradeSignEnum.TP_BUY_SIGN.getCode())
                || Objects.equals(tradeInfo.getTradeSign(), TradeSignEnum.SL_BUY_SIGN.getCode());
    }

    /**
     * 检查是否为止损交易信号
     * @param tradeInfo
     * @return
     */
    public static boolean checkIfSLSign(TradeSignDTO.TradeInfo tradeInfo) {
        if(null == tradeInfo){
            return false;
        }
        return Objects.equals(tradeInfo.getTradeSign(), TradeSignEnum.SL_SELL_SIGN.getCode())
                || Objects.equals(tradeInfo.getTradeSign(), TradeSignEnum.SL_BUY_SIGN.getCode());
    }

    /**
     * 检查是否为止盈止损sell交易信号
     * @param tradeInfo
     * @return
     */
    public static boolean checkIfTPSLSellSign(TradeSignDTO.TradeInfo tradeInfo) {
        if(null == tradeInfo){
            return false;
        }
        return Objects.equals(tradeInfo.getTradeSign(), TradeSignEnum.SL_SELL_SIGN.getCode())
                || Objects.equals(tradeInfo.getTradeSign(), TradeSignEnum.TP_SELL_SIGN.getCode());
    }


}
