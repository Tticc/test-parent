package com.tester.testactiviti;

import com.tester.testactiviti.dao.domain.TaskAssigneeDO;
import com.tester.testactiviti.dao.mapper.TaskAssigneeMapper;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author 温昌营
 * @Date
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestActivitiApplication.class)
@Slf4j
public class ActivitiTest {
    private static final String START_EVENT_ID = "start";
    private static final String START_TASK_KEY = "startTask";
    private static final String GATE_WAY_ID = "exGateway";
    private static final String GATE_WAY_END_EXPRESS = "${conditionEntity.getOrderReceived(procId) == false}";
    private static final String GATE_WAY_CONTINUE_EXPRESS = "${conditionEntity.getOrderReceived(procId) == true}";
    private static final String USER_TASK_ID = "user_task";
    private static final String END_EVENT_ID = "end";
    private static final String END_TASK_KEY = "endTask";

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    private String processKey = "process_key_uuid";

    @Resource
    private TaskAssigneeMapper taskAssigneeMapper;
    @Test
    public void test_size(){

        List<TaskAssigneeDO> select = taskAssigneeMapper.selectAll();
        System.out.println("select.size():"+select.size());
    }

    @Test
    public void test_createModel(){
        BpmnModel model = tcreatFlow(processKey);
        Deployment deploy = repositoryService.createDeployment()
                .addBpmnModel(processKey + ".bpmn", model)
                .name(processKey).deploy();
        String id = deploy.getId();
        System.out.println("deloy id:"+id);
//        startProcess(processKey);


    }
    @Test
    public void start_process(){
        startProcess(processKey);
    }

    public void startProcess(String procKey){
        String id = runtimeService.startProcessInstanceByKey(procKey).getId();
        System.out.println("instance id:"+id);
        while (completeTask(id)){
            System.out.println("complete task");
        }
    }
    public boolean completeTask(String insId){
        Task taskByProcId = taskService.createTaskQuery().processInstanceId(insId).singleResult();
        if(null == taskByProcId){
            return false;
        }
        String taskId = taskByProcId.getId();
        System.out.println("taskId:"+taskId+", taskName:"+taskByProcId.getName());
        if(Objects.equals(taskByProcId.getName(),START_TASK_KEY)){
            Map<String,Object> map = new HashMap<>();
            map.put("procId",insId);
            taskService.complete(taskId,map);
        }else {

            taskService.complete(taskId);
        }
        return true;
    }



    public static BpmnModel tcreatFlow(String processKey){
        Process process = new Process();
        process.setId(processKey);
        // start event
        process.addFlowElement(createStartEvent(START_EVENT_ID));
        // link
        process.addFlowElement(createSequenceFlow(START_EVENT_ID, START_TASK_KEY));
        // start task
        process.addFlowElement(createUserTask(START_TASK_KEY, START_TASK_KEY));
        // link
        process.addFlowElement(createSequenceFlow(START_TASK_KEY, GATE_WAY_ID));
        // gate way
        process.addFlowElement(generateExclusiveGateway(GATE_WAY_ID,GATE_WAY_ID));
        process.addFlowElement(createSequenceFlow(GATE_WAY_ID, USER_TASK_ID,GATE_WAY_CONTINUE_EXPRESS));
        process.addFlowElement(createSequenceFlow(GATE_WAY_ID, END_EVENT_ID,GATE_WAY_END_EXPRESS));

        process.addFlowElement(createUserTask(USER_TASK_ID, USER_TASK_ID));
        process.addFlowElement(createSequenceFlow(USER_TASK_ID, GATE_WAY_ID));

        process.addFlowElement(createEndEvent(END_EVENT_ID));

        BpmnModel model = new BpmnModel();
        model.addProcess(process);
        return model;
    }
    /*public static BpmnModel createFlowModel(String processKey, List<ActivitiTaskModel> userTaskLists){
        // length 是真正的审批节点， 包含起始和终止就是length+2
        *//*
        这里之所以加一个 startTask节点，是为了做缓冲。
        因为activiti的流程在启动之后就会立刻开始task，而我需要手动控制审批task的开始，所以加了一个单纯缓冲用的task，
        一旦我认为时机合适，我就会将这个task complete，开始真正的审批节点，触发审批相关的事件。
         *//*
        process.addFlowElement(createSequenceFlow("start", ActivitiFlowModel.START_TASK_KEY));
        process.addFlowElement(createUserTask(ActivitiFlowModel.START_TASK_KEY, ActivitiFlowModel.START_TASK_KEY));
        keys[0] = ActivitiFlowModel.START_TASK_KEY;
        keys[length + 1] = "end";
        for (int i = 0; i < length; i++) {
            ActivitiTaskModel activitiTaskModels = userTaskList.get(i);
            String key = activitiTaskModel.getTaskKey();
            keys[i+1] = key;
            process.addFlowElement(createSequenceFlow(keys[i], key));
            process.addFlowElement(activitiTaskModel.genereateActivitiUserTask());
        }
        process.addFlowElement(createSequenceFlow(keys[length], keys[length + 1]));
        process.addFlowElement(createEndEvent("end"));
        return model;
    }*/
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
