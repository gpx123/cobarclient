package com.alibaba.cobar.client.seconder.config;

import com.alibaba.cobar.client.router.config.vo.InternalRule;
import com.alibaba.cobar.client.router.config.vo.InternalRules;
import com.alibaba.cobar.client.seconder.vo.ActionSecondingConf;
import com.alibaba.cobar.client.seconder.vo.InternalSeconding;
import com.alibaba.cobar.client.seconder.vo.InternalSecondings;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;
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
public class CobarSeconderXmlFactoryBean extends AbstractCobarSeconderConfigurationFactoryBean{
    @Override
    protected void assembleRulesForRouter(Resource configLocation, Set<ActionSecondingConf> sqlActionSecondConf) throws IOException {
        XStream xstream = new XStream();
        xstream.alias("secondings", InternalSecondings.class);
        xstream.alias("seconding", InternalSeconding.class);
        xstream.addImplicitCollection(InternalSecondings.class, "secondings");

        InternalSecondings internalSecondings = (InternalSecondings) xstream.fromXML(configLocation.getInputStream());
        List<InternalSeconding> secondings = internalSecondings.getSecondings();
        if(secondings == null){
            return;
        }
        for (InternalSeconding seconding : secondings){
            String namespace = StringUtils.trimToEmpty(seconding.getNamespace());
            String sql = StringUtils.trimToEmpty(seconding.getSql());
            if (StringUtils.isEmpty(namespace) || StringUtils.isEmpty(sql)) {
                throw new IllegalArgumentException(
                        "at least one of 'namespace' and 'sql' must be given.");
            }
            ActionSecondingConf conf = new ActionSecondingConf();
            conf.setSql(sql);
            conf.setNamespace(namespace);
            sqlActionSecondConf.add(conf);
        }
    }
}
