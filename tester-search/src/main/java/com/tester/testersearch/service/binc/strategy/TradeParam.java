package com.tester.testersearch.service.binc.strategy;

import com.tester.testersearch.util.BKeyEnum;
import com.tester.testersearch.util.BarEnum;
import com.tester.testersearch.util.StrategyEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class TradeParam {

    /***************** 非策略字段 ***********************************************/
    private Boolean needSave = false;

    private String bKey = BKeyEnum.BTCUSDT.getCode();
    // 交易费用
    private BigDecimal tradeFee = new BigDecimal("0.0004");

    // 止损交易费用
    private BigDecimal slTradeFee = new BigDecimal("0.0007");

    /***************** 非策略字段 ***********************************************/


    /***************** 策略字段 ***********************************************/
    /**
     * 步长
     */
    private String strategyCode;
    /**
     * 步长
     */
    private Integer step = 5;
    /**
     * 短期MA
     */
    private Integer maShort = 5;
    /**
     * 中期期MA
     */
    private Integer maMiddle = 10;
    /**
     * 长期MA
     */
    private Integer maLong = 20;
    /**
     * 蜡烛时长
     */
    private BarEnum barEnum = BarEnum._30m;
    /**
     * 遇到大收益后跳过交易次数
     */
    private Integer skipAfterHuge = 10;
    /**
     * 持续遇到大收益后跳过交易次数
     */
    private Integer keepSkipAfterHuge = 10;
    /**
     * 大收益收益率
     */
    private BigDecimal skipTimes = new BigDecimal("0.012");
    /**
     * 1=profits超过skipTimes出发skip，2=ma超过skipTimes出发skip
     */
    private Integer skipByProfits = 1;
    /**
     * 止损收益率
     */
    private BigDecimal slTimes = new BigDecimal("0.005");
    /**
     * 止盈收益率
     */
    private BigDecimal tpTimes = new BigDecimal("0.07");
    /**
     * true=排除最后一个节点，false=不排除最后一个节点
     */
    private Boolean excludeLast = true;
    /**
     * 排除伪ma。即：上穿，但最后一个节点是下穿/下穿，但最后一个节点是上穿
     */
    private boolean skipFakeMa = true;




    /**
     * 止损收益率
     */
    private BigDecimal reverseSlTimes = new BigDecimal("0.005");
    /**
     * 止盈收益率
     */
    private BigDecimal reverseTpTimes = new BigDecimal("0.01");
    /**
     * 大收益后反向交易前跳过次数
     */
    private Integer reverseSkipNum = 0;
    /**
     * 大收益后反向交易持续次数
     */
    private Integer reverseTakeNum = 0;

    /***************** 策略字段 ***********************************************/
}
