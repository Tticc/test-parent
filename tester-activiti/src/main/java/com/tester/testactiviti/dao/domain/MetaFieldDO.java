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
@Table(name = "meta_field")
public class MetaFieldDO extends BaseDomain {
    @Override
    public MetaFieldDO init(){
        super.init();
        return this;
    }
    /** 名称 */
    private String name ;
    /** 类型 */
    private Integer type ;
    /** 大类;可以按长度区分，暂时不区分 */
    private Integer category ;

}
/*
CREATE TABLE f_node_model(
    id BIGINT NOT NULL AUTO_INCREMENT  COMMENT 'id' ,
    activiti_flow_id VARCHAR(64)    COMMENT 'activiti流程id' ,
    node_key VARCHAR(32)    COMMENT '节点id，uuid 在有序列号之后，需要吗' ,
    node_type INT    COMMENT '节点类型 节点类型(任务节点/判断节点/起始/终止)' ,
    serial_number INT    COMMENT '序号' ,
    next_node_str_id_list VARCHAR(128)    COMMENT '下一个节点 在有序列号之后，需要吗' ,
    form_field_id BIGINT    COMMENT '表单-字段id。 注：判断节点才需要' ,
    approver_type INT    COMMENT '审批人类型。 注：任务节点才需要' ,
    specific_id_list VARCHAR(64)    COMMENT '指定id列表 根据审批人类型来确定。可能是人员id、岗位id、角色id等。注：任务节点才需要' ,
    deleted INT    COMMENT '是否删除 0=未删除，1=删除' ,
    create_time DATETIME    COMMENT '创建时间' ,
    update_time DATETIME    COMMENT '更新时间' ,
    revision INT    COMMENT '版本号' ,
    PRIMARY KEY (id)
) COMMENT = 'f_node_model 节点模板表';

*/
