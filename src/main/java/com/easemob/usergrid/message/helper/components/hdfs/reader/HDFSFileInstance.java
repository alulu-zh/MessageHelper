package com.easemob.usergrid.message.helper.components.hdfs.reader;

import com.easemob.usergrid.message.helper.components.common.FileInstance;
import org.apache.commons.lang.StringUtils;

/**
 * Created by zhouhu on 4/10/2016.
 */
public class HDFSFileInstance extends FileInstance {
    private static final String SPLIT = "_";
    private static final String EXTENTION_SPLIT = "\\.";

    public HDFSFileInstance(String path) {
        super(path);
    }

    @Override
    public String getOrg() {
        if (StringUtils.isNotBlank(org)) {
            return org;
        }
        if (StringUtils.isNotBlank(name)) {
            org = name.split(SPLIT)[0];
            return org;
        }
        return null;
    }

    @Override
    public String getApp() {
        if (StringUtils.isNotBlank(app)) {
            return app;
        }
        if (StringUtils.isNotBlank(name)) {
            String[] split = name.split(SPLIT);
            if (split.length >= 2) {
                app = split[1];
                return app;
            }
        }
        return null;
    }

    @Override
    public long getTimestamp() {
        if (timestamp > 0L) {
            return timestamp;
        }
        String[] split = name.split(SPLIT);
        if (split.length >= 3) {
            String temp = split[2];
            String[] split1 = temp.split(EXTENTION_SPLIT);
            if (split1.length >= 2) {
                timestamp = Long.valueOf(split1[0]);
                return timestamp;
            }
        }
        return 0L;
    }

}
