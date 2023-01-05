package com.tester.base.dto.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tester.base.dto.model.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "PagerReqDTO", description = "分页请求对象")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Data
public class PageRequest extends BaseDTO {
    @ApiModelProperty(
            value = "第几页，默认1",
            name = "pageNum"
    )
    private int pageNum = 1;
    @ApiModelProperty(
            value = "每页展示几条，默认10",
            name = "pageSize"
    )
    private int pageSize = 10;
    @ApiModelProperty(
            value = "总记录数",
            name = "total",
            required = false
    )
    private long total;

}
