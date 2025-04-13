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

    /***************** MA 和 交易信号 **********************************************************************/
    @ApiModelProperty(value = "交易信号。 1=buy, -1=卖。null=未设置", name = "tradeSign")
    private Integer tradeSign;

    @ApiModelProperty(value = "交易价格", name = "tradePrice")
    private BigDecimal tradePrice;

    @ApiModelProperty(value = "交易时间", name = "tradeTime")
    private Date tradeTime;

    @ApiModelProperty(value = "ma5", name = "ma5")
    private BigDecimal ma5;

    @ApiModelProperty(value = "ma10", name = "ma10")
    private BigDecimal ma10;

    @ApiModelProperty(value = "ma20", name = "ma20")
    private BigDecimal ma20;
    /***************** MA 和 交易信号 **********************************************************************/



    /***************** 蜡烛图扩展信息 **********************************************************************/
    @ApiModelProperty(value = "蜡烛开始毫秒时间戳", name = "timestamp")
    private Long openTimestamp;

    @ApiModelProperty(value = "蜡烛最后更新毫秒时间戳", name = "lastUpdateTimestamp")
    private Long lastUpdateTimestamp;

    @ApiModelProperty(value = "蜡烛结束毫秒时间戳", name = "endTimestamp")
    private Long endTimestamp;
    /***************** 蜡烛图扩展信息 **********************************************************************/



    /***************** 布林线信息 **********************************************************************/
    @ApiModelProperty(value = "布林线中轨", name = "middleBand")
    public BigDecimal middleBand;

    @ApiModelProperty(value = "布林线下轨", name = "middleBand")
    public BigDecimal upperBand;

    @ApiModelProperty(value = "布林线上轨", name = "middleBand")
    public BigDecimal lowerBand;
    /***************** 布林线信息 **********************************************************************/


    /***************** ADX信息 **********************************************************************/

    @ApiModelProperty(value = "+DI", name = "plusDI")
    public BigDecimal plusDI;

    @ApiModelProperty(value = "-DI", name = "minusDI")
    public BigDecimal minusDI;

    @ApiModelProperty(value = "ADX", name = "ADX")
    public BigDecimal ADX;

    /***************** ADX信息 **********************************************************************/

}