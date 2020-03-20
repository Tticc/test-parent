package com.tester.testactiviti.activiti.condiction;

import com.tester.testactiviti.activiti.annotation.ActivitiParamBean;
import com.tester.testactiviti.dao.domain.TaskAssigneeDO;
import com.tester.testactiviti.dao.mapper.TaskAssigneeMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author 温昌营
 * @Date
 */
@ActivitiParamBean("conditionEntity")
public class ConditionEntity implements Serializable {
    @Resource
    private TaskAssigneeMapper taskAssigneeMapper;

    private boolean orderReceived = true;
    private int count = 0;

    public boolean getOrderReceived(String procId){
        System.out.println("Entity中的流程实例id："+procId);
        List<TaskAssigneeDO> select = taskAssigneeMapper.selectAll();
//        List<TaskAssigneeDO> goingTasks = select.stream().filter(e -> Objects.equals(e.getTaskStatus(), 2)).collect(Collectors.toList());
        System.out.println("进行中的任务项："+select.size());
        // todo 外部传参是必要条件
        if(count++ < 3){
            return true;
        }
        return false;
    }
    public void setOrderReceived(boolean orderReceived){
        this.orderReceived = orderReceived;
    }

    public Integer days(){
        return (int)(Math.random()*2+1);
    }
}
