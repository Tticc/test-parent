package com.tester.testercv.draw.basic;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FormFieldConditionDO {
    /** 流程模板id;冗余字段 */
    private Long flowModelId ;
    /** 表单-字段关系表id;可能并不需要 */
    private Long formFieldId ;
    /** 表单-字段关系表key */
    private String formFieldKey ;
    /** 字段类型;冗余字段 */
    private Integer fieldType ;
    /** 使用节点id */
    private Long nodeId ;
    /** 使用节点key;在哪个节点使用这个条件 */
    private String nodeKey ;
    /** 判断类型;1=大于，2=小于，3=等于 */
    private Integer checkType ;
    /** 比较的值 */
    private String checkValue ;
    /** 结果为真时的下一个节点key */
    private String trueNext ;
    /** 结果为真时的下一个节点id */
    private Long trueNextId ;
    /** 结果为假时的下一个节点key */
    private String falseNext ;
    /** 结果为假时的下一个节点id */
    private Long falseNextId ;

}
