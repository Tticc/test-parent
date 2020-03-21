package com.tester.testactiviti.model.request;

import com.tester.testactiviti.dao.domain.FlowFieldConditionDO;
import com.tester.testactiviti.dao.domain.NodeModelDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class FlowModelCreateModel {
    @ApiModelProperty(value = "流程类型", example = "1", required = true)
    private Integer flowType;

    @ApiModelProperty(value = "节点列表", example = "1", required = true)
    private List<NodeCreateModel> nodes;
}
