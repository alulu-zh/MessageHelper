package com.easemob.usergrid;

import java.util.Calendar;

/**
 * Created by zhouhu on 1/11/2016.
 */
public class Test1 {
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        System.out.println("time = " + calendar.getTime());
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        System.out.println("time = " + calendar.getTime());
    }
}
