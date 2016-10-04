package com.easemob.usergrid.message.helper.Utils;

import com.easemob.usergrid.message.helper.exceptions.CommonRuntimeException;

/**
 * Created by zhouhu on 3/10/2016.
 */
public class TimeUtil {
    public static void sleep(long millis) {
        try {
            Thread.currentThread().sleep(millis);
        } catch (InterruptedException e) {
            throw new CommonRuntimeException(e);
        }
    }
    private TimeUtil() {
        throw new IllegalAccessError("TimeUtil is utility class");
    }
}
