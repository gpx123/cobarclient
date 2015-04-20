package com.alibaba.cobar.client.seconder.vo;

/**
 * <seconding>
 *     <namespace>Cont.groupbyName</namespace>
 *     <sql>select taobaoId , name from $tableName$ group by name</sql>
 * </seconding>
 * <br>==========================
 * <br> 公司：优视科技
 * <br> 开发：fangyuan
 * <br> 版本：1.0
 * <br> 创建时间： 2015/4/17
 * <br>==========================
 */
public class InternalSeconding {
    private String namespace;
    private String sql;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
        result = prime * result
                + ((sql == null) ? 0 : sql.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InternalSeconding other = (InternalSeconding) obj;
        if (namespace == null) {
            if (other.namespace != null)
                return false;
        } else if (!namespace.equals(other.namespace))
            return false;
        if (sql == null) {
            if (other.sql != null)
                return false;
        } else if (!sql.equals(other.sql))
            return false;
        return true;
    }


    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
