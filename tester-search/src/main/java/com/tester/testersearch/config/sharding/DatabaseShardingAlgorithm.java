package com.tester.testersearch.config.sharding;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

public class DatabaseShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        String bKey = shardingValue.getValue();
        if (bKey == null) throw new IllegalArgumentException("b_key is null");
        // 分库规则
        return "ds_" + bKey;
    }
}
