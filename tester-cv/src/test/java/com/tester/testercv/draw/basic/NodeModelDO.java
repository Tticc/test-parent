package com.tester.testercv.draw.basic;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NodeModelDO {
    private Long id;

    /** 流程模板id */
    private Long flowModelId ;
    /** activiti流程id */
    private String activitiFlowId ;
    /** 节点key，uuid;在有序列号之后，需要吗 */
    private String nodeKey ;
    /** 节点类型;节点类型(任务节点/排他判断节点/起始/终止) */
    private Integer nodeType ;
    /** 序号 */
    private Integer serialNumber ;
    /** 任务节点序号;用来排序流程中的任务节点 */
    private Integer taskNodeSerialNo ;
    /** 下一个节点列表;在有序列号之后，需要吗 */
    private String nextNodeKeyList ;

    // 审批节点才会有下列字段。coopType，approverType，specificIdList,limitType
    /** 审批任务类型。;1=或签，2=会签。 注：任务节点才需要 */
    private Integer coopType ;
    /** 审批人类型。;注：任务节点才需要 */
    private Integer approverType ;
    /** 指定人id列表;根据审批人类型来确定。可能是人员id、岗位id、角色id等。注：任务节点才需要 */
    private String specificIdList ;
    /** 限制类型;指定审批人为权限组时使用。0=不限制，1=同组织，2=同公司 */
    private Integer limitType ;

}
