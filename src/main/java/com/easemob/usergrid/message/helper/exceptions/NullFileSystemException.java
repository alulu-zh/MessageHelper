package com.easemob.usergrid.message.helper.exceptions;

/**
 * Created by zhouhu on 3/10/2016.
 */
public class NullFileSystemException extends RuntimeException {
    private static final long serialVersionUID = 3708666044283922453L;

    public NullFileSystemException(Exception e) {
        super(e);
    }

    public NullFileSystemException(String msg, Exception e) {
        super(msg, e);
    }

    public NullFileSystemException(String msg) {
        super(msg);
    }
}
