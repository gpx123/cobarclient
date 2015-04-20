package com.alibaba.cobar.client.util;

import java.util.ArrayList;

/**
 * <br>==========================
 * <br> 公司：优视科技
 * <br> 开发：fangyuan
 * <br> 版本：1.0
 * <br> 创建时间： 2015/4/20
 * <br>==========================
 */
public class StringSelfUtil {

    public static String randomA2Z(int len){
        ArrayList list = new ArrayList();
        for (char c = 'a'; c <= 'z'; c++) {
            list.add(c);
        }
        String str = "";
        for (int i = 0; i < len; i++) {
            int num = (int) (Math.random() * 26);
            str = str + list.get(num);
        }
        return str;
    }
}
