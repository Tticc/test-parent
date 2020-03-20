package com.tester.testactiviti.activiti.listener;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

/**
 * activiti流程监听器
 */
public abstract class BaseActicitiListener implements ActivitiEventListener {
    @Override
    public void onEvent(ActivitiEvent event) {
        switch (event.getType()) {
            case PROCESS_STARTED:
//                whenProcessStart(event);
                break;
            case PROCESS_COMPLETED:
                whenProcessEnd(event);
                break;
            case PROCESS_CANCELLED:
                whenProcessCancel(event);
                break;
            case TASK_CREATED:
                TaskEntity entity = (TaskEntity) ((ActivitiEntityEvent) event).getEntity();
                // 如果是初始缓冲节点，结束

                whenTaskCreated(entity);
                break;
            case TASK_COMPLETED:
                TaskEntity entity2 = (TaskEntity) ((ActivitiEntityEvent) event).getEntity();
                // 如果是初始缓冲节点，结束

                whenTaskCompleted(entity2);
                break;
            default:
                break;
        }

    }

    @Override
    public boolean isFailOnException() {
        return true;
    }

    /**
     * 流程开始
     *
     * @param event
     */
    public abstract void whenProcessStart(ActivitiEvent event);

    /**
     * 流程结束
     *
     * @param event
     */
    public abstract void whenProcessEnd(ActivitiEvent event);
    /**
     * 流程结束
     *
     * @param event
     */
    public abstract void whenProcessCancel(ActivitiEvent event);

    /**
     * task创建
     *
     * @param taskEntity
     */
    public abstract void whenTaskCreated(TaskEntity taskEntity);

    /**
     * task完成
     *
     * @param taskEntity
     */
    public abstract void whenTaskCompleted(TaskEntity taskEntity);

}
