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
public class TradeDataBasePageRequest extends PageRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 毫秒时间戳
     */
    @ApiModelProperty(value = "id", name = "id")
    private Long id;
    /**
     * 毫秒时间戳
     */
    @ApiModelProperty(value = "毫秒时间戳", name = "timestamp")
    private Long timestamp;
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
     * 是否真实数据。1=是，0=否
     */
    @ApiModelProperty(value = "是否真实数据。1=是，0=否", name = "realData")
    private Integer realData;

    @ApiModelProperty(value = "是否删除", name = "deleted")
    private Integer deleted;
}