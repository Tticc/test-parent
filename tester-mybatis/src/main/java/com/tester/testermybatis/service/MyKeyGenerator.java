package com.tester.testermybatis.service;

import com.tester.testercommon.util.IdWorker;
import com.tester.testermybatis.annotation.DecryptDomain;
import com.tester.testermybatis.config.DemoClass;
import com.tester.testermybatis.config.ThatShouldNotInBeanFactory1;
import com.tester.testermybatis.config.ThatShouldNotInBeanFactory2;
import com.tester.testermybatis.config.prop.MyDatabaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Author 温昌营
 * @Date 2020-8-21 17:10:49
 */
@Service
public class MyKeyGenerator {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext=new AnnotationConfigApplicationContext(DemoClass.class);  //这里的参数代表要做操作的类

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : beanDefinitionNames){
            System.out.println(name);
        }

    }


    private static final String PREFIX = "1";
    public static final String PREFIX_PROD = "1";
    public static final String PREFIX_SIT = "2";
    public static final String PREFIX_DEV = "3";
    public static final String PREFIX_UAT = "4";
    public static final String PREFIX_UNKNOWN = "5";

    private static final String EVN_DEV = "dev";
    private static final String EVN_SIT = "sit";
    private static final String EVN_UAT = "uat";
    private static final String EVN_PROD = "prod";

    @Value("${spring.profiles.active}")
    private String activeEvn;

    @Autowired
    private MyDatabaseProperties myDatabaseProperties;

    @Autowired
    private IdWorker idWorker;

    public Long generateOrderNo(Long memberId, Integer orderType){
        // 注：这里虽然使用雪花算法控制唯一，但是由于需要兼容分表算法，所以只截取雪花算法生成的long的后12位，可能会导致orderNo非唯一。
        String longStr = String.valueOf(idWorker.nextId());
        String substring = longStr.length() >= 12 ? longStr.substring(longStr.length() - 12) : longStr;
        return generateOrderNo(substring,memberId,orderType);
    }
    public Long generateOrderNo(String serialNo, Long memberId, Integer orderType){
        Integer tableIndex = getTableIndex(memberId);
        Integer dbIndex = getDbIndex(tableIndex);
        Integer tableIndexInDb = getTableIndexInDb(tableIndex);
        String orderNo = PREFIX + serialNo + orderType.toString() + (dbIndex < 10 ? "0" + dbIndex : dbIndex) + tableIndexInDb;
        return generateNoByEnv(Long.valueOf(orderNo));
    }

    private Integer getTableIndex(Long memberId) {
        Integer dbNum = myDatabaseProperties.getAllDbNum();
        Integer tableShardNum = myDatabaseProperties.getShardingNumPerDb();
        return (int) (memberId % (dbNum * tableShardNum));
    }
    private Integer getDbIndex(Integer tableIndex) {
        Integer tableShardNum = myDatabaseProperties.getShardingNumPerDb();
        return tableIndex / tableShardNum;
    }
    private Integer getTableIndexInDb(Integer tableIndex) {
        Integer tableShardNum = myDatabaseProperties.getShardingNumPerDb();
        return tableIndex - (tableIndex / tableShardNum) * tableShardNum;
    }
    private Long generateNoByEnv(Long orderNo) {
        if (Objects.equals(EVN_PROD, activeEvn)) {
            return Long.parseLong(PREFIX_PROD + orderNo);
        } else if (Objects.equals(EVN_SIT, activeEvn)) {
            return Long.parseLong(PREFIX_SIT + orderNo);
        } else if (Objects.equals(EVN_DEV, activeEvn)) {
            return Long.parseLong(PREFIX_DEV + orderNo);
        } else if (Objects.equals(EVN_UAT, activeEvn)) {
            return Long.parseLong(PREFIX_UAT + orderNo);
        } else {
            return Long.valueOf(PREFIX_UNKNOWN + orderNo);
        }
    }

}
