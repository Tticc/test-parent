package com.tester.testersearch.config.sharding;

import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;

import java.util.Collection;
import java.util.Collections;

public class DatabaseHintShardingAlgorithm implements HintShardingAlgorithm<String> {
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, HintShardingValue<String> shardingValue) {
        String bKey = shardingValue.getValues().iterator().next();
        String target = "ds_" + bKey;
        if (!availableTargetNames.contains(target)) {
            throw new IllegalArgumentException("未找到目标数据源: " + target);
        }
        return Collections.singletonList(target);
    }
}