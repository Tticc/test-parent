package com.tester.testersearch.dao.domain;

import com.tester.base.dto.dao.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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

    public static final String B_KEY = "b_key";
    public static final String BAR = "bar";
    public static final String OPEN_TIMESTAMP = "open_timestamp";
    public static final String END_TIMESTAMP = "end_timestamp";
    public static final String LAST_UPDATE_TIMESTAMP = "last_update_timestamp";
    public static final String OPEN = "open";
    public static final String CLOSE = "close";
    public static final String HIGH = "high";
    public static final String LOW = "low";
    public static final String VOLUME = "volume";
    public static final String MA5 = "ma5";
    public static final String MA10 = "ma10";
    public static final String MA20 = "ma20";
    public static final String COMPLETED = "completed";
    public static final String EXT_COLUMN = "ext_column";

}