package com.tester.testercommon.dao.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.LogicDelete;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author 温昌营
 * @Date
 */
@Data
@Accessors(chain = true)
public class BaseDomain implements Serializable, Cloneable {
    public BaseDomain init(){
        this.updateTime = this.createTime = new Date();
        this.revision = this.deleted = 0;
        return this;
    }

    private Long id;
    private Date createTime;
    private Date updateTime;

    @LogicDelete
    private Integer deleted;
    private Integer revision;
}
