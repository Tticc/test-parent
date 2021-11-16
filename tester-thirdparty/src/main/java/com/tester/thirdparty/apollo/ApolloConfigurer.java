package com.tester.thirdparty.apollo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 温昌营
 * @Date 2021-11-2 13:47:42
 */
@Data
@Configuration
public class ApolloConfigurer {

    // portal url
    @Value("${data.tools.auto.release.apollo.portalUrl:http://xx-apollo.xx.com/}")
    private String apolloPortalUrl;

    // 申请的token
    @Value("${data.tools.auto.release.apollo.token:xxxxxxx130fd783245ef850aed1f4d4e5e6}")
    private String apolloToken;

    // ************************ apollo end ************************


}
