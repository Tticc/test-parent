package com.tester.testercv.config;

import com.tester.testercommon.util.SpringBeanContextUtil;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanDefinitionCustomizer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @Author 温昌营
 * @Date
 */
@Configuration
public class MyApplicationContext implements SmartInitializingSingleton{
    public static ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext)SpringBeanContextUtil.getApplicationContext();
    /**
     * 所有bean实例化后调用
     * @param
     * @return void
     * @Date 15:20 2020/12/8
     * @Author 温昌营
     **/
    @Bean
    public OutBean getOutBean(){
        return new OutBean();
    }

    /**
     * spring已实例化所有bean并管理后，调用这个方法。
     * @param
     * @return void
     * @Date 15:38 2020/12/9
     * @Author 温昌营
     **/
    @Override
    public void afterSingletonsInstantiated() {
        GenericApplicationContext genericApplicationContext = (GenericApplicationContext)this.applicationContext;
        // 注册bean/刷新bean
        Object outBean1 = SpringBeanContextUtil.getBean("outBean");
        System.out.println(outBean1);
        String containerBeanName = "containerBeanNamecontainerBeanName";
        Class<OutBean> outBean = OutBean.class;
        OutBean beanOld = (OutBean)genericApplicationContext.getBean(outBean);

        genericApplicationContext.registerBean(containerBeanName, outBean, () -> {
            return this.createController(beanOld);
        }, new BeanDefinitionCustomizer[0]);
        OutBean bean = (OutBean)genericApplicationContext.getBean(containerBeanName, outBean);
        System.out.println(bean);
    }
    OutBean createController(OutBean beanOld){
        OutBean outBean = new OutBean();
        if(beanOld != null) {
            outBean.setDateStart(beanOld.getDateStart());
        }
        return outBean;
    }



}
