package com.tester.testercommon.model.request.convert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 温昌营
 * @Date 2021-4-3 23:42:14
 */
@ApiModel(description = "TestConvertRequest")
@Data
public class TestConvertRequest {
    /**
     * name
     */
    @ApiModelProperty(value = "name", name = "name")
    private String name;
    /**
     * age
     */
    @ApiModelProperty(value = "age", name = "age")
    private Integer age;
}
