package com.tester.testersearch.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * KnowledgeResponse
 *
 * @Date 14:13 2022/8/23
 * @Author 温昌营
 **/
@Data
@Accessors(chain = true)
public class KnowledgeResponse extends Knowledge {

    /**
     * 类型。1=游戏，2=文学，3=科普
     **/
    private String sort;

}