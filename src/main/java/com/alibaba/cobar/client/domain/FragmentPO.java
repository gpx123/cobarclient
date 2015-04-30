package com.alibaba.cobar.client.domain;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;

import java.util.ArrayList;
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

    private Object fragment;

    private List<Object> fragmentList;

    public Object getFragment() {
        return fragment;
    }

    public void setFragment(Object fragment) {
        this.fragment = fragment;
    }

    public List<Object> getFragmentList() {
        return fragmentList;
    }

    /**
     * 按月份进行分表
     *
     * @param monthExpress 1,2 表示当前月份+1到 当前月份+2 ； 0,2 表示当前月份 到 当前月份+2 -2,2 表示当前月份-2到当前月份+2
     * @return
     */
    public void setFragmentList(Date currentMonth, String monthExpress) {
        fragmentList = new ArrayList<Object>();
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
            fragmentList.add(FastDateFormat.getInstance("yyyyMM").format(DateUtils.addMonths(currentMonth, Integer.valueOf(i))));
        }
        return;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
