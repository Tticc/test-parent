package com.tester.base.dto.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tester.base.dto.model.BaseDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Author 温昌营
 * @Date 2021-12-2 17:12:25
 */
@ApiModel(description = "kv响应对象")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KVResponse<K, V> extends BaseDTO {
    private static final long serialVersionUID = 1L;

    private K key;

    private V value;

    private String desc;
}
