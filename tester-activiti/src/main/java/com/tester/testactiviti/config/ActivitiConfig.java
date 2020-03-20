package com.tester.testactiviti.config;

import com.tester.testactiviti.activiti.annotation.ActivitiParamBean;
import com.tester.testactiviti.activiti.listener.BaseActicitiListener;
import com.tester.testactiviti.activiti.listener.DefaultActicitiListener;
import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ActivitiConfig {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DefaultActicitiListener listener;
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ProcessEngine processEngine() {
        return before();
    }

    /**
     * 使用统一的dataSource
     * @param
     * @return org.activiti.engine.ProcessEngine
     * @throws
     * @author 温昌营
     * @date 2019/11/18
     */
    private ProcessEngine before() {
        StandaloneProcessEngineConfiguration conf = (StandaloneProcessEngineConfiguration) ProcessEngineConfiguration
                .createStandaloneProcessEngineConfiguration();
        conf.setDataSource(dataSource);
        conf.setBeans(getActivitiParamBeans());
        conf.setEventListeners(Collections.singletonList(listener));
        // 设置TransactionFactory，activiti事务交给spring管理
        conf.setTransactionFactory(new SpringManagedTransactionFactory());
        conf.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE);
//        conf.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        return conf.buildProcessEngine();
    }
    /**
     * 获取有ActivitiParamBean注解的bean
     * @param
     * @return java.util.Map<java.lang.Object,java.lang.Object>
     * @throws
     * @author 温昌营
     * @date 2019/11/28
     */
    private Map<Object, Object> getActivitiParamBeans(){
        Map<Object,Object> beanMap = new HashMap<>();
        Map<String,Object> beans = applicationContext.getBeansWithAnnotation(ActivitiParamBean.class);
        for(Map.Entry item : beans.entrySet()){
            beanMap.put(item.getKey(),item.getValue());
        }
        return beanMap;
    }

    @Deprecated
    private ProcessEngine after() {
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
        processEngineConfiguration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/wxqyh?useSSL=false&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8");
        processEngineConfiguration.setJdbcUsername("root");
        processEngineConfiguration.setJdbcPassword("123456");
        processEngineConfiguration.setDatabaseType("mysql");
        processEngineConfiguration.setDatabaseSchemaUpdate("update");
        processEngineConfiguration.setDatabaseSchemaUpdate("true");
        return processEngineConfiguration.buildProcessEngine();
    }

    @Bean
    public RuntimeService runtimeService() {
        return processEngine().getRuntimeService();
    }

    @Bean
    public TaskService taskService() {
        return processEngine().getTaskService();
    }

    @Bean
    public RepositoryService repositoryService() {
        return processEngine().getRepositoryService();
    }

    @Bean
    public HistoryService historyService() {
        return processEngine().getHistoryService();
    }

    @Bean
    public ManagementService managementService() {
        return processEngine().getManagementService();
    }

    @Bean
    public DynamicBpmnService dynamicBpmnService() {
        return processEngine().getDynamicBpmnService();
    }
}
