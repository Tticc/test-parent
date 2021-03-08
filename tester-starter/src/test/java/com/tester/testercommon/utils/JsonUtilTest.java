package com.tester.testercommon.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tester.testercommon.util.JsonUtils;
import lombok.Data;
import org.junit.Test;

/**
 * @Author 温昌营
 * @Date 2021-3-8 09:54:11
 */
public class JsonUtilTest {

    @Test
    public void test_json(){
        String s = "{\n" +
                "\t\"OrderNoxx\": \"xxxxxx\"\n" +
                "}";
        OrderRequest orderRequest = JsonUtils.fromJson(s, OrderRequest.class);
        System.out.println(orderRequest);
    }

    @Data
    public static class OrderRequest{
        @JsonProperty("OrderNoxx")
        private String orderNo;
    }
}
