package com.easemob.usergrid.message.helper.exceptions;

/**
 * Created by zhouhu on 3/10/2016.
 */
public class NullPathException extends RuntimeException {

    private static final long serialVersionUID = 8589854063130252081L;

    public NullPathException(Exception e) {
        super(e);
    }

    public NullPathException(String msg, Exception e) {
        super(msg, e);
    }

    public NullPathException(String msg) {
        super(msg);
    }
}
