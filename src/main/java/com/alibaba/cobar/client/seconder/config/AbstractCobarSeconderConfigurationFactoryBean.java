package com.alibaba.cobar.client.seconder.config;

import com.alibaba.cobar.client.seconder.DefaultCobarSeconder;
import com.alibaba.cobar.client.seconder.vo.ActionSecondingConf;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <br>==========================
 * <br> 公司：优视科技
 * <br> 开发：fangyuan
 * <br> 版本：1.0
 * <br> 创建时间： 2015/4/17
 * <br>==========================
 */
public abstract class AbstractCobarSeconderConfigurationFactoryBean  implements FactoryBean,InitializingBean {

    private DefaultCobarSeconder seconder;

    private Resource configLocation;
    private Resource[] configLocations;

    public Object getObject() throws Exception {
        return this.seconder;
    }

    public Class getObjectType() {
        return DefaultCobarSeconder.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        seconder = new DefaultCobarSeconder();
        final Set<ActionSecondingConf> sqlActionSecondConfs = new HashSet<ActionSecondingConf>();

        if (getConfigLocation() != null) {
            assembleRulesForRouter(getConfigLocation(), sqlActionSecondConfs);
        }

        if (!ObjectUtils.isEmpty(getConfigLocations())) {
            for (Resource res : getConfigLocations()) {
                assembleRulesForRouter( res, sqlActionSecondConfs);
            }
        }

        List<Set<ActionSecondingConf>> secondingSequences = new ArrayList<Set<ActionSecondingConf>>() {
            private static final long serialVersionUID = 1493353938640646579L;
            {
                add(sqlActionSecondConfs);
            }
        };

        seconder.setSecondingSequences(secondingSequences);
    }

    protected abstract void assembleRulesForRouter(
            Resource configLocation,
            Set<ActionSecondingConf> sqlActionSecondConf)
            throws IOException;

    public Resource getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(Resource configLocation) {
        this.configLocation = configLocation;
    }

    public Resource[] getConfigLocations() {
        return configLocations;
    }

    public void setConfigLocations(Resource[] configLocations) {
        this.configLocations = configLocations;
    }
}
