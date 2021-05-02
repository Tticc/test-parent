package com.tester.testercommon.model.request;

import com.tester.testercommon.model.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;


/**
 * 会员id
 */

@ApiModel(description = "MemberRequest")
@Data
@Accessors(chain = true)
public class MemberRequest extends BaseDTO {

    @ApiModelProperty(value = "会员ID", required = true)
    @NotNull(message = "memberId not null")
    private Long memberId;
}
