package com.tester.testactiviti.controller;

import com.tester.testactiviti.dao.domain.FormFieldConditionDO;
import com.tester.testactiviti.dao.domain.FormFieldDO;
import com.tester.testactiviti.dao.domain.NodeModelDO;
import com.tester.testactiviti.dao.mapper.FormFieldConditionMapper;
import com.tester.testactiviti.dao.mapper.FormFieldMapper;
import com.tester.testactiviti.dao.mapper.MetaFieldMapper;
import com.tester.testactiviti.dao.mapper.NodeModelMapper;
import com.tester.testactiviti.model.request.FormFieldCreateModel;
import com.tester.testactiviti.model.request.NodeConditionCreateModel;
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
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author 温昌营
 * @Date
 */
@RestController
@Slf4j
@RequestMapping("/activiti")
public class ActivitiController extends BaseController {

    private String processKey = "process_key_uuid";
    private Long flowModelId = 1L;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Resource
    private FormFieldConditionMapper formFieldConditionMapper;
    @Resource
    private MetaFieldMapper metaFieldMapper;
    @Resource
    private NodeModelMapper nodeModelMapper;
    @Resource
    private FormFieldMapper formFieldMapper;

    @PostMapping("/testConnect")
    public Mono<RestResult<Object>> testConnect() {
        return monoSuccess();
    }

    @PostMapping("/createFlowModel")
    public Mono<RestResult<Object>> createFlowModel(@RequestBody @Valid FlowModelCreateModel model) {
        // todo activiti flow id 可以考虑存放到枚举里面
        String activitiFlowId = "cycle_flow";
        // 1.持久化流程模板(可拆分)。假设已完成
        Long flowModelId = 1L;
        // 2.持久化表单模板(可拆分)。假设已完成
        Long formModelId = 1L;
        // 3.持久化 节点模板 和 表单-字段模板(可拆分)
        List<FormFieldDO> newFormFields = generateFormField(model.getFormFields(),formModelId);
        List<NodeModelDO> newNodeModels = generateNodeModel(model.getNodes(),flowModelId,activitiFlowId);
        formFieldMapper.insertList(newFormFields);
        nodeModelMapper.insertList(newNodeModels);
        // 4.持久化条件模板(可拆分)
        List<FormFieldConditionDO> conditionDOS = generateFlowFieldCondition(model.getNodes());
        formFieldConditionMapper.insertList(conditionDOS);
        return monoSuccess(Mono.justOrEmpty("success"));
    }

