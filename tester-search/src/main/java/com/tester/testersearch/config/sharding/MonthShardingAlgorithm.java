package com.tester.testersearch.config.sharding;


import com.google.common.collect.Range;
import com.tester.testercommon.util.DateUtil;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class MonthShardingAlgorithm implements PreciseShardingAlgorithm<Long>, RangeShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        Long timestamp = shardingValue.getValue(); // 毫秒时间戳
        String tablePrefix = shardingValue.getLogicTableName(); // 逻辑表名，如 trade_data

        String monthStr = new SimpleDateFormat("yyyyMM").format(new Date(timestamp));
        String targetTable = tablePrefix + "_" + monthStr;

        if (availableTargetNames.contains(targetTable)) {
            return targetTable;
        }
        throw new IllegalArgumentException("No matching table found for " + targetTable);
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTables, RangeShardingValue<Long> shardingValue) {
        // 获取时间范围
        Range<Long> range = shardingValue.getValueRange();
        Long lower = range.hasLowerBound() ? range.lowerEndpoint() : null;
        Long upper = range.hasUpperBound() ? range.upperEndpoint() : null;
        // 计算覆盖的所有月份分表
        Set<String> tables = new LinkedHashSet<>();
        // 处理无下限的场景
        if (lower == null && upper != null) {
            LocalDateTime start = DateUtil.getLocalDateTime("20190101000000");
            LocalDateTime end = LocalDateTime.ofInstant(Instant.ofEpochMilli(upper), ZoneId.systemDefault());
            while (!start.isAfter(end)) {
                String table = getTargetTable(shardingValue.getLogicTableName(), start);
                if (availableTables.contains(table)) {
                    tables.add(table);
                }
                start = start.plusMonths(1);
            }
        }
        // 处理无上限的场景
        if (lower != null && upper == null) {
            LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli(lower), ZoneId.systemDefault());
            LocalDateTime end = LocalDateTime.now();
            while (!start.isAfter(end)) {
                String table = getTargetTable(shardingValue.getLogicTableName(), start);
                if (availableTables.contains(table)) {
                    tables.add(table);
                }
                start = start.plusMonths(1);
            }
        }

        if (lower != null && upper != null) {
            LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli(lower), ZoneId.systemDefault());
            LocalDateTime end = LocalDateTime.ofInstant(Instant.ofEpochMilli(upper), ZoneId.systemDefault());

            while (!start.isAfter(end)) {
                String tableS = getTargetTable(shardingValue.getLogicTableName(), start);
                if (availableTables.contains(tableS)) {
                    tables.add(tableS);
                }
                start = start.plusMonths(1);
            }
            // 避免刚好处于跨月场景。例如start=2023-01-31 23:59:55,end=2023-02-01 00:00:05
            // 此时start+1个月之后，超过了end，导致推出循环，20230201无法进入tables，导致缺少数
            // 因此在最后这里把end对应的table加上
            // 之所以要在最后加，是避免最后这张顺序有误。tables是一个LinkedHashSet，里面的表顺序是按时间顺序排序的。
            // sharding会根据这个排序取数据，如果这里tables表顺序乱了，取出来的数据也是乱的
            String tableE = getTargetTable(shardingValue.getLogicTableName(), end);
            if (availableTables.contains(tableE)) {
                tables.add(tableE);
            }
        }
        return tables;
    }

    private String getTargetTable(String logicTable, Long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        return logicTable + "_" + dateTime.format(DateTimeFormatter.ofPattern("yyyyMM"));
    }

    private String getTargetTable(String logicTable, LocalDateTime dateTime) {
        return logicTable + "_" + dateTime.format(DateTimeFormatter.ofPattern("yyyyMM"));
    }
}

