/*
 * 版权所有.
 */

package com.tester.testersearch.dao.model;

import com.tester.base.dto.model.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 蜡烛交易信号数据请求对象
 *
 * @author wenc
 * @version 1.0.0
 * @date 2025-05-18
 */
@ApiModel(description = "蜡烛交易信号数据请求对象")
@Data
@Accessors(chain = true)
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
     * 策略编码
     */
    @ApiModelProperty(value = "策略编码", name = "strategyCode")
    private String strategyCode;
    /**
     * 蜡烛开始毫秒时间戳
     */
    @ApiModelProperty(value = "蜡烛开始毫秒时间戳", name = "openTimestamp")
    private Long openTimestamp;

    /**
     * 蜡烛开始毫秒时间戳 list
     */
    @ApiModelProperty(value = "蜡烛开始毫秒时间戳列表", name = "openTimestampList")
    private List<Long> openTimestampList;
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
     * skip数。距离上一次大收益信号数
     */
    @ApiModelProperty(value = "skip数。距离上一次大收益信号数", name = "skipNum")
    private Integer skipNum;
    /**
     * 实际交易。1=true，0=false;
     */
    @ApiModelProperty(value = "实际交易。1=true，0=false", name = "deleted")
    private Integer actualTrade;
    /**
     * 是否删除 0=未删除，1=删除
     */
    @ApiModelProperty(value = "是否删除 0=未删除，1=删除", name = "deleted")
    private Integer deleted;
}