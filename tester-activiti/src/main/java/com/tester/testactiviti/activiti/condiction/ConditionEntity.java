package com.tester.testactiviti.activiti.condiction;

import com.tester.testactiviti.activiti.annotation.ActivitiParamBean;

import java.io.Serializable;

/**
 * @Author 温昌营
 * @Date
 */
@ActivitiParamBean("conditionEntity")
public class ConditionEntity implements Serializable {
    private boolean orderReceived = true;
    private int count = 0;
    public boolean getOrderReceived(){
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
