package com.tester.testercommon.model.response;

import com.tester.testercommon.model.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author 温昌营
 * @Date 2021-6-4 15:50:00
 */
@Data
@Accessors(chain = true)
@ApiModel(value =  "ExcelUserResponse", description = "ExcelUserResponse")
public class ExcelUserResponse extends BaseDTO {

    @ApiModelProperty(name =  "name", value =  "姓名")
    private String name;

    @ApiModelProperty(name =  "employeeId", value =  "员工号")
    private String employeeId;

    @ApiModelProperty(name =  "wechatid", value =  "微信id")
    private String wechatid;

    @ApiModelProperty(name =  "badge", value =  "badge")
    private String badge;

    @ApiModelProperty(name =  "cellphone", value =  "手机号")
    private String cellphone;

    @ApiModelProperty(name =  "gender", value =  "性别")
    private String gender;

    @ApiModelProperty(name =  "email", value =  "邮箱")
    private String email;

    @ApiModelProperty(name =  "corpEmail", value =  "公司邮箱")
    private String corpEmail;

}
