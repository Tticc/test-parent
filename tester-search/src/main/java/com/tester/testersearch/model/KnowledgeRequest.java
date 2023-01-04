package com.tester.testersearch.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * knowledge实体类
 *
 * @Date 14:13 2022/8/23
 * @Author 温昌营
 **/
@Data
@Accessors(chain = true)
public class KnowledgeRequest {
    /**
     * 唯一编码
     **/
    private String code;

    /**
     * 类型。1=游戏，2=文学，3=科普
     **/
    @NotNull(message = "类型不能为空")
    private Integer type;

    /**
     * 归属
     **/
    private String belong;

    /**
     * 关键字
     **/
    @NotEmpty(message = "关键字不能为空")
    private String keyword;

    /**
     * 标题
     **/
    @NotEmpty(message = "标题不能为空")
    private String title;

    /**
     * 描述
     **/
    @NotEmpty(message = "描述不能为空")
    private String description;

    /**
     * 详情
     **/
    @NotEmpty(message = "详情不能为空")
    private String detail;

    /**
     * 作者
     **/
    private String author;

    /**
     * 优先级
     **/
    private Integer priority;

    /**
     * 是否删除。1=已删除，0=未删除
     **/
    private Integer deleted;

    /**
     * 开始时间
     **/
    private Date startTime;

    /**
     * 结束时间
     **/
    private Date endTime;

    /**
     * 创建时间
     **/
    private Date createdTime;

    /**
     * 更新时间
     **/
    private Date updatedTime;

    /**
     * 创建人
     **/
    private String createdBy;

    /**
     * 更新人
     **/
    private String updatedBy;

    // 非索引库字段
    /**
     * copy_to，非索库引字段
     **/
    private String all;
}