package com.tester.testercommon.model.request;

import com.tester.testercommon.model.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import java.util.List;

@ApiModel(description = "id和名字VO")
@Data
@Accessors(chain = true)
public class UserRequest extends BaseDTO {

    @ApiModelProperty(name = "id", value = "id", example = "1209")
    private Long id;

    @ApiModelProperty(name = "name", value = "名称", example = "看看")
    private String name;

    @ApiModelProperty(name = "email",value = "邮箱", example = "wjio@qq.com",required = true)
//    @Email(message = "invalid email",regexp = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")
    @Email(message = "invalid email",regexp = "^(\\w+?)@(\\w+?)\\.([a-zA-Z]{2,})$")
    private String email;

    @ApiModelProperty(name = "address",value = "地址", example = "看看")
    private List<String> address;

}
