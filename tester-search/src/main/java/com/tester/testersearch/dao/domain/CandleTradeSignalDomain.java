/*
 * 版权所有.
 */

package com.tester.testersearch.dao.domain;

import com.tester.base.dto.dao.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 蜡烛交易信号数据(与数据库表字段一一对应的实体类,公有字段继承至父类)
 *
 * @author wenc
 * @version 1.0.0
 * @date 2025-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CandleTradeSignalDomain extends BaseDomain {
    private static final long serialVersionUID = 1L;
    /**
     * 币key
     */
    private String bKey;
    /**
     * 时间区间
     */
    private String bar;
    /**
     * 蜡烛开始毫秒时间戳
     */
    private Long openTimestamp;
    /**
     * 扩展信息
     */
    private String extColumn;
    /**
     * 交易信号。1=MA信号buy，11=止损buy，12=止盈buy，-1=MA信号sell，-11=止损sell，-12=止盈sell
     */
    private Integer tradeSign;
    /**
     * 交易价格
     */
    private BigDecimal tradePrice;
    /**
     * 交易时间
     */
    private Date tradeTime;
    /**
     * 交易序列号
     */
    private Integer tradeSerialNum;
    /**
     * 实际交易收益率
     */
    private BigDecimal tradeProfitsRate;
    /**
     * 实际交易收益
     */
    private BigDecimal tradeProfits;
    /**
     * 交易终点
     */
    private Integer tradeEnd;
    /**
     * 交易起点
     */
    private Integer tradeStart;
    /**
     * 纯ma交易收益率(不设止盈止损)
     */
    private BigDecimal maTradeProfitsRate;
    /**
     * 纯ma交易收益(不设止盈止损)
     */
    private BigDecimal maTradeProfits;
    /**
     * 遇到大收益后跳过交易次数
     */
    private Integer skipAfterHuge;
    /**
     * 跳过期间遇到大收益重置跳过次数
     */
    private Integer keepSkipAfterHuge;
    /**
     * skip数。距离上一次大收益信号数
     */
    private BigDecimal skipNum;
    /**
     * 大收益比率
     */
    private BigDecimal skipTimes;

    public static final String B_KEY = "b_key";
    public static final String BAR = "bar";
    public static final String OPEN_TIMESTAMP = "open_timestamp";
    public static final String EXT_COLUMN = "ext_column";
    public static final String TRADE_SIGN = "trade_sign";
    public static final String TRADE_PRICE = "trade_price";
    public static final String TRADE_TIME = "trade_time";
    public static final String TRADE_SERIAL_NUM = "trade_serial_num";
    public static final String TRADE_PROFITS_RATE = "trade_profits_rate";
    public static final String TRADE_PROFITS = "trade_profits";
    public static final String TRADE_END = "trade_end";
    public static final String TRADE_START = "trade_start";
    public static final String MA_TRADE_PROFITS_RATE = "ma_trade_profits_rate";
    public static final String MA_TRADE_PROFITS = "ma_trade_profits";
    public static final String SKIP_AFTER_HUGE = "skip_after_huge";
    public static final String KEEP_SKIP_AFTER_HUGE = "keep_skip_after_huge";
    public static final String SKIP_NUM = "skip_num";
    public static final String SKIP_TIMES = "skip_times";

}