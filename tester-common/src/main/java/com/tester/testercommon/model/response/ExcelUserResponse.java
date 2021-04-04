package com.tester.testercommon.model.response;

import com.tester.testercommon.model.BaseDTO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author 温昌营
 * @Date
 */
@Data
@Accessors(chain = true)
public class ExcelUserResponse extends BaseDTO {
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