    @PostMapping("/instanceFlow")
    public Mono<RestResult<Object>> instanceFlow(@RequestBody @Valid FlowModelCreateModel model) {
        // todo 创建流程，注意case id，考虑case id使用的地方

        // 5.插入表单值
        // 6.根据表单值初始化 task_assignee表
        return monoSuccess(Mono.justOrEmpty("success"));
    }
/*
    @PostMapping("flow/createModel")
    public Mono<RestResult<Object>> createModel(@RequestBody @Valid FlowModelCreateModel model) {
        List<FormFieldConditionDO> ffc = new ArrayList<>();
        List<NodeModelDO> nodeModelDOS = new ArrayList<>();
        List<NodeCreateModel> nodeModels = model.getNodes();
        for (NodeCreateModel nodeModel : nodeModels) {
            NodeModelDO node = new NodeModelDO().init();
            node.setFlowModelId(flowModelId) // todo 这个可以应该是新生成的，无法从前端获得
                    .setCoopType(nodeModel.getCoopType())
                    .setSpecificIdList(String.valueOf(nodeModel.getSpecificIdList())) // todo 处理list
                    .setSerialNumber(nodeModel.getSerialNumber())
                    .setNodeType(nodeModel.getNodeType())
                    .setNodeKey(nodeModel.getNodeKey())
                    .setApproverType(nodeModel.getApproverType())
                    .setActivitiFlowId(processKey)// todo 后台设置
                    .setNextNodeKeyList(String.valueOf(nodeModel.getNextNodeKeyList()))// todo 处理list
            ;
            NodeConditionCreateModel conditionModel = nodeModel.getCondition();
            if (conditionModel != null) {
                FormFieldConditionDO condition = new FormFieldConditionDO().init();
                condition.setCheckType(conditionModel.getCheckType())
                        .setCheckValue(conditionModel.getCheckValue())
                        .setFalseNext(conditionModel.getFalseNext())
                        .setFieldModelId(conditionModel.getfo())
                        .setFlowModelId(flowModelId)// todo 这个无法从前端获得
                        .setIfCondition(conditionModel.getIfCondition())
                        .setNodeKey(nodeModel.getNodeKey())
                        .setTrueNext(conditionModel.getTrueNext());
                ffc.add(condition);
            }
            nodeModelDOS.add(node);
        }
        Tuple2<List<FormFieldConditionDO>, List<NodeModelDO>> tt = Tuples.of(ffc, nodeModelDOS);
        return monoSuccess(Mono.justOrEmpty(tt));
    }*/

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
        if (null == task) {
            msg = "task completed";
        } else {
            msg = "has next task。taskId:" + task.getId() + ", taskName:" + task.getName();
        }
        return monoSuccess(Mono.justOrEmpty(msg));
    }



    private List<FormFieldDO> generateFormField(List<FormFieldCreateModel> formFieldModels,Long formModelId){

        List<FormFieldDO> newFormFields = new ArrayList<>();
        for (FormFieldCreateModel formField : formFieldModels) {
            FormFieldDO formFieldDO = new FormFieldDO().init();
            formFieldDO.setFieldModelId(formField.getFieldModelId())
                    .setFormModelId(formModelId)
                    .setIfCondition(0)
                    .setFormFieldKey(formField.getKey())
                    .setFormFieldName(formField.getName())
                    .setSerialNumber(formField.getSerialNumber());
            newFormFields.add(formFieldDO);
        }
        return newFormFields;
    }

    private List<NodeModelDO> generateNodeModel(List<NodeCreateModel> nodes, Long flowModelId, String activitiFlowId) {
        List<NodeModelDO> result = new ArrayList<>();
        for (NodeCreateModel node : nodes) {
            NodeModelDO newNode = new NodeModelDO().init();
            newNode
                    .setFlowModelId(flowModelId)
                    .setActivitiFlowId(activitiFlowId)
                    .setNodeKey(node.getNodeKey())
                    .setNodeType(node.getNodeType())
                    .setSerialNumber(node.getSerialNumber())
                    // 可能不需要
                    .setNextNodeKeyList(String.valueOf(node.getNextNodeKeyList()))
                    ;
            if(Objects.equals(node.getNodeType(),1)) {
                // 如果是任务节点，需要设置审批人数据
                newNode.setApproverType(node.getApproverType())
                        .setSpecificIdList(String.valueOf(node.getSpecificIdList()))
                        .setCoopType(node.getCoopType());
            }
            result.add(newNode);
        }
        return result;
    }

    private List<FormFieldConditionDO> generateFlowFieldCondition(List<NodeCreateModel> nodes) {
        List<FormFieldConditionDO> result = new ArrayList<>();
        for (NodeCreateModel node : nodes) {
            if(Objects.equals(node.getNodeType(),2)){
                // 如果是条件节点，生成条件
                NodeConditionCreateModel condition = node.getCondition();
                FormFieldConditionDO newFormField = new FormFieldConditionDO().init();
                newFormField.setCheckType(condition.getCheckType())
                        .setCheckValue(condition.getCheckValue())
                        .setFalseNext(condition.getFalseNext())
                        .setFalseNextId(nodeModelMapper.selectIdByNodeKey(condition.getFalseNext()))
                        .setTrueNext(condition.getTrueNext())
                        .setTrueNextId(nodeModelMapper.selectIdByNodeKey(condition.getTrueNext()))
                        .setNodeKey(node.getNodeKey())
                        .setNodeId(nodeModelMapper.selectIdByNodeKey(node.getNodeKey()))
                        .setFormFieldKey(condition.getFormFieldKey())
                        .setFormFieldId(formFieldMapper.selectIdByFormFieldKey(condition.getFormFieldKey()));
                result.add(newFormField);
            }
        }
        return result;
    }
}
