package com.tester.testersearch.util.binc.tradeSign;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TradeSignEnum {
    NONE_SIGN(0, "无信号"),

    BUY_SIGN(1, "MA信号buy"),
    SL_BUY_SIGN(11, "止损buy"),
    TP_BUY_SIGN(12, "止盈buy"),

    SELL_SIGN(-1, "MA信号sell"),
    SL_SELL_SIGN(-11, "止损sell"),
    TP_SELL_SIGN(-12, "止盈sell"),
    ;
    private final int code;
    private final String text;
}