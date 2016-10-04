package com.easemob.usergrid.message.helper.exceptions;

/**
 * Created by zhouhu on 3/10/2016.
 */
public class ReadHDFSException extends RuntimeException {
    private static final long serialVersionUID = 1581456461359881343L;

    public ReadHDFSException(Exception e) {
        super(e);
    }

    public ReadHDFSException(String msg, Exception e) {
        super(msg, e);
    }

    public ReadHDFSException(String msg) {
        super(msg);
    }
}
