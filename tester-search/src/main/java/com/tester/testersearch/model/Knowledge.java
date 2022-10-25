package com.tester.testersearch.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * knowledge实体类
 *
 * @Date 14:13 2022/8/23
 * @Author 温昌营
 **/
@Data
@Accessors(chain = true)
public class Knowledge {
    /**
     * 唯一编码
     **/
    private String code;

    /**
     * 类型。1=游戏，2=文学，3=科普
     **/
    private Integer type;

    /**
     * 归属
     **/
    private String belong;

    /**
     * 关键字
     **/
    private String keyword;

    /**
     * 标题
     **/
    private String title;

    /**
     * 描述
     **/
    private String description;

    /**
     * 详情
     **/
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
}