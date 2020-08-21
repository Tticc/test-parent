package com.tester.testermybatis.prop;

import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.Collection;
import java.util.Objects;

/**
 * @Author 温昌营
 * @Date 2020-8-21 10:04:41
 */
public class ComplexDatabaseShardingAlgorithm extends AbstractComplexShardingAlgorithm implements ComplexKeysShardingAlgorithm<Long> {
    @Override
    String getSuffix(String logicTableName, String columnName, Long shardValue) {
        Integer dbNum = getAllDbNum();
        Integer tableShardNum = getTableShardingNum();
        Long suffix;
        if(Objects.equals(columnName,FIELD_ORDER_NO)){
            suffix = shardValue/10;
        }else if(Objects.equals(columnName,FIELD_MEMBER_ID)){
            suffix = (shardValue % (dbNum*tableShardNum))/tableShardNum;
        }else {
            suffix = null;
        }
        return "_"+(suffix<10?"0"+suffix:suffix);
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue<Long> shardingValue) {
        return super.getTargetList(availableTargetNames,shardingValue);
    }
}
