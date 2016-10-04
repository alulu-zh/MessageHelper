package com.easemob.usergrid.message.helper.exceptions;

/**
 * Created by zhouhu on 3/10/2016.
 */
public class CommonRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -222012315143560070L;

    public CommonRuntimeException(Exception e) {
        super(e);
    }

    public CommonRuntimeException(String msg, Exception e) {
        super(msg, e);
    }

    public CommonRuntimeException(String msg) {
        super(msg);
    }
}
