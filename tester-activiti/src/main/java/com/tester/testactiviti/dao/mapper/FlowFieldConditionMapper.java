package com.tester.testactiviti.dao.mapper;

import com.tester.testactiviti.dao.domain.FlowFieldConditionDO;
import com.tester.testercommon.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FlowFieldConditionMapper extends BaseMapper<FlowFieldConditionDO,Long> {





/*
    *//**
     * 返回所有的审批关系
     * @return List<TaskAssigneeDO>
     *//*
    List<TaskAssigneeDO> listAll();

    List<TaskAssigneeDO> listTaskAssigneeByCondition(TaskAssigneeDO taskAssigneeDo);

    List<TaskAssigneeDO> listTaskAssigneeByActivitiTaskId(@Param("taskId") String taskId);
    *//**
     * 按caseType统计待办/已办。返回分类及数量
     * @param approverUserId 必需
     * @param completed true-已办、false-待办
     * @param caseType 查所有类型传null
     * @param startTime startTime和endTime传null查所有
     * @param endTime startTime和endTime传null查所有
     * @return java.util.List<com.aeon.dmc.cloud.office.common.model.response.flow.TodoGroupVO>
     * @author 温昌营
     * @date 2019/11/25
     *//*
    List<TodoGroupVO> countTaskGroupByCaseType(@Param("approverUserId") Long approverUserId,
                                               @Param("completed") boolean completed,
                                               @Param("caseType") Integer caseType,
                                               @Param("startTime") Date startTime,
                                               @Param("endTime") Date endTime);

    *//**
     * 获取审批人待办/已办。返回流程id、任务分配时间
     * @param approverUserId 必需
     * @param completed true-已办、false-待办
     * @param caseType 查所有类型传null
     * @param startTime startTime和endTime传null查所有
     * @param endTime startTime和endTime传null查所有
     * @return java.util.List<com.aeon.dmc.cloud.office.core.model.bo.flow.TodoCaseBO>
     * @throws
     * @author 温昌营
     * @date 2019/11/25
     *//*
    List<TodoCaseBO> listTask(@Param("approverUserId") Long approverUserId,
                              @Param("completed") boolean completed,
                              @Param("caseType") Integer caseType,
                              @Param("startTime") Date startTime,
                              @Param("endTime") Date endTime);

    *//**
     * 统计待办/已办，返回数量
     * @param approverUserId 必需
     * @param completed true-已办、false-待办
     * @param startTime startTime和endTime传null查所有
     * @param endTime startTime和endTime传null查所有
     * @return java.lang.Integer
     * @throws
     * @author 温昌营
     * @date 2019/11/25
     *//*
    Integer countTask(@Param("approverUserId") Long approverUserId,
                      @Param("completed") boolean completed,
                      @Param("startTime") Date startTime,
                      @Param("endTime") Date endTime);


    *//** 检查当前用户能否被转审*//*
    Integer checkCanBeShifted(@Param("activitiProcinstId") String activitiProcinstId, @Param("targetUserId") Long targetUserId);

    Integer countByProcinstIdAndUserId(@Param("activitiProcinstId") String activitiProcinstId, @Param("userId") Long userId);

    *//**
     * 根据procinstId 和 审批人用户id获取当前进行中任务节点的所有审批数据。
     * @param activitiProcinstId
     * @param approverUserId
     * @return java.util.List<com.aeon.dmc.cloud.office.core.dao.domain.flow.TaskAssigneeDO>
     * @throws
     * @author 温昌营
     * @date 2019/11/28
     *//*
    List<TaskAssigneeDO> listTaskByProcinstIdAndApproverUserId(@Param("activitiProcinstId") String activitiProcinstId, @Param("approverUserId") Long approverUserId);

    *//** 获取上一个当前类型的节点的序号*//*
    @Select("select max(serial_no) from f_task_assignee as t " +
            "where t.activiti_procinst_id = #{activitiProcinstId} and t.serial_no < #{serialNo} and t.approver_type = #{approverType} " +
            "and t.deleted = 0 \n" +
            "group by t.approver_type\n")
    Integer getPreviousNodeSerialNo(@Param("activitiProcinstId") String activitiProcinstId, @Param("approverType") Integer approverType, @Param("serialNo") Integer serialNo);





    *//**************** update area ************************************************//*
    *//**
     * 更新流程所有审批节点的状态
     * @param flowStatus
     * @param activitiProcinstId
     * @return int
     * @throws
     * @author 温昌营
     * @date 2019/11/29
     *//*
    Integer updateFlowStatus(@Param("flowStatus") Integer flowStatus, @Param("activitiProcinstId") String activitiProcinstId);

    *//**
     * 更新任务所有审批节点的状态
     * @param taskStatus 新状态
     * @param activitiProcinstId 条件-流程id
     * @param activitiTaskId 条件-任务id
     * @return int
     * @throws
     * @author 温昌营
     * @date 2019/11/29
     *//*
    Integer updateTaskStatus(@Param("taskStatus") Integer taskStatus,
                             @Param("activitiProcinstId") String activitiProcinstId,
                             @Param("activitiTaskId") String activitiTaskId);

    *//**
     * 任务创建时，设置任务状态、设置activitiTaskId、更新审批人信息
     * @param taskAssigneeDoList
     * @return int
     * @throws
     * @author 温昌营
     * @date 2019/11/29
     *//*
    Integer assignedTask(List<TaskAssigneeDO> taskAssigneeDoList);*/

}
