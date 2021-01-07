package com.tester.testercommon.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 温昌营
 * @Date 2020-12-21 12:04:05
 */
@ApiModel(description = "TextRequest")
@Data
public class TextRequest {
    /**
     * text
     */
    @ApiModelProperty(value = "text", name = "text")
    private String text;
}
