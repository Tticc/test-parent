package com.tester.testercommon.model.response;

import com.tester.base.dto.model.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author 温昌营
 * @Date 2020-9-7 18:34:41
 */
@Data
@Accessors(chain = true)
@ApiModel(value =  "ItemResponse", description = "礼品明细")
public class ItemResponse extends BaseDTO {

    @ApiModelProperty(name =  "giftCode", value =  "礼品编码")
    private String giftCode;

    @ApiModelProperty(name =  "skuName", value =  "礼品名称")
    private String skuName;

    @ApiModelProperty(name =  "itemCode", value =  "货号")
    private String itemCode;

    @ApiModelProperty(name = "orderItemNo", value =   "订单礼品行号")
    private Integer orderItemNo;
}
