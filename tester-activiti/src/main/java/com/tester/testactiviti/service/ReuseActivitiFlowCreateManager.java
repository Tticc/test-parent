package com.tester.testactiviti.service;

import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Author 温昌营
 * @Date
 */
@Service
@Slf4j
public class ReuseActivitiFlowCreateManager {
    private static final String START_EVENT_ID = "start";
    private static final String START_TASK_KEY = "reuse_start_task";
    private static final String GATE_WAY_ID = "reuse_ex_gateway";
    private static final String GATE_WAY_END_EXPRESS = "${conditionEntity.getOrderReceived(procId) == false}";
    private static final String GATE_WAY_CONTINUE_EXPRESS = "${conditionEntity.getOrderReceived(procId) == true}";
    private static final String USER_TASK_ID = "reuse_user_task";
    private static final String END_EVENT_ID = "end";

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    public static final String REUSE_PROCESS_KEY = "reuse_process_key_uuid_1584953640420";


    public void createActivitiFlowModel(){
        String processKey = REUSE_PROCESS_KEY;
        BpmnModel model = tcreatFlow(processKey);
        Deployment deploy = repositoryService.createDeployment()
                .addBpmnModel(processKey + ".bpmn", model)
                .name(processKey).deploy();
        String id = deploy.getId();
        log.info("deploy id:{}",id);
        testProcess(processKey);
    }
    private void testProcess(String procKey){
        String id = runtimeService.startProcessInstanceByKey(procKey).getId();
        log.info("process instance id:{}",id);
        runtimeService.deleteProcessInstance(id,"testing deleted after flow deployed");
    }

    public void completeStartTask(String procInstId){
        completeStartTask(procInstId,START_TASK_KEY);
    }
    public void completeStartTask(String procInstId, String taskKey){
        String id = taskService.createTaskQuery().processInstanceId(procInstId).taskDefinitionKey(taskKey).singleResult().getId();
        if(StringUtils.isEmpty(id)){
            taskService.complete(id);
        }
        log.info("completed start task。id:{}",id);
    }

    private BpmnModel tcreatFlow(String processKey){
        Process process = new Process();
        process.setId(processKey);
        // 开始节点
        process.addFlowElement(createStartEvent(START_EVENT_ID));
        // 连接。启动event -> 初始任务
        process.addFlowElement(createSequenceFlow(START_EVENT_ID, START_TASK_KEY));
        // 初始任务
        process.addFlowElement(createUserTask(START_TASK_KEY, START_TASK_KEY));
        // 连接。初始任务 -> 排他网关
        process.addFlowElement(createSequenceFlow(START_TASK_KEY, GATE_WAY_ID));
        // 排他网关
        process.addFlowElement(generateExclusiveGateway(GATE_WAY_ID,GATE_WAY_ID));
        // 连接。网关 -> 用户任务节点
        process.addFlowElement(createSequenceFlow(GATE_WAY_ID, USER_TASK_ID,GATE_WAY_CONTINUE_EXPRESS));
        // 连接。网关 -> 终止节点
        process.addFlowElement(createSequenceFlow(GATE_WAY_ID, END_EVENT_ID,GATE_WAY_END_EXPRESS));
        // 用户任务节点
        process.addFlowElement(createUserTask(USER_TASK_ID, USER_TASK_ID));
        // 连接。任务 -> 排他网关
        process.addFlowElement(createSequenceFlow(USER_TASK_ID, GATE_WAY_ID));
        // 终止节点
        process.addFlowElement(createEndEvent(END_EVENT_ID));
        BpmnModel model = new BpmnModel();
        model.addProcess(process);
        return model;
    }
    public static StartEvent createStartEvent(String id) {
        StartEvent startEvent = new StartEvent();
        startEvent.setId(id);
        return startEvent;
    }
    public static SequenceFlow createSequenceFlow(String from, String to){
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        return flow;
    }
    public static SequenceFlow createSequenceFlow(String from, String to,String express){
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        flow.setConditionExpression(express);
        return flow;
    }
    public static EndEvent createEndEvent(String id) {
        EndEvent endEvent = new EndEvent();
        endEvent.setId(id);
        return endEvent;
    }
    public static UserTask createUserTask(String id, String name){
        UserTask userTask = new UserTask();
        userTask.setId(id);
        userTask.setName(name);
        return userTask;
    }
    public static ExclusiveGateway generateExclusiveGateway(String id, String name){
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(id);
        exclusiveGateway.setName(name);
        return exclusiveGateway;
    }
}
