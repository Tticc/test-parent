package com.tester.testercommon.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@ApiModel(description = "id和名字VO")
@Data
@Accessors(chain = true)
public class IdAndNameModel {
    @ApiModelProperty(value = "id", example = "1209",required = true)
    private Long id;
    @ApiModelProperty(value = "名称", example = "看看",required = true)
    @NotNull(message = "name not null")
    private String name;
//    @Email(message = "invalid email",regexp = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")
    @Email(message = "invalid email",regexp = "^(\\w+?)@(\\w+?)\\.([a-zA-Z]{2,})$")
    private String email;
}
