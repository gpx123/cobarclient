package com.alibaba.cobar.client.domain;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <br>==========================
 * <br> 公司：优视科技
 * <br> 开发：fangyuan
 * <br> 版本：1.0
 * <br> 创建时间： 2015/4/15
 * <br>==========================
 */
public class FragmentPO implements Cloneable  {

    private Object shard;

    private List<Object> shardList;

    public Object getShard() {
        return shard;
    }

    public void setShard(Object fragment) {
        this.shard = fragment;
    }

    public List<Object> getShardList() {
        return shardList;
    }

    public void setShardList(List<Object> fragmentList){
        this.shardList = fragmentList;
    }

    /**
     * 按年分表
     *
     * @param date
     */
    public void setShard(String prefix , Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        this.shard = prefix + String.valueOf(cal.get(Calendar.YEAR));
    }

    public String getShardUtil(String prefix , Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return prefix + String.valueOf(cal.get(Calendar.YEAR));
    }

    /**
     * 按月份进行分表
     *
     * @param monthExpress 1,2 表示当前月份+1到 当前月份+2 ； 0,2 表示当前月份 到 当前月份+2 -2,2 表示当前月份-2到当前月份+2
     * @return
     */
    public void setShardList(Date currentMonth, String monthExpress) {
        shardList = new ArrayList<Object>();
        String[] months = monthExpress.split(",");
        if (months.length != 2) {
            return;
        }
        Integer start = Integer.valueOf(months[0]);
        Integer end = Integer.valueOf(months[1]);
        if (start > end) {
            return;
        }
        for (int i = start; i <= end; i++) {
            shardList.add(FastDateFormat.getInstance("yyyyMM").format(DateUtils.addMonths(currentMonth, Integer.valueOf(i))));
        }
        return;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
