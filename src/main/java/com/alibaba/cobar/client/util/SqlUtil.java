package com.alibaba.cobar.client.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.ResultSetDynaClass;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <br>==========================
 * <br> 公司：优视科技
 * <br> 开发：fangyuan
 * <br> 版本：1.0
 * <br> 创建时间： 2015/4/16
 * <br>==========================
 */
public class SqlUtil {

    /**
     * @param cls
     * @return
     */
    public static String convert2MysqlType(String cls) throws Exception {
        if (cls.equals("java.lang.String")) {
            return "VARCHAR(20)";
        } else if (cls.equals("java.lang.byte[]")) {
            return "BLOB";
        } else if (cls.equals("java.lang.Integer")) {
            return "INT";
        } else if (cls.equals("java.lang.Long")) {
            return "BIGINT";
        } else if (cls.equals("java.lang.Boolean")) {
            return "TINYINT";
        } else if (cls.equals("java.math.BigInteger")) {
            return "BIGINT";
        } else if (cls.equals("java.lang.Float")) {
            return "FLOAT";
        } else if (cls.equals("java.lang.Double")) {
            return "DOUBLE";
        } else if (cls.equals("java.math.BigDecimal")) {
            return "DECIMAL";
        } else if (cls.equals("java.util.Date")) {
            return "DATE";
        } else if (cls.equals("java.sql.Date")) {
            return "DATE";
        } else if (cls.equals("java.sql.Time")) {
            return "TIME";
        } else if (cls.equals("java.sql.Timestamp")) {
            return "TIMESTAMP";
        } else if (cls.equals("java.util.DateTime")) {
            return "TIMESTAMP";
        } else {
            throw new Exception("cls canot convert to mysql type! ");
        }
    }

    public static String convert2MysqlValue(Object value) {
        if (value instanceof Date) {
            return "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value) + "'";
        } else if (value instanceof Integer || value instanceof Long
                || value instanceof Double || value instanceof BigDecimal
                || value instanceof Float || value instanceof BigInteger) {
            return String.valueOf(value);
        } else {
            return "'" + String.valueOf(value) + "'";
        }
    }

    public static List<Object> getObject(ResultSet rs, Object obj) throws Exception {
        ArrayList lists = new ArrayList();
        ResultSetDynaClass rsdc = new ResultSetDynaClass(rs);
        Iterator rows = rsdc.iterator();
        while (rows.hasNext()) {
            DynaBean row = (DynaBean) rows.next();
            obj = obj.getClass().newInstance();
            if(obj instanceof Map){
                obj = PropertyUtils.describe(row);
            }else{
                BeanUtils.copyProperties(obj, row);
            }
            lists.add(obj);
        }
        return lists;

    }
}
