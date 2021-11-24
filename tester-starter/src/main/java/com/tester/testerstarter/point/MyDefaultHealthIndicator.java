package com.tester.testerstarter.point;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

/**
 * @Author 温昌营
 * @Date 2021-11-24 11:01:19
 */
public class MyDefaultHealthIndicator extends AbstractHealthIndicator {
    /**
     * wsdl接口地址。默认为生产环境
     */
    private String someWhere = "http://xxxx:8080/test";

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        builder.withDetail("location", this.someWhere);
        connectSomeWhere();
        builder.up();
    }

    private void connectSomeWhere() throws Exception {
        // do something here
//        doConnectSomeWhere(this.someWhere);
        System.out.println("this.someWhere = " + this.someWhere);
    }
}
