package com.tester.testercommon.model.request;

import com.tester.testercommon.model.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel(description = "id和名字VO")
@Data
@Accessors(chain = true)
public class IdAndNameRequest extends BaseDTO {
    @ApiModelProperty(name = "id", value = "id", example = "1209")
    private Long id;

    @ApiModelProperty(name = "name", value = "名称", example = "看看")
    private String name;
}
