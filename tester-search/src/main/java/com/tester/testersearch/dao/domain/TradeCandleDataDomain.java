package com.tester.testersearch.dao.domain;

import com.alibaba.fastjson.JSON;
import com.tester.base.dto.dao.BaseDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * 蜡烛数据(与数据库表字段一一对应的实体类,公有字段继承至父类)
 *
 * @author wenc
 * @version 1.0.0
 * @date 2025-05-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TradeCandleDataDomain extends BaseDomain {
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
     * 蜡烛结束毫秒时间戳
     */
    private Long endTimestamp;
    /**
     * 蜡烛最后更新毫秒时间戳
     */
    private Long lastUpdateTimestamp;
    /**
     * 开盘价
     */
    private BigDecimal open;
    /**
     * 收盘价
     */
    private BigDecimal close;
    /**
     * 最高价
     */
    private BigDecimal high;
    /**
     * 最低价
     */
    private BigDecimal low;
    /**
     * 交易量
     */
    private BigDecimal volume;
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
     * 蜡烛是否已完成。0=未完成，1=已完成
     */
    private Integer completed;
    /**
     * 扩展信息
     */
    private String extColumn;

    public ExtColumn parseExtColumn(){
        String extColumnStr = this.getExtColumn();
        ExtColumn extColumnObj;
        if(StringUtils.isEmpty(extColumnStr) || null == (extColumnObj = JSON.parseObject(extColumnStr, ExtColumn.class))){
            extColumnObj = new ExtColumn();
        }
        return extColumnObj;
    }

    @Data
    @Accessors(chain = true)
    public static class ExtColumn{

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

}