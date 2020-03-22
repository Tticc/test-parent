package com.tester.testactiviti.dao.domain;

import com.tester.testercommon.dao.domain.BaseDomain;
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
@Table(name = "f_form_field_condition")
public class FormFieldConditionDO extends BaseDomain {
    @Override
    public FormFieldConditionDO init(){
        super.init();
        return this;
    }
    /** 表单-字段关系表id;可能并不需要 */
    private Long formFieldId ;
    /** 表单-字段关系表key */
    private String formFieldKey ;
    /** 使用节点id */
    private Long nodeId ;
    /** 使用节点key;在哪个节点使用这个条件 */
    private String nodeKey ;
    /** 判断类型;1=大于，2=小于，3=等于 */
    private Integer checkType ;
    /** 比较的值 */
    private String checkValue ;
    /** 结果为真时的下一个节点key */
    private String trueNext ;
    /** 结果为真时的下一个节点id */
    private Long trueNextId ;
    /** 结果为假时的下一个节点key */
    private String falseNext ;
    /** 结果为假时的下一个节点id */
    private Long falseNextId ;

}
/*
CREATE TABLE f_flow_field_condition(
    id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'id' ,
    flow_model_id BIGINT    COMMENT '流程模板id' ,
    field_model_id BIGINT    COMMENT '字段模板id' ,
    if_condition INT    COMMENT '是否条件 0=false，1=true' ,
    check_type INT    COMMENT '判断类型 1=大于，2=小于，3=等于' ,
    check_value VARCHAR(64)    COMMENT '比较的值' ,
    node_key VARCHAR(64)    COMMENT '使用节点 在哪个节点使用这个条件' ,
--    result INT    COMMENT '比较结果 0=false，1=true' ,
    true_next VARCHAR(64)    COMMENT '结果为真时的下一个节点key' ,
    false_next VARCHAR(64)    COMMENT '结果为假时的下一个节点key' ,
    deleted INT    COMMENT '是否删除 0=未删除，1=删除' ,
    create_time DATETIME    COMMENT '创建时间' ,
    update_time DATETIME    COMMENT '更新时间' ,
    revision INT    COMMENT '版本号' ,
    PRIMARY KEY (id)
) COMMENT = 'f_flow_field_condition 流程-字段-条件关系表';
*/
