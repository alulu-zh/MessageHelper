package com.easemob.usergrid.message.helper.exceptions;

/**
 * Created by zhouhu on 3/10/2016.
 */
public class UploadFileException extends RuntimeException {

    private static final long serialVersionUID = -5217860713832771534L;

    public UploadFileException(Exception e) {
        super(e);
    }

    public UploadFileException(String msg, Exception e) {
        super(msg, e);
    }

    public UploadFileException(String msg) {
        super(msg);
    }
}
