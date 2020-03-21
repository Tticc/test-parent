package com.tester.testactiviti.model.request;

import com.tester.testactiviti.dao.domain.FlowFieldConditionDO;
import com.tester.testactiviti.dao.domain.NodeModelDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class NodeCreateModel {

    // task model table
    @ApiModelProperty(value = "节点类型;节点类型(任务节点/判断节点/起始/终止)", example = "1", required = true)
    private Integer nodeType;

    @ApiModelProperty(value = "节点唯一id", example = "jifoaiofia", required = true)
    private String nodeKey;

    @ApiModelProperty(value = "序列号", example = "1", required = true)
    private Integer serialNumber;

    @ApiModelProperty(value = "可能的下一个节点", example = "担当", required = false)
    private List<String> nextNodeKeyList;

    @ApiModelProperty(value = "审批任务类型", example = "担当", required = false)
    private Integer coopType;

    @ApiModelProperty(value = "审批人类型", example = "1,2,3。人，角色，岗位等等", required = false)
    private Integer approverType;

    @ApiModelProperty(value = "指定id列表", example = "[1,2,3]", required = false)
    private List<Long> specificIdList;

    @ApiModelProperty(value = "条件列表。节点类型为 判断节点 时才有", example = "[1,2,3]", required = false)
    List<FieldConditionCreateModel> conditions;

    /*// condition table
    @ApiModelProperty(value = "字段模板id", example = "1", required = false)
    private Long fieldModelId;

    @ApiModelProperty(value = "是否条件;0=false，1=true", example = "endNode", required = true)
    private Integer ifCondition;

    @ApiModelProperty(value = "判断类型;1=大于，2=小于，3=等于", example = "1", required = false)
    private Integer checkType ;

    @ApiModelProperty(value = "比较的值", example = "1", required = false)
    private String checkValue ;

    @ApiModelProperty(value = "条件为真时下一个节点的key", example = "userTask", required = false)
    private String trueNext;

    @ApiModelProperty(value = "条件为假时下一个节点的key", example = "endNode", required = false)
    private String falseNext;


    private void te() {
        NodeModelDO nodeModelDO = new NodeModelDO();
        nodeModelDO
                .setActivitiFlowId(null)
                .setFlowModelId(null)

                .setNodeType(null)
                .setNodeKey(null)
                .setApproverType(null)
                .setCoopType(null)
                .setNextNodeKeyList(null)
                .setSerialNumber(null)
                .setSpecificIdList(null)
        ;
    }*/

}
