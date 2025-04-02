package com.tester.testersearch.dao.domain;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易信号
 *
 * @author 温昌营
 * @version 1.0.0
 * @date 2025-03-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TradeSignDTO extends TradeDataBaseDomain {
    private static final long serialVersionUID = 1L;

    /**
     * 交易信号。 1=buy, -1=卖。null=未设置
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
     * ma5
     */
    private BigDecimal ma5;
    /**
     * ma10
     */
    private BigDecimal ma10;
    /**
     * ma20
     */
    private BigDecimal ma20;

    /**
     * 开始毫秒时间戳
     */
    @ApiModelProperty(value = "开始毫秒时间戳", name = "timestamp")
    private Long openTimestamp;
    /**
     * 最后更新毫秒时间戳
     */
    @ApiModelProperty(value = "最后更新毫秒时间戳", name = "lastUpdateTimestamp")
    private Long lastUpdateTimestamp;
    /**
     * 结束毫秒时间戳
     */
    @ApiModelProperty(value = "结束毫秒时间戳", name = "endTimestamp")
    private Long endTimestamp;

}