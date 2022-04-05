package com.tester.testactiviti.dao.domain;

import com.tester.base.dto.dao.domain.BaseDomain;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Table;

/**
 * 用来做流程模板定义的DO。任务节点
 * @author 温昌营
 * @date 2019/11/5
 */
@Data
@ToString(callSuper=true)
@Accessors(chain=true)
@Table(name = "f_business_task_model")
public class BusinessTaskModelDO extends BaseDomain {
    /**
     * 初始化数据。插入数据的时候调用，查询的时候不需要
     * @return com.aeon.dmc.cloud.office.core.dao.domain.flow.BusinessTaskModelDO
     * @author 温昌营
     * @date 2019/11/14
     */
    @Override
    public BusinessTaskModelDO init(){
        super.init();
        return this;
    }
    /** 流程模板id;f_business_flow_model表id */
    private Long flowModelId ;
    /** activiti_task_key */
    private String activitiTaskKey ;
    /** 序号;序号代表审批的层级位置 */
    private Integer serialNo ;
    /** 节点审批类型;TaskApprovalTypeEnum。1=或签，2=会签。 */
    private Integer taskApprovalType ;
    /** 审批人类型;ApproverTypeEnum。1=组织上级，2=指定角色，3=指定人。 */
    private Integer approverType ;
    /** 审批人类型名;*/
    private String approverTypeName ;
    /** 指定审批角色id;审批角色id。只有当审批人类型是指定角色时使用 */
    private Long specificRoleId ;
    /** 指定审批人id;审批人id。只有当审批人类型是指定审批人时使用 */
    private Long specificUserId ;
}
