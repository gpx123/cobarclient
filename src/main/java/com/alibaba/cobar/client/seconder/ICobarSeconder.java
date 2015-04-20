package com.alibaba.cobar.client.seconder;

import java.util.List;

/**
 * <br>==========================
 * <br> 公司：优视科技
 * <br> 开发：fangyuan
 * <br> 版本：1.0
 * <br> 创建时间： 2015/4/17
 * <br>==========================
 */
public interface ICobarSeconder {

    /**
     * 将第一次检索部分结果数据存入derby中
     * 1，表名由Object属性名按字母排序连接组成
     * 2，解析resultList，组织create table语句
     * 3，将resultList存入新建table中
     * 4，返回表名
     *
     * @param res       instanseof List
     * @param tableName
     * @result 是否新建表成功
     */
    public void store(String tableName, Object res) throws SeconderException;

    /**
     * 进入二次检索
     *
     * @param statementName
     * @param tableName
     * @return
     */
    public List<Object> doSecond(String tableName, String statementName,Object res) throws SeconderException;

    /**
     * finnally中进行删除表操作
     * @param tableName
     * @throws SeconderException
     */
    public void drop(String tableName) throws SeconderException;

    public boolean isNeedSeconder(String statementName);

}
