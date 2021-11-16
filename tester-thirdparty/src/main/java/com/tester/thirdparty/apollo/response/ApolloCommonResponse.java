package com.tester.thirdparty.apollo.response;

import com.tester.testercommon.model.BaseDTO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author 温昌营
 * @Date 2021-10-22 11:40:30
 */
@Data
@Accessors(chain = true)
public class ApolloCommonResponse<T> extends BaseDTO {

    /**
     * 数据
     **/
    private List<T> data;
}
