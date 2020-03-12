package com.tester.testeraop.controller;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString(callSuper=true)
@Accessors(chain=true)
public class UserOperationDO {


    private String traceId;
    private Integer deletedReason;

    /** 操作类型;增删改查<-->1,2,3,4 */
    private Integer operationType ;
    /** 操作动作内容 */
    private String operationContent ;
    /** 原始数据;原始数据 */
    private String originalData ;
    /** 新数据;修改后 */
    private String newData ;
    /** 操作人id */
    private Long operatorPersonId ;
    /** 操作人employeeid */
    private String operatorEmployeeId ;
    /** 操作人wechatid */
    private String operatorWechatid ;
    /** 操作人岗位id */
    private Long operatorStationId ;
    /** 数据来源 */
    private Integer dataFrom ;

}
