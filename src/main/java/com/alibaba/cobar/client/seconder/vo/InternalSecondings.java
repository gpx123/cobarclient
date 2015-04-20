package com.alibaba.cobar.client.seconder.vo;

import java.util.List;

/**
 *<secondings>
 *  <seconding>
 *     <namespace>Cont.groupbyName</namespace>
 *     <sql>select taobaoId , name from $tableName$ group by name</sql>
 *  </seconding>
 * </secondings>
 * <br>==========================
 * <br> 公司：优视科技
 * <br> 开发：fangyuan
 * <br> 版本：1.0
 * <br> 创建时间： 2015/4/17
 * <br>==========================
 */
public class InternalSecondings {

    private List<InternalSeconding> secondings;

    public List<InternalSeconding> getSecondings() {
        return secondings;
    }

    public void setSecondings(List<InternalSeconding> secondings) {
        this.secondings = secondings;
    }
}
