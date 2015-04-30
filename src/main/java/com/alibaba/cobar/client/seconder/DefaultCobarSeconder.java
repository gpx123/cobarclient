package com.alibaba.cobar.client.seconder;

import com.alibaba.cobar.client.seconder.vo.ActionSecondingConf;
import com.alibaba.cobar.client.util.DerbyUtil;
import com.alibaba.cobar.client.util.SqlUtil;
import org.apache.commons.beanutils.PropertyUtils;

import java.util.*;

/**
 * <br>==========================
 * <br> 公司：优视科技
 * <br> 开发：fangyuan
 * <br> 版本：1.0
 * <br> 创建时间： 2015/4/17
 * <br>==========================
 */
public class DefaultCobarSeconder implements ICobarSeconder {

    private List<Set<ActionSecondingConf>> secondingSequences = new ArrayList<Set<ActionSecondingConf>>();

    private synchronized void create(String tableName, Object res) throws Exception {
        if (DerbyUtil.getInstance().existTable(tableName)) {
            return;
        }
        //-- 解析res，并拿到属性名及其类型
        Object bean = ((List) res).get(0);
        Map resMap;
        Map<String, String> propertyMap = new HashMap<String, String>();
        if (!(bean instanceof Map)) {
            resMap = PropertyUtils.describe(bean);
        } else {
            resMap = (Map) bean;
        }
        for (Object key : resMap.keySet()) {
            String propertyTypeName = PropertyUtils.getPropertyType(bean, String.valueOf(key)).getName();
            if (!key.equals("class") && !propertyTypeName.equals("java.util.List") && !propertyTypeName.equals("java.util.Map") && !propertyTypeName.equals("java.lang.Object")) {
                propertyMap.put(String.valueOf(key), SqlUtil.convert2MysqlType(propertyTypeName));
            }
        }
        //-- 构造create table sql
        StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + "(");
        int maxLen = propertyMap.size();
        for (String proKey : propertyMap.keySet()) {
            maxLen--;
            String proType = propertyMap.get(proKey);
            sql.append(proKey + " " + proType + " DEFAULT NULL");
            if (maxLen != 0) {
                sql.append(",");
            }
        }
        sql.append(")");
        //-- 执行derby命令，新建表
        DerbyUtil.getInstance().execute(sql.toString());
    }

    private void insert(String tableName, Object bean) throws Exception {
        Map resMap;
        if (!(bean instanceof Map)) {
            resMap = PropertyUtils.describe(bean);
        } else {
            resMap = (Map) bean;
        }
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName );
        Map<String , String> sqlMap = new HashMap<String, String>();
        for (Object key : resMap.keySet()) {
            String propertyTypeName = PropertyUtils.getPropertyType(bean, String.valueOf(key)).getName();
            if (!"class".equals(key) && !propertyTypeName.equals("java.util.List") && !propertyTypeName.equals("java.util.Map") && !propertyTypeName.equals("java.lang.Object")) {
                sqlMap.put(String.valueOf(key), SqlUtil.convert2MysqlValue(resMap.get(key)));
            }
        }
        String fields = " (";
        String values = " VALUES (";
        Integer maxLen = sqlMap.size();
        for(String field : sqlMap.keySet()){
            fields += field;
            values += sqlMap.get(field);
            if(--maxLen != 0){
                fields += ",";
                values += ",";
            }
        }
        sql.append(fields + ")");
        sql.append(values + ")");
        //-- 执行derby命令，新建表
        DerbyUtil.getInstance().execute(sql.toString());
    }

    public void store(String tableName, Object res) throws SeconderException {
        if (res == null) {
            return;
        }
        //只有List类型才可被插入
        if (res instanceof List) {
            if (((List) res).isEmpty()) {
                return;
            }
            try {
                //判断表是否存在
                create(tableName, res);
                //插入数据
                for (Object bean : (List) res) {
                    insert(tableName, bean);
                }
            } catch (Exception e) {
                throw new SeconderException("DefaultCobarSeconder.store errors =>", e);
            }
        }
    }

    private ActionSecondingConf getSecondingByNamespace(String namespace) {
        if (getSecondingSequences() == null) {
            return null;
        }
        for (Set<ActionSecondingConf> confSet : getSecondingSequences()) {
            for (ActionSecondingConf seconding : confSet) {
                if (seconding.getNamespace().equals(namespace)) {
                    return seconding;
                }
            }
        }
        return null;
    }

    public List<Object> doSecond(String tableName, String statementName, Object res) throws SeconderException {
        try {
            ActionSecondingConf seconding = getSecondingByNamespace(statementName);
            if (seconding != null) {
                //--替换$tableName$
                String sql = seconding.getSql().replace("$tableName$", tableName);
                //--执行sql
                return DerbyUtil.getInstance().executeQuery(sql, res);
            }
        } catch (Exception e) {
            throw new SeconderException("DefaultCobarSeconder.doSecond errors =>", e);
        }
        return null;
    }

    public void drop(String tableName) throws SeconderException {
        //-- 执行derby命令，新建表
        if (!DerbyUtil.getInstance().existTable(tableName)) {
            return;
        }
        DerbyUtil.getInstance().execute("DROP TABLE " + tableName);
    }

    public boolean isNeedSeconder(String statementName) {
        return getSecondingByNamespace(statementName) != null;
    }

    public List<Set<ActionSecondingConf>> getSecondingSequences() {
        return secondingSequences;
    }

    public void setSecondingSequences(List<Set<ActionSecondingConf>> secondingSequences) {
        this.secondingSequences = secondingSequences;
    }
}
