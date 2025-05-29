/*
 * 版权所有.
 */

package com.tester.testersearch.dao.domain;

import com.tester.base.dto.dao.BaseDomain;
import com.tester.testersearch.service.binc.strategy.TradeParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
     * 策略编码
     */
    private String strategyCode;
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
     * skip数。距离上一次大收益信号数
     */
    private Integer skipNum;
    /**
     * 实际交易。1=true，0=false;
     */
    private Integer actualTrade;


    /*************************************查询字段*****************************************/
    /**
     * 蜡烛开始毫秒时间戳 list
     */
    private List<Long> openTimestampList;
    /*************************************查询字段*****************************************/


    @Data
    @Accessors(chain = true)
    public static class ExtColumn{
        private TradeParam tradeParam;
    }

}