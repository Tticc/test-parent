package com.tester.thirdparty.apollo.request;

import com.tester.testercommon.model.BaseDTO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author 温昌营
 * @Date 2021-10-22 11:34:49
 */
@Data
@Accessors(chain = true)
public class ApolloCommonRequest<T> extends BaseDTO {

    /**
     * appid。如：middleend-auth
     **/
    private String appId;

    /**
     * 环境。如：DEV、FAT
     **/
    private String env;

    /**
     * 集群。如：default、local
     **/
    private String clusterName;

    /**
     * 命名空间。如：application
     **/
    private String namespaceName;

    /**
     * 创建/修改人。不能为空
     **/
    private String by;

    /**
     * 数据
     **/
    private List<T> datas;
}
