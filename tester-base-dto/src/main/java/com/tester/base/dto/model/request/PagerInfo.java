package com.tester.base.dto.model.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author 温昌营
 * @Date 2021-3-8 10:35:10
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "分页结果")
public class PagerInfo<T> {

    private static String getMessage() {
        return "";
    }

    @ApiModelProperty("当前页")
    private int pageNum;

    @ApiModelProperty("每页的数量")
    private int pageSize = 10;

    @ApiModelProperty("总记录数")
    private long total;

    @ApiModelProperty("总页数")
    private int pages;

    @ApiModelProperty("数据集合")
    private List<T> list;

    @ApiModelProperty("是否有下一页")
    private boolean hasNextPage;

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private String totalMapperId;

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private boolean orderByOnly;

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private boolean count = true;

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private String countColumn;

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private Boolean pageSizeZero;

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private String orderBy;

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private int startRow;

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private int endRow;
}