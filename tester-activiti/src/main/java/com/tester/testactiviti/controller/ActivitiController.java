package com.tester.testactiviti.controller;

import com.tester.testactiviti.dao.domain.FlowFieldConditionDO;
import com.tester.testactiviti.dao.domain.NodeModelDO;
import com.tester.testactiviti.dao.mapper.FlowFieldConditionMapper;
import com.tester.testactiviti.dao.mapper.MetaFieldMapper;
import com.tester.testactiviti.dao.mapper.NodeModelMapper;
import com.tester.testactiviti.model.request.FieldConditionCreateModel;
import com.tester.testactiviti.model.request.FlowModelCreateModel;
import com.tester.testactiviti.model.request.NodeCreateModel;
import com.tester.testercommon.controller.BaseController;
import com.tester.testercommon.controller.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
    @Resource
    private FlowFieldConditionMapper flowFieldConditionMapper;
    @Resource
    private MetaFieldMapper metaFieldMapper;
    @Resource
    private NodeModelMapper nodeModelMapper;

    @PostMapping("/testConnect")
    public Mono<RestResult<Object>> testConnect() {
        return monoSuccess();
    }

    @PostMapping("flow/createModel")
    public Mono<RestResult<Object>> createModel(@RequestBody @Valid FlowModelCreateModel model) {
        List<FlowFieldConditionDO> ffc = new ArrayList<>();
        List<NodeModelDO> nodeModelDOS = new ArrayList<>();
        List<NodeCreateModel> nodes = model.getNodes();
        for (NodeCreateModel node : nodes) {
            NodeModelDO init1 = new NodeModelDO().init();
            init1.setFlowModelId(0L) // todo 这个可以应该是新生成的，无法从前端获得
                    .setCoopType(node.getCoopType())
                    .setSpecificIdList(node.getSpecificIdList().toString()) // todo 处理
                    .setSerialNumber(node.getSerialNumber())
                    .setNodeType(node.getNodeType())
                    .setNodeKey(node.getNodeKey())
                    .setApproverType(node.getApproverType())
                    .setActivitiFlowId("null")// todo 后台设置
                    .setNextNodeKeyList(node.getNextNodeKeyList().toString())// todo 处理
                    ;
            nodeModelDOS.add(init1);
            List<FieldConditionCreateModel> conditions = node.getConditions();
            for (FieldConditionCreateModel condition : conditions) {
                FlowFieldConditionDO cond = new FlowFieldConditionDO().init();
                cond.setCheckType(condition.getCheckType())
                        .setCheckValue(condition.getCheckValue())
                        .setFalseNext(condition.getFalseNext())
                        .setFieldModelId(condition.getFieldModelId())
                        .setFlowModelId(0L)// todo 这个可以应该是新生成的，无法从前端获得
                        .setIfCondition(condition.getIfCondition())
                        .setNodeKey(node.getNodeKey())
                        .setTrueNext(condition.getTrueNext());
                ffc.add(cond);
            }
        }
        Tuple2<List<FlowFieldConditionDO>,List<NodeModelDO>> tt = Tuples.of(ffc,nodeModelDOS);
        return monoSuccess(Mono.justOrEmpty(tt));
    }

    @PostMapping("/flow/createFLowInstance")
    public Mono<RestResult<Object>> createFLowInstance(@RequestParam String procKey) {
        return monoSuccess(Mono.justOrEmpty("createFLowInstance"));
    }

    @PostMapping("/flow/deleteFLowInstance")
    public Mono<RestResult<Object>> deleteFLowInstance(@RequestParam String procKey) {
        return monoSuccess(Mono.justOrEmpty("deleteFLowInstance"));
    }


    @PostMapping("/task/complete")
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
}
