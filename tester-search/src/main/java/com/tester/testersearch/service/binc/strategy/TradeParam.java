package com.tester.testersearch.service.binc.strategy;

import com.tester.testersearch.util.BKeyEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class TradeParam {
    private String bKey = BKeyEnum.BTCUSDT.getCode();
    /**
     * 遇到大收益后跳过交易次数
     */
    private int skipAfterHuge = 10;
    /**
     * 持续遇到大收益后跳过交易次数
     */
    private int keepSkipAfterHuge = 10;
    /**
     * 大收益收益率
     */
    private BigDecimal skipTimes = new BigDecimal("0.012");
    /**
     * 止损收益率
     */
    private BigDecimal slTimes = new BigDecimal("0.005");
    /**
     * 止盈收益率
     */
    private BigDecimal tpTimes = new BigDecimal("0.07");
    /**
     * 止损收益率
     */
    private BigDecimal reverseSlTimes = new BigDecimal("0.005");
    /**
     * 止盈收益率
     */
    private BigDecimal reverseTpTimes = new BigDecimal("0.07");
    /**
     * 大收益后反向交易前跳过次数
     */
    private int reverseSkipNum = 0;
    /**
     * 大收益后反向交易持续次数
     */
    private int reverseTakeNum = 0;
}
