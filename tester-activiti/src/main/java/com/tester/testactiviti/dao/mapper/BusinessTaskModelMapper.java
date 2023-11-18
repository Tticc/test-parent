package com.tester.testactiviti.dao.mapper;

import com.tester.testactiviti.dao.domain.BusinessTaskModelDO;
import com.tester.base.dto.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BusinessTaskModelMapper extends BaseMapper<BusinessTaskModelDO,Long> {

    /**
     * 返回所有的任务节点模板数据
     * @return List<TaskAssigneeDO>
     */
    List<BusinessTaskModelDO> listAll();


    /**
     * 根据FlowModelId返回任务节点模板数据
     * @param flowModelId
     * @return java.util.List<com.xxx.xxx.xxx.office.core.dao.domain.flow.BusinessTaskModelDO>
     * @author 温昌营
     * @date 2019/11/7
     */
    List<BusinessTaskModelDO> listTaskModeByFlowModelId(@Param("flowModelId") Long flowModelId);


}
