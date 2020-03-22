package com.tester.testactiviti.model.request;

import com.tester.testactiviti.dao.domain.FormFieldConditionDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NodeConditionCreateModel {




    // condition table
    @ApiModelProperty(value = "表单-字段key", example = "1", required = false)
    private String formFieldKey;

    // todo tobe deleted. 根据formFieldKey获取这个id，然后更新 表单-字段表的 if_condition
//    @ApiModelProperty(value = "表单-字段id", example = "1", required = false)
//    private Long formFieldId;

    // 一定会是condition
//    @ApiModelProperty(value = "是否条件;0=false，1=true", example = "endNode", required = true)
//    private Integer ifCondition;

    @ApiModelProperty(value = "判断类型;1=大于，2=小于，3=等于", example = "1", required = false)
    private Integer checkType ;

    @ApiModelProperty(value = "比较的值", example = "1", required = false)
    private String checkValue ;

    @ApiModelProperty(value = "条件为真时下一个节点的key", example = "userTask", required = false)
    private String trueNext;

    @ApiModelProperty(value = "条件为假时下一个节点的key", example = "endNode", required = false)
    private String falseNext;




    private void tt(){
        FormFieldConditionDO flowFieldConditionDO = new FormFieldConditionDO();
        flowFieldConditionDO
                .setNodeKey(null)


                .setCheckType(null)
                .setCheckValue(null)
                .setTrueNext(null)
                .setFalseNext(null)
        ;
    }
}
