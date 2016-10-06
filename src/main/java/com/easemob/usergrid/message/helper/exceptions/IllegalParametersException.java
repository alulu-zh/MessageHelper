package com.easemob.usergrid.message.helper.exceptions;

/**
 * Created by zhouhu on 3/10/2016.
 */
public class IllegalParametersException extends RuntimeException {

    private static final long serialVersionUID = 2820304889089468727L;

    public IllegalParametersException(Exception e) {
        super(e);
    }

    public IllegalParametersException(String msg, Exception e) {
        super(msg, e);
    }

    public IllegalParametersException(String msg) {
        super(msg);
    }
}
