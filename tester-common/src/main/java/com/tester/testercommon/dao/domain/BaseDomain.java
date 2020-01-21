package com.tester.testercommon.dao.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author 温昌营
 * @Date
 */
@Data
@Accessors(chain = true)
public class BaseDomain implements Serializable, Cloneable {
    private Long id;
    private Date createTime;
    private Date updateTime;
    private Integer deleted;
    private Integer revision;
}
