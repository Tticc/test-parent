package com.tester.base.dto.model.request;

import com.tester.base.dto.model.BaseDTO;
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

    @ApiModelProperty(value = "text", name = "text", example = "texxxxxx")
    private String text;
}
