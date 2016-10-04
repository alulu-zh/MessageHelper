package com.easemob.usergrid.message.helper.components.common;

import java.io.InputStream;

/**
 * Created by zhouhu on 1/10/2016.
 */
public interface CommonReader {
    InputStream read(String path);
    FileInstance[] list(String path);
}
