package com.tester.testercommon.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author 温昌营
 * @Date 2021-12-2 17:12:25
 */
@ApiModel(description = "kv响应对象")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KVListResponse<K, V, T> extends KVResponse<K, V> {

    private List<T> list;

}
