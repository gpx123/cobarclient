/**
 * Copyright 1999-2011 Alibaba Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.cobar.client.router.rules.ibatis;

import com.alibaba.cobar.client.router.support.IBatisRoutingFact;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class IBatisNamespaceShardingRule extends AbstractIBatisOrientedRule {

    private transient final Logger logger = LoggerFactory
            .getLogger(IBatisNamespaceShardingRule.class);

    public IBatisNamespaceShardingRule(String pattern, String action, String attributePattern) {
        super(pattern, action, attributePattern);
    }

    public IBatisNamespaceShardingRule(String pattern, String action, String attributePattern, String fragmentPattern) {
        super(pattern, action, attributePattern, fragmentPattern);
    }

    public boolean isDefinedAt(IBatisRoutingFact routingFact) {
        Validate.notNull(routingFact);
        String namespace = StringUtils.substringBeforeLast(routingFact.getAction(), ".");
        boolean matches = StringUtils.equals(namespace, getTypePattern());
        if (matches) {
            try {
                Map<String, Object> vrs = new HashMap<String, Object>();
                vrs.putAll(getFunctionMap());
                vrs.put("$ROOT", routingFact.getArgument()); // add top object reference for expression
                VariableResolverFactory vrfactory = new MapVariableResolverFactory(vrs);
                if (MVEL.evalToBoolean(getAttributePattern(), routingFact.getArgument(), vrfactory)) {
                    return true;
                }
            } catch (Throwable t) {
                logger
                        .info(
                                "failed to evaluate attribute expression:'" + getAttributePattern() + "' with context object:'" + routingFact.getArgument() + "'\n" + t);
            }
        }
        return false;
    }

    public Object fragment(IBatisRoutingFact routingFact) {
        if (StringUtils.isEmpty(this.getFragmentPattern())) {
            return null;
        }
        Validate.notNull(routingFact);
        try {
            Map<String, Object> vrs = new HashMap<String, Object>();
            vrs.putAll(getFunctionMap());
            vrs.put("$ROOT", routingFact.getArgument()); // add top object reference for expression
            VariableResolverFactory vrfactory = new MapVariableResolverFactory(vrs);
            return MVEL.eval(getFragmentPattern(), routingFact.getArgument(), vrfactory);
        } catch (Throwable t) {
            logger
                    .info(
                            "failed to evaluate attribute expression:'" + getAttributePattern() + "' with context object:'" + routingFact.getArgument() + "'\n" + t);
        }
        return null;
    }

    @Override
    public String toString() {
        return "IBatisNamespaceShardingRule [getAttributePattern()=" + getAttributePattern()
                + ", getAction()=" + getAction() + ", getTypePattern()=" + getTypePattern() + "]";
    }

}
