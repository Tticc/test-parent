package com.tester.testactiviti.dao.domain;

import com.tester.base.dto.dao.domain.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Table;
import java.util.Date;

/**
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper=true)
@Accessors(chain=true)
@Table(name = "f_task_assignee")
public class TaskAssigneeDO extends BaseDomain {
    /**
     * 初始化数据。插入数据的时候调用，查询的时候不需要
     * @return com.xxx.xxx.xxx.office.core.dao.domain.flow.TaskAssigneeDO
     * @author 温昌营
     * @date 2019/11/14
     */
    @Override
    public TaskAssigneeDO init(){
        super.init();
        this.depth = 0;
        this.flowStatus = 20; // 进行中
        this.taskApprovalType = 1; // 或签
        this.approverType = 2; // 指定人
        this.taskStatus = 1; // 未开始
        this.approvalStatus = 1; // 未审批
        return this;
    }

    /********************** flow ***************************************/
    /** caseid。业务数据 */
    private Long caseId;
    /** sub_case_id。业务数据 */
    private Long subCaseId ;
    /** sub_case_type。业务数据;1=正常出差，2=超额，3=改签... */
    private Integer subCaseType ;
    /** 流程类型id。model的数据;1=正常出差，2=超额出差，3=报销... */
    private Integer flowTypeId ;
    /** 申请人岗位id。人员数据;申请人岗位id */
    private Long requesterStationId;
    /** 申请人员工id。人员数据;申请人员工id */
    private Long requesterUserId ;
    /** activiti流程id */
    private String activitiProcinstId ;
    /** 当前流程状态;1=进行中，2=完成，3=撤回，4=拒绝 xxxEnum */
    private Integer flowStatus ;
    /********************** flow ***************************************/


    /********************** task**********************************/
    /** activiti_task_id。运行数据 */
    private String activitiTaskId ;
    /** activiti任务key。model的数据;model的数据 */
    private String activitiTaskKey ;
    /** 当前任务状态。审批数据; TaskStatusEnum */
    private Integer taskStatus ;
    /** 序号。model的数据;一个case中同一个depth的所有拥有相同序号的记录看作是同一个节点。每一层的每一个节点的序号是唯一的 */
    private Integer serialNo ;
    /** 父节点序号。审批数据;当前节点的父级节点，父节点的深度比当前深度小1 */
    private Integer pnodeSerialNo ;
    /** 深度;起始深度为0 */
    private Integer depth ;
    /** 审批类型会签或签。model的数据; TaskApprovalTypeEnum */
    private Integer taskApprovalType ;
    /** 审批人类型。角色，岗位，上级。model的数据; ApproverTypeEnum */
    private Integer approverType ;
    /** 审批人类型。角色，岗位，上级。model的数据; ApproverTypeEnum */
    private String approverTypeName ;
    /********************** task**********************************/


    /********************** task assignee**********************************/
    /** 审批人岗位id。人员数据 */
    private Long approverStationId ;
    /** 审批人员工id。运行数据;审批人员工id。任务分配时设置 */
    private Long approverUserId ;
    /** 审批意见。审批数据 */
    private String currentComments ;
    /** 当前用户审批状态。审批数据;ApprovalStatusEnum */
    private Integer approvalStatus ;
    /** 审批操作时间;审批执行时间 */
    private Date operationTime;
    /********************** task assignee**********************************/
}
