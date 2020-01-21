package com.tester.testwebapp.dao.domain;

import com.tester.testercommon.dao.domain.BaseDomain;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Table;

/**
 * @Author 温昌营
 * @Date
 */
@Data
@Accessors(chain = true)
@Table(name = "u_person")
public class UserDomain extends BaseDomain {
    /** 员工号;员工号 */
    private String employeeId ;

    /***************************** ext包含的数据 **********************/
    /** ext_person_id;外部人员编号 */
    private String extPersonId ;
    /** 名字 */
    private String name ;
    /** 手机号 */
    private String cellphone ;
    /** 性别 1=男，0=女*/
    private Integer gender ;
    /** 邮箱 */
    private String email ;
    /** 英文名 */
    private String enname ;
    /** 状态 */
    private Integer status ;
    /***************************** ext包含的数据 **********************/


    /** 微信ID */
    private String wechatid ;
    /** 备注 */
    private String note ;
    /** 数据来源;PlatformEnum */
    private Integer dataFrom ;
}
