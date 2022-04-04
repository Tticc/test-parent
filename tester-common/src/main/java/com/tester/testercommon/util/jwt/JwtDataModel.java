package com.tester.testercommon.util.jwt;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel(description = "id和名字VO")
@Data
@Accessors(chain = true)
public class JwtDataModel {

    @ApiModelProperty(value = "id", example = "1209", required = true)
    private Long id;
    @ApiModelProperty(value = "expiredTime", example = "过期时间", required = true)
    private Long expiredTime;


}
