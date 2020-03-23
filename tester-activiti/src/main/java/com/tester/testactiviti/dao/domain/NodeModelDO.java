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
@Table(name = "f_node_model")
public class NodeModelDO extends BaseDomain {
    @Override
    public NodeModelDO init(){
        super.init();
        return this;
    }
    /** 流程模板id */
    private Long flowModelId ;
    /** activiti流程id */
    private String activitiFlowId ;
    /** 节点key，uuid; */
    private String nodeKey ;
    /**
     * 节点类型;节点类型(任务节点/判断节点/起始/终止)
     * <br/>note: 除了判断节点，其他类型都只有唯一一个确定的下一个节点
     */
    private Integer nodeType ;
    /** 序号 */
    private Integer serialNumber ;
    /** 下一个节点;在有序列号之后，需要吗 */
    private String nextNodeKeyList ;
    /** 审批任务类型。1=或签，2=会签。 注：任务节点才需要 */
    private Integer coopType ;
    /** 审批人类型。;注：任务节点才需要 */
    private Integer approverType ;
    /** 指定id列表;根据审批人类型来确定。可能是人员id、岗位id、角色id等。注：任务节点才需要 */
    private String specificIdList ;

}
