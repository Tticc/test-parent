package com.tester.testactiviti.model.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FormFieldCreateModel {

    @ApiModelProperty(value = "表单模板id", example = "1", required = true)
    private Long formModelId;
    @ApiModelProperty(value = "字段模板id", example = "1", required = true)
    private Long fieldModelId;
    @ApiModelProperty(value = "字段名字", example = "1", required = true)
    private String name;
    @ApiModelProperty(value = "字段key", example = "1", required = true)
    private String key;
    @ApiModelProperty(value = "序列号", example = "1", required = true)
    private Integer serialNumber;

}
