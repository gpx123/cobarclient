package com.alibaba.cobar.client.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.ResultSetDynaClass;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
        } else if (cls.equals("java.lang.Integer") || cls.equals("int")) {
            return "INT";
        } else if (cls.equals("java.lang.Long") || cls.equals("long")) {
            return "BIGINT";
        } else if (cls.equals("java.lang.Boolean") || cls.equals("boolean")) {
            return "TINYINT";
        } else if (cls.equals("java.math.BigInteger")) {
            return "BIGINT";
        } else if (cls.equals("java.lang.Float") || cls.equals("float")) {
            return "FLOAT";
        } else if (cls.equals("java.lang.Double") || cls.equals("double")) {
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
            throw new Exception("cls=>" + cls + " canot convert to mysql type! ");
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
        List<Map> resultMap = convertList(rs);
        for(Object map : resultMap){
            if (obj instanceof Map) {
                lists.add(map);
            }else{
                BeanUtils.copyProperties(obj, map);
                lists.add(obj);
            }
        }
        return lists;
    }

    private static List convertList(ResultSet rs) throws Exception {
        List list = new ArrayList();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        while (rs.next()) {
            Map rowData = new HashMap();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i).toLowerCase(), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }

}
