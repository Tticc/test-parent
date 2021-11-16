package com.tester.thirdparty.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.TopicConfig;
import org.apache.rocketmq.common.protocol.body.ClusterInfo;
import org.apache.rocketmq.common.protocol.body.TopicList;
import org.apache.rocketmq.common.protocol.route.BrokerData;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExtImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

/**
 * @Author 温昌营
 * @Date 2021-11-1 11:47:27
 */
@Service
@Slf4j
public class RocketMQAutoManager implements InitializingBean, GenericApplicationListener {

    private DefaultMQAdminExtImpl defaultMQAdminExtImpl;

    @Autowired
    private RocketMQConfigurer rocketMQConfigurer;

    /**
     * 创建topic
     * @Date 11:52 2021/11/1
     * @Author 温昌营
     **/
    public void myWayToCreateTopic(String topic) throws Exception {
        myWayToCreateTopic(rocketMQConfigurer.getMqBroker(),topic);
    }

    /**
     * 创建topic
     * @Date 11:52 2021/11/1
     * @Author 温昌营
     **/
    public void myWayToCreateTopic(String broker, String topic) throws Exception {
        log.info("开始创建topic。broker:{}, topic:{}",broker,topic);
        TopicConfig topicConfig = new TopicConfig();
        topicConfig.setReadQueueNums(16);
        topicConfig.setWriteQueueNums(16);
        topicConfig.setTopicName(topic);

        BrokerData brokerData = getBrokerAddrTable().get(broker);
        HashMap<Long, String> brokerAddrs = brokerData.getBrokerAddrs();
        String s = brokerAddrs.get(0L); // master 机器ip:端口  brokerAddrs={0=10.10.36.88:10911, 1=10.10.36.89:10911}]
        log.info("broker master ip:{}",s);

        defaultMQAdminExtImpl.createAndUpdateTopicConfig(s, topicConfig);
        log.info("topic【{}】创建完成",topic);
    }

    /**
     * for testing
     * @param
     * @return void
     * @Date 11:45 2021/11/1
     * @Author 温昌营
     **/
    public void getBrokerConfig(String broker) throws Exception {
        BrokerData brokerData = getBrokerAddrTable().get(broker);
        HashMap<Long, String> brokerAddrs = brokerData.getBrokerAddrs();
        String s = brokerAddrs.get(0L); // master 机器ip:端口  brokerAddrs={0=10.10.36.88:10911, 1=10.10.36.89:10911}]
        Properties brokerConfig = defaultMQAdminExtImpl.getBrokerConfig(s);
        System.out.println("brokerConfig = " + brokerConfig);
    }

    /**
     * for testing
     * @param
     * @return void
     * @Date 11:45 2021/11/1
     * @Author 温昌营
     **/
    public void getTopic() throws Exception {
        System.out.println("dev-rocketmq start");
        TopicList topicList = defaultMQAdminExtImpl.fetchTopicsByCLuster("dev-rocketmq");
        System.out.println("topicList.size() = " + topicList.getTopicList().size());
        Set<String> topicList2 = topicList.getTopicList();
        for (String s : topicList2) {
            if(s.contains("create_push_test")){
                System.out.println("s = " + s);
            }
        }

        System.out.println("dev-rocketmq end");

        System.out.println("dev-rocketmq-1 start");
        TopicList topicList1 = defaultMQAdminExtImpl.fetchTopicsByCLuster("dev-rocketmq-1");

        System.out.println("topicList1.size() = " + topicList1.getTopicList().size());
        Set<String> topicList3 = topicList1.getTopicList();
        for (String s : topicList3) {
            if(s.contains("create_push_test")){
                System.out.println("s = " + s);
            }
        }
        System.out.println("dev-rocketmq-1 end");
    }


    /**
     * 获取所有的cluster
     * @Date 11:46 2021/11/1
     * @Author 温昌营
     **/
    public HashMap<String, Set<String>> getClusterAddrTable() throws Exception {
        ClusterInfo clusterInfo = defaultMQAdminExtImpl.examineBrokerClusterInfo();
        HashMap<String, Set<String>> clusterAddrTable = clusterInfo.getClusterAddrTable();
        return clusterAddrTable;
    }

    /**
     * 获取所有的broker
     * @Date 11:46 2021/11/1
     * @Author 温昌营
     **/
    public HashMap<String, BrokerData> getBrokerAddrTable() throws Exception {
        ClusterInfo clusterInfo = defaultMQAdminExtImpl.examineBrokerClusterInfo();
        HashMap<String, BrokerData> brokerAddrTable = clusterInfo.getBrokerAddrTable();
        return brokerAddrTable;
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMQAdminExt mqAdminExt = new DefaultMQAdminExt(5000L);
        mqAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
        mqAdminExt.setNamesrvAddr(rocketMQConfigurer.getMqNameServAddr());
        defaultMQAdminExtImpl = new DefaultMQAdminExtImpl(mqAdminExt, (RPCHook)null, 5000L);
        defaultMQAdminExtImpl.start();
    }


    private static final Class<?>[] EVENT_TYPES = { ApplicationFailedEvent.class};

    @Override
    public boolean supportsEventType(ResolvableType resolvableType) {
        return isAssignableFrom(resolvableType.getRawClass(), EVENT_TYPES);
    }
    private boolean isAssignableFrom(Class<?> type, Class<?>... supportedTypes) {
        if (type != null) {
            for (Class<?> supportedType : supportedTypes) {
                if (supportedType.isAssignableFrom(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationFailedEvent) {
            if(defaultMQAdminExtImpl != null){
                try{
                    defaultMQAdminExtImpl.shutdown();
                }catch (Exception e){
                    log.error("error. close mq client failed!");
                }
            }
        }
    }
}
