/*
 * 版权所有.
 */

package com.tester.testersearch.dao.model;

import com.tester.base.dto.model.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 蜡烛交易信号数据请求对象
 *
 * @author wenc
 * @version 1.0.0
 * @date 2025-05-18
 */
@ApiModel(description = "蜡烛交易信号数据请求对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class CandleTradeSignalPageRequest extends PageRequest {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id", name = "id")
    private Long id;
    /**
     * 币key
     */
    @ApiModelProperty(value = "币key", name = "bKey")
    private String bKey;
    /**
     * 时间区间
     */
    @ApiModelProperty(value = "时间区间", name = "bar")
    private String bar;
    /**
     * 蜡烛开始毫秒时间戳
     */
    @ApiModelProperty(value = "蜡烛开始毫秒时间戳", name = "openTimestamp")
    private Long openTimestamp;
    /**
     * 扩展信息
     */
    @ApiModelProperty(value = "扩展信息	", name = "extColumn")
    private String extColumn;
    /**
     * 交易信号。1=MA信号buy，11=止损buy，12=止盈buy，-1=MA信号sell，-11=止损sell，-12=止盈sell
     */
    @ApiModelProperty(value = "交易信号。1=MA信号buy，11=止损buy，12=止盈buy，-1=MA信号sell，-11=止损sell，-12=止盈sell", name = "tradeSign")
    private Integer tradeSign;
    /**
     * 交易价格
     */
    @ApiModelProperty(value = "交易价格", name = "tradePrice")
    private BigDecimal tradePrice;
    /**
     * 交易时间
     */
    @ApiModelProperty(value = "交易时间", name = "tradeTime")
    private Date tradeTime;
    /**
     * 交易序列号
     */
    @ApiModelProperty(value = "交易序列号", name = "tradeSerialNum")
    private Integer tradeSerialNum;
    /**
     * 实际交易收益率
     */
    @ApiModelProperty(value = "实际交易收益率", name = "tradeProfitsRate")
    private BigDecimal tradeProfitsRate;
    /**
     * 实际交易收益
     */
    @ApiModelProperty(value = "实际交易收益", name = "tradeProfits")
    private BigDecimal tradeProfits;
    /**
     * 交易终点
     */
    @ApiModelProperty(value = "交易终点", name = "tradeEnd")
    private Integer tradeEnd;
    /**
     * 交易起点
     */
    @ApiModelProperty(value = "交易起点", name = "tradeStart")
    private Integer tradeStart;
    /**
     * 纯ma交易收益率(不设止盈止损)
     */
    @ApiModelProperty(value = "纯ma交易收益率(不设止盈止损)", name = "maTradeProfitsRate")
    private BigDecimal maTradeProfitsRate;
    /**
     * 纯ma交易收益(不设止盈止损)
     */
    @ApiModelProperty(value = "纯ma交易收益(不设止盈止损)", name = "maTradeProfits")
    private BigDecimal maTradeProfits;
    /**
     * 遇到大收益后跳过交易次数
     */
    @ApiModelProperty(value = "遇到大收益后跳过交易次数", name = "skipAfterHuge")
    private Integer skipAfterHuge;
    /**
     * 跳过期间遇到大收益重置跳过次数
     */
    @ApiModelProperty(value = "跳过期间遇到大收益重置跳过次数", name = "keepSkipAfterHuge")
    private Integer keepSkipAfterHuge;
    /**
     * skip数。距离上一次大收益信号数
     */
    @ApiModelProperty(value = "skip数。距离上一次大收益信号数", name = "skipNum")
    private BigDecimal skipNum;
    /**
     * 大收益比率
     */
    @ApiModelProperty(value = "大收益比率", name = "skipTimes")
    private BigDecimal skipTimes;
    /**
     * 是否删除 0=未删除，1=删除
     */
    @ApiModelProperty(value = "是否删除 0=未删除，1=删除", name = "deleted")
    private Integer deleted;
}