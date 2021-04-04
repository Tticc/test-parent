package com.tester.testercommon.model.request;

import com.tester.testercommon.model.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 温昌营
 * @Date 2020-12-21 12:04:05
 */
@ApiModel(description = "TextRequest")
@Data
public class TextRequest extends BaseDTO {
    /**
     * text
     */
    @ApiModelProperty(value = "text", name = "text")
    private String text;
}
