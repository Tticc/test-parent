package com.tester.testactiviti.dao.domain;

import com.tester.testercommon.dao.domain.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Table;


@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper=true)
@Accessors(chain=true)
@Table(name="front_form_field")
public class FormFieldDO extends BaseDomain {
    @Override
    public FormFieldDO init(){
        super.init();
        return this;
    }

    /** 表单id */
    private Long formModelId ;
    /** 字段模板id */
    private Long fieldModelId ;
    /** 字段名字 */
    private String formFieldName ;
    /** 字段key */
    private String formFieldKey ;
    /** 序号 */
    private Integer serialNumber ;
    /** 是否条件;0=false,1=true。默认为0 */
    private Integer ifCondition ;

}