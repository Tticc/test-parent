package com.tester.testercommon.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tester.base.dto.model.BaseDTO;
import com.tester.testercommon.util.DecimalUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Quotation 响应对象
 *
 * @author 温昌营
 * @version 1.0.0
 * @date 2021-12-8 13:44:06
 */
@ApiModel(description = "Quotation 响应对象")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
public class QuotationResponse extends BaseDTO {
    private static final long serialVersionUID = 1L;
    /**
     * 产品(高级)
     */
    @ApiModelProperty(value = "产品(高级)", name = "seniorProductCost")
    private BigDecimal seniorProductCost;

    /**
     * 产品(中级)
     */
    @ApiModelProperty(value = "产品(中级)", name = "middleProductCost")
    private BigDecimal middleProductCost;

    /**
     * 后端(高级)
     */
    @ApiModelProperty(value = "后端(高级)", name = "seniorBackCost")
    private BigDecimal seniorBackCost;

    /**
     * 后端(中级)
     */
    @ApiModelProperty(value = "后端(中级)", name = "middleBackCost")
    private BigDecimal middleBackCost;

    /**
     * H5 App(高级)
     */
    @ApiModelProperty(value = "H5 App(高级)", name = "seniorH5AppCost")
    private BigDecimal seniorH5AppCost;

    /**
     * H5 App(中级)
     */
    @ApiModelProperty(value = "H5 App(中级)", name = "middleH5AppCost")
    private BigDecimal middleH5AppCost;

    /**
     * H5 Web(高级)
     */
    @ApiModelProperty(value = "H5 Web(高级)", name = "seniorH5WebCost")
    private BigDecimal seniorH5WebCost;

    /**
     * H5 Web(中级)
     */
    @ApiModelProperty(value = "H5 Web(中级)", name = "middleH5WebCost")
    private BigDecimal middleH5WebCost;

    /**
     * App(高级)
     */
    @ApiModelProperty(value = "App(高级)", name = "seniorAppCost")
    private BigDecimal seniorAppCost;

    /**
     * App(中级)
     */
    @ApiModelProperty(value = "App(中级)", name = "middleAppCost")
    private BigDecimal middleAppCost;

    /**
     * UI设计(高级)
     */
    @ApiModelProperty(value = "UI设计(高级)", name = "seniorUiCost")
    private BigDecimal seniorUiCost;

    /**
     * UI设计(中级)
     */
    @ApiModelProperty(value = "UI设计(中级)", name = "middleUiCost")
    private BigDecimal middleUiCost;

    /**
     * 测试(高级)
     */
    @ApiModelProperty(value = "测试(高级)", name = "seniorTestCost")
    private BigDecimal seniorTestCost;

    /**
     * 测试(中级)
     */
    @ApiModelProperty(value = "测试(中级)", name = "middleTestCost")
    private BigDecimal middleTestCost;

    /**
     * 总计
     */
    @ApiModelProperty(value = "总计", name = "total")
    private BigDecimal total;


    public BigDecimal sum() {
        return DecimalUtil.toDecimal(this.seniorProductCost)
                .add(DecimalUtil.toDecimal(this.middleProductCost))
                .add(DecimalUtil.toDecimal(this.seniorBackCost))
                .add(DecimalUtil.toDecimal(this.middleBackCost))
                .add(DecimalUtil.toDecimal(this.seniorH5AppCost))
                .add(DecimalUtil.toDecimal(this.middleH5AppCost))
                .add(DecimalUtil.toDecimal(this.seniorH5WebCost))
                .add(DecimalUtil.toDecimal(this.middleH5WebCost))
                .add(DecimalUtil.toDecimal(this.seniorAppCost))
                .add(DecimalUtil.toDecimal(this.middleAppCost))
                .add(DecimalUtil.toDecimal(this.seniorUiCost))
                .add(DecimalUtil.toDecimal(this.middleUiCost))
                .add(DecimalUtil.toDecimal(this.seniorTestCost))
                .add(DecimalUtil.toDecimal(this.middleTestCost));
    }

}