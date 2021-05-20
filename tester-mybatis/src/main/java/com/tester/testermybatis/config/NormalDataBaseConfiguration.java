package com.tester.testermybatis.config;

import com.tester.testercommon.constant.ConstantList;
import com.tester.testermybatis.config.prop.NormalDatabaseProperties;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 非分库分表数据源
 * <br/>
 * 分库分表数据源配置：MyDataBaseConfiguration
 * <br/>
 * @Author 温昌营
 * @Date 2020-8-20 18:18:22
 */
@Configuration
@EnableConfigurationProperties({MybatisProperties.class})
@tk.mybatis.spring.annotation.MapperScan(basePackages = {"com.tester.testerwebapp.dao.mapper"}, sqlSessionFactoryRef = "normalSqlSessionFactory")
@Data
@Slf4j
public class NormalDataBaseConfiguration implements InitializingBean {

    @Autowired
    private final MybatisProperties properties;

    private final ResourceLoader resourceLoader;

    private NormalDatabaseProperties normalDatabaseProperties = new NormalDatabaseProperties();


    @Bean("normalSqlSessionFactory")
    public SqlSessionFactory normalSqlSessionFactory() throws Exception {
        log.info("====== normalSqlSessionFactory init ======");
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(normalDataSource());
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:mybatis-config.xml");
        if(resources.length>0){
            sqlSessionFactoryBean.setConfigLocation(resources[0]);
        }
        if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
            sqlSessionFactoryBean.setMapperLocations(this.properties.resolveMapperLocations());
        }
        return sqlSessionFactoryBean.getObject();
    }

    @Bean("normalDataSource")
    public DataSource normalDataSource() throws SQLException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setPoolName("normalDataSourcePool");
        dataSource.setDriverClassName(normalDatabaseProperties.getDriverClassName());
        dataSource.setJdbcUrl(normalDatabaseProperties.getJdbcUrl());
        dataSource.setUsername(normalDatabaseProperties.getUsername());
        dataSource.setPassword(normalDatabaseProperties.getPassword());
        dataSource.setMaximumPoolSize(normalDatabaseProperties.getMaximumPoolSize());
        dataSource.setConnectionTimeout(normalDatabaseProperties.getConnectionTimeout());
        dataSource.setMinimumIdle(normalDatabaseProperties.getMinimumIdle());
        dataSource.setMaxLifetime(normalDatabaseProperties.getMaxLifetime());
        dataSource.addDataSourceProperty("cachePrepStmts", normalDatabaseProperties.isCachePrepStmts());
        dataSource.addDataSourceProperty("prepStmtCacheSize", normalDatabaseProperties.getPrepStmtCacheSize());
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", normalDatabaseProperties.getPrepStmtCacheSqlLimit());
        dataSource.addDataSourceProperty("useServerPrepStmts", normalDatabaseProperties.isUseServerPrepStmts());
        return dataSource;
    }

    @Bean(ConstantList.NORMAL_MANAGER)
    public DataSourceTransactionManager getDataSourceTransactionManager() throws SQLException {
        return new DataSourceTransactionManager(normalDataSource());
    }



    @Override
    public void afterPropertiesSet() {
        checkConfigFileExists();
    }

    private void checkConfigFileExists() {
        if (this.properties.isCheckConfigLocation() && StringUtils.hasText(this.properties.getConfigLocation())) {
            Resource resource = this.resourceLoader.getResource(this.properties.getConfigLocation());
            Assert.state(resource.exists(), "Cannot find config location: " + resource
                    + " (please add config file or check your Mybatis configuration)");
        }
    }
}
