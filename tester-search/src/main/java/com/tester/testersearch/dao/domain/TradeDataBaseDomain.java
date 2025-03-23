package com.tester.testersearch.dao.domain;


import com.tester.base.dto.dao.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易数据(与数据库表字段一一对应的实体类,公有字段继承至父类)
 *
 * @author 温昌营
 * @version 1.0.0
 * @date 2025-03-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TradeDataBaseDomain extends BaseDomain {
    private static final long serialVersionUID = 1L;
    /**
     * 毫秒时间戳。非数据库字段
     */
    private Long timestamp;
    /**
     * 币key
     */
    private String bKey;
    /**
     * 时间区间
     */
    private String bar;
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
     * 是否真实数据。1=是，0=否
     */
    private Integer realData;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    public static final String B_KEY = "b_key";
    public static final String OPEN = "open";
    public static final String CLOSE = "close";
    public static final String HIGH = "high";
    public static final String LOW = "low";
    public static final String VOLUME = "volume";
    public static final String REAL_DATA = "real_data";
    public static final String CREATE_TIME = "create_time";
    public static final String UPDATE_TIME = "update_time";

}