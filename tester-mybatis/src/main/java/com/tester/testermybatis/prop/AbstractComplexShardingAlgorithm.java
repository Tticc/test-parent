package com.tester.testermybatis.prop;

import com.tester.testercommon.util.SpringBeanContextUtil;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author 温昌营
 * @Date 2020-8-21 10:05:12
 */
public abstract class AbstractComplexShardingAlgorithm {

    static final String FIELD_MEMBER_ID = "member_id";

    static final String FIELD_ORDER_NO = "order_no";

    /**
     * 分库数量。
     */
    private Integer allDbNum;

    /**
     * strict=false时，每个db实例分片数量
     */
    private Integer tableShardingNum;

    public Integer getAllDbNum() {
        return allDbNum;
    }

    public Integer getTableShardingNum() {
        return tableShardingNum;
    }

    Collection<String> getTargetList(Collection<String> availableTargetNames, ComplexKeysShardingValue<Long> complexKeysShardingValue){
        if(allDbNum == null || tableShardingNum == null){
            AtomicReference<MyDatabaseProperties> properties = new AtomicReference<>(SpringBeanContextUtil.getBean(MyDatabaseProperties.class));
            allDbNum = properties.get().getAllDbNum();
            tableShardingNum = properties.get().getShardingNumPerDb();
        }
        Map<String,Collection<Long>> shardingValueMap = complexKeysShardingValue.getColumnNameAndShardingValuesMap();
        List<String> targetList = new ArrayList<>();
        if(CollectionUtils.isEmpty(shardingValueMap)){
            return targetList;
        }
        String logicTableName = complexKeysShardingValue.getLogicTableName();
        for (Map.Entry<String, Collection<Long>> entry : complexKeysShardingValue.getColumnNameAndShardingValuesMap().entrySet()) {
            for (Long val : entry.getValue()){
                String targetName = getTargetName(availableTargetNames,logicTableName,entry.getKey(),val);
                if(null != targetName){
                    targetList.add(targetName);
                }
            }
        }
        return targetList;
    }

    private String getTargetName(Collection<String> availableTargetNames, String logicTableName, String columnName, Long value){
        Long shardValue;
        if(Objects.equals(columnName,FIELD_ORDER_NO)){
            shardValue = value % 1000;
        }else if(Objects.equals(columnName,FIELD_MEMBER_ID)){
            shardValue = value;
        }else{
            return null;
        }
        String suffix = getSuffix(logicTableName, columnName, shardValue);
        for(String availableTargetName : availableTargetNames){
            if(suffix != null && availableTargetName.endsWith(suffix)){
                return availableTargetName;
            }
        }
        return null;
    }

    abstract String getSuffix(String logicTableName, String columnName, Long shardValue);


}
