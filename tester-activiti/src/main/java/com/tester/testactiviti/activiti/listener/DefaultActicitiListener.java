package com.tester.testactiviti.activiti.listener;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 默认流程监听器
 */
@Slf4j
@Component
public class DefaultActicitiListener extends BaseActicitiListener implements SmartInitializingSingleton {

    /**
     * 更新失败最大重试次数
     */
    public static final int TRY_UPDATE_TIMES = 3;

    /**
     * 重试时间间隔，默认100ms
     */
    public static final int DEFAULT_SLEEP_INTERVAL = 100;


    @Autowired
    private ApplicationContext applicationContext;


    @Override
    @Deprecated
    public void whenProcessStart(ActivitiEvent event) {
    }

    /**
     * 流程正常走完。更新流程状态，发布流程完成事件
     *
     * @param event
     * @return void
     * @throws
     * @author 温昌营
     * @date 2019/11/25
     */
    @SneakyThrows
    @Override
    public void whenProcessEnd(ActivitiEvent event) {
        /*log.info("\r\nflow was ended! execution id:{}", event.getExecutionId());
        String procinstId = event.getProcessInstanceId();
        List<TaskAssigneeDO> targetDoList = taskAssigneeService.listTaskAssigneeByProcinstId(procinstId);
        if (CollectionUtils.isEmpty(targetDoList)) {
            log.error("异常。流程对应的审批节点数为空。ProcessInstanceId：{}", procinstId);
            throw new CloudOfficeException(ResultCodeEnum.EMPTY_TASK_ASSIGNEE);
        }

        taskAssigneeService.updateFlowStatus(FlowStatusEnum.COMPLETED.getValue(), procinstId);
//        //发布事件
        log.info("发布流程完成事件，流程id：{}", procinstId);
//        applicationContext.publishEvent(new FlowEvent(procinstId, FlowEventTypeEnum.PROCESS_SUCCESS_END, null));
        flowListenerList.forEach(e -> e.onApplicationEvent(new ProcessSuccessEndFlowEvent(procinstId)));*/
    }


    /**
     * 流程取消，根据流程被拒绝或是被撤回更新流程，并发布事件
     *
     * @param event
     * @return void
     * @throws
     * @author 温昌营
     * @date 2019/11/25
     */
    @SneakyThrows
    @Override
    @Deprecated
    public void whenProcessCancel(ActivitiEvent event) {
    }


    /**
     * 更新任务状态，并发送事件
     *
     * @param taskEntity
     * @return void
     * @throws
     * @author 温昌营
     * @date 2019/11/25
     */
    @SneakyThrows
    @Override
    public void whenTaskCreated(TaskEntity taskEntity) {
        /*String procinstId = taskEntity.getProcessInstanceId();
        String activitiTaskKey = taskEntity.getTaskDefinitionKey();
        String taskId = taskEntity.getId();

        List<TaskAssigneeDO> targetDoList = taskAssigneeService.listTaskAssigneeByProcinstIdAndTaskKey(procinstId, activitiTaskKey);
        if (CollectionUtils.isEmpty(targetDoList)) {
            log.error("数据异常。审批节点数为空。ProcessInstanceId：{}, taskId: {}", procinstId, taskId);
            throw new CloudOfficeException(ResultCodeEnum.EMPTY_TASK_ASSIGNEE);
        }

        *//****************** 原始的 - 只会删除无效用户的taskAssignee ************************************//*
        // 拿到审批人仍然存在的taskAssignee
        List<TaskAssigneeDO> existTask = targetDoList.stream()
                .peek(e -> listenerHelper.updateApprover(e).setTaskStatus(TaskStatusEnum.GOING.getValue()).setActivitiTaskId(taskId))
                .filter(e -> Objects.equals(e.getDeleted(), 0)).collect(Collectors.toList());
        // 更新当前节点的taskAssignee
        taskAssigneeService.assignedTask(targetDoList);
        *//****************** 原始的 ************************************//*

        *//****************** 新的 - 会根据上一级审批人获取当前审批人，并动态添加、删除taskAssignee ************************************//*

//        listenerHelper.updateApprover(targetDoList);
//        targetDoList.forEach(e -> e.setTaskStatus(TaskStatusEnum.GOING.getValue()).setActivitiTaskId(taskId));
//
//        // 更新当前节点的taskAssignee
//        taskAssigneeService.assignedTask(targetDoList);
//        List<TaskAssigneeDO> existTask = targetDoList.stream().filter(e -> Objects.equals(e.getDeleted(), 0)).collect(Collectors.toList());
        *//****************** 新的 ************************************//*


        *//*********** 不需要自动完成任务**********************************//*
//        if (CollectionUtils.isEmpty(existTask)) {
//            // 如果当前节点的所有审批人都不存在了，那么立刻完成当前任务，开始下一个节点
//            activitiService.completeTaskByTaskId(taskId);
//            return;
//        }
        *//*********** 不需要自动完成任务**********************************//*


        List<Long> approverList = existTask.stream()
                .map(e -> e.getApproverUserId()).collect(Collectors.toList());

        //发布事件
//        applicationContext.publishEvent(new TaskCreateFlowEvent(procinstId, FlowEventTypeEnum.TASK_CREATE, null, approverList));
        flowListenerList.forEach(e -> e.onApplicationEvent(new TaskCreateFlowEvent(procinstId, approverList)));*/
    }


    /**
     * 更新状态
     *
     * @param taskEntity
     * @return void
     * @throws
     * @author 温昌营
     * @date 2019/11/25
     */
    @Override
    @SneakyThrows
    @Deprecated
    public void whenTaskCompleted(TaskEntity taskEntity) {
        /*String procinstId = taskEntity.getProcessInstanceId();
        String taskId = taskEntity.getId();

        List<TaskAssigneeDO> targetDoList = taskAssigneeService.listTaskAssigneeByActivitiTaskId(taskId);
        if (CollectionUtils.isEmpty(targetDoList)) {
            log.error("数据异常。审批节点数为空。ProcessInstanceId：{}, taskId: {}", procinstId, taskId);
            throw new CloudOfficeException(ResultCodeEnum.EMPTY_TASK_ASSIGNEE);
        }
        List<Long> approverList = targetDoList.stream().map(e -> e.getApproverUserId()).collect(Collectors.toList());
        applicationContext.getBeansOfType(FlowListener.class)
                .values()
                .forEach(e -> e.onApplicationEvent(new NodeSuccessEndFlowEvent(procinstId, approverList)));*/
    }

    @Override
    public void afterSingletonsInstantiated() {
//        flowListenerList.addAll(applicationContext.getBeansOfType(FlowListener.class).values());
    }
}
