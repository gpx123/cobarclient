package com.alibaba.cobar.client.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.HashSet;
import java.util.List;

/**
 * <br>==========================
 * <br> 公司：优视科技
 * <br> 开发：fangyuan
 * <br> 版本：1.0
 * <br> 创建时间： 2015/4/20
 * <br>==========================
 */
public class DerbyUtil {

    private static volatile DerbyUtil instance;

    public static DerbyUtil getInstance() {
        if (instance == null) {
            synchronized (DerbyUtil.class) {
                if (instance == null)
                    instance = new DerbyUtil();
            }
        }
        return instance;
    }

    private static final String driverName = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String url = "jdbc:derby:testmemory;create=true";

    private final Log log = LogFactory.getLog(getClass());

    private DerbyUtil() {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            log.error(e);
        }
    }

    public List<Object> executeQuery(String sql , Object res) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);
            conn.setSavepoint();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(sql);
            return SqlUtil.getObject(rs,res);
        } catch (Exception e) {
            log.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                log.error(e1);
            }
        } finally {
            if (conn != null) {
                try {
                    conn.commit();
                    if (!conn.isClosed())
                        conn.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            }
        }
        return null;
    }

    public boolean existTable(String tableName){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);
            conn.setSavepoint();
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet res = meta.getTables(null, null, null, new String[]{"TABLE"});
            HashSet<String> set=new HashSet<String>();
            while (res.next()) {
                set.add(res.getString("TABLE_NAME"));
            }
            return set.contains(tableName.toUpperCase());
        } catch (SQLException e) {
            log.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                log.error(e1);
            }
        } finally {
            if (conn != null) {
                try {
                    conn.commit();
                    if (!conn.isClosed())
                        conn.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            }
        }
        return false;
    }

    public void execute(String sql) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);
            conn.setSavepoint();
            Statement s = conn.createStatement();
            s.execute(sql);
        } catch (SQLException e) {
            log.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                log.error(e1);
            }
        } finally {
            if (conn != null) {
                try {
                    conn.commit();
                    if (!conn.isClosed())
                        conn.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            }
        }
    }

}
