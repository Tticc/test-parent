package com.tester.testactiviti.dao.domain;

import com.tester.base.dto.dao.domain.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Table;

/**
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper=true)
@Accessors(chain=true)
@Table(name = "front_field_value")
public class FieldValueDO extends BaseDomain {
    @Override
    public FieldValueDO init(){
        super.init();
        return this;
    }
    /** 字段类型id */
    private Long fieldId ;
    /** caseid */
    private Long caseId ;
    /** 值 */
    private String value ;

}
/*
CREATE TABLE front_field_value(
    id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'id' ,
    field_id BIGINT    COMMENT '字段类型id' ,
    case_id BIGINT    COMMENT 'caseid' ,
    value VARCHAR(64)    COMMENT '值' ,
    deleted INT    COMMENT '是否删除 0=未删除，1=删除' ,
    create_time DATETIME    COMMENT '创建时间' ,
    update_time DATETIME    COMMENT '更新时间' ,
    revision INT    COMMENT '版本号' ,
    PRIMARY KEY (id)
) COMMENT = 'front_field_value 字段值';

*/
