package com.tester.testermybatis.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author 温昌营
 * @Date
 */
@Data
@Accessors(chain = true)
public class DemoVO {
    private String orgStr;
    private String name;
    private String employeeId;
    private String wechatid;
    private String badge;
    private String cellphone;
    private String gender;
    private String email;
    private String corpEmail;

}
