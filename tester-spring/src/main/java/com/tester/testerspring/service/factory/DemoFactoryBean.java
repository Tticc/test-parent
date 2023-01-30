package com.tester.testerspring.service.factory;

import org.springframework.beans.factory.FactoryBean;

public class DemoFactoryBean implements FactoryBean<OtherBean> {
    @Override
    public OtherBean getObject() throws Exception {
        return new OtherBean();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
