package com.springboot.tools.bean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author kxd
 * @date 2018/11/2 14:39
 * description:
 */
@Component("DemoFactoryBean")
public class DemoFactoryBean implements FactoryBean<FacDemoBean> {
    @Override
    public FacDemoBean getObject() throws Exception {
        return new FacDemoBean();
    }

    @Override
    public Class<?> getObjectType() {
        return FacDemoBean.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
