package com.tester.testactiviti.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class FlowContentModelCreateModel {

    @ApiModelProperty(value = "流程类型", example = "1", required = true)
    private Integer flowType;

    @ApiModelProperty(value = "节点列表", example = "1", required = true)
    private List<NodeCreateModel> nodes;

    @ApiModelProperty(value = "表单字段列表", example = "1", required = true)
    private List<FormFieldCreateModel> formFields;

    @ApiModelProperty(value = "流程模板id", example = "1", required = true)
    private Long flowModelId;

    @ApiModelProperty(value = "表单模板id", example = "1", required = true)
    private Long formModelId;


}
