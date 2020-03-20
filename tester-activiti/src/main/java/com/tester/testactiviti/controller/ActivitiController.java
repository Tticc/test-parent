package com.tester.testactiviti.controller;

import com.tester.testercommon.controller.BaseController;
import com.tester.testercommon.controller.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @Author 温昌营
 * @Date
 */
@RestController
@Slf4j
@RequestMapping("/activiti")
public class ActivitiController extends BaseController {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @PostMapping("task/complete")
    public Mono<RestResult<Object>> completeTask(@RequestParam String insId, @RequestParam String content) {
        Task task = taskService.createTaskQuery().processInstanceId(insId).singleResult();
        String msg;
        if(null == task){
            msg = "task completed";
        }else {
            msg = "has next task。taskId:"+task.getId()+", taskName:"+task.getName();
        }
        return monoSuccess(Mono.justOrEmpty(msg));
    }

    @PostMapping("flow/create")
    public Mono<RestResult<Object>> createFlow(@RequestParam String procKey) {
        return monoSuccess(Mono.justOrEmpty(runtimeService.startProcessInstanceByKey(procKey).getId()));
    }

    @PostMapping("flow/delete")
    public Mono<RestResult<Object>> deleteFlow(@RequestParam String procKey) {
        return monoSuccess(Mono.justOrEmpty(runtimeService.startProcessInstanceByKey(procKey).getId()));
    }
}
