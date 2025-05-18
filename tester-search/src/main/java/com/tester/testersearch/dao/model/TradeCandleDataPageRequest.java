package com.tester.testersearch.dao.model;

import com.tester.base.dto.model.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 交易数据分页请求对象
 *
 * @Date 2025-3-16 16:13:27
 * @Author 温昌营
 **/
@ApiModel(description = "交易数据分页请求对象")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TradeCandleDataPageRequest extends PageRequest {
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
     * 蜡烛结束毫秒时间戳
     */
    @ApiModelProperty(value = "蜡烛结束毫秒时间戳", name = "endTimestamp")
    private Long endTimestamp;
    /**
     * 蜡烛最后更新毫秒时间戳
     */
    @ApiModelProperty(value = "蜡烛最后更新毫秒时间戳", name = "lastUpdateTimestamp")
    private Long lastUpdateTimestamp;
    /**
     * 开盘价
     */
    @ApiModelProperty(value = "开盘价", name = "open")
    private BigDecimal open;
    /**
     * 收盘价
     */
    @ApiModelProperty(value = "收盘价", name = "close")
    private BigDecimal close;
    /**
     * 最高价
     */
    @ApiModelProperty(value = "最高价", name = "high")
    private BigDecimal high;
    /**
     * 最低价
     */
    @ApiModelProperty(value = "最低价", name = "low")
    private BigDecimal low;
    /**
     * 交易量
     */
    @ApiModelProperty(value = "交易量", name = "volume")
    private BigDecimal volume;
    /**
     * ma5
     */
    @ApiModelProperty(value = "ma5", name = "ma5")
    private BigDecimal ma5;
    /**
     * ma10
     */
    @ApiModelProperty(value = "ma10", name = "ma10")
    private BigDecimal ma10;
    /**
     * ma20
     */
    @ApiModelProperty(value = "ma20", name = "ma20")
    private BigDecimal ma20;
    /**
     * 蜡烛是否已完成。0=未完成，1=已完成
     */
    @ApiModelProperty(value = "蜡烛是否已完成。0=未完成，1=已完成", name = "completed")
    private Integer completed;
    /**
     * 扩展信息
     */
    @ApiModelProperty(value = "扩展信息	", name = "extColumn")
    private String extColumn;
    /**
     * 是否删除 0=未删除，1=删除
     */
    @ApiModelProperty(value = "是否删除 0=未删除，1=删除", name = "deleted")
    private Integer deleted;
}