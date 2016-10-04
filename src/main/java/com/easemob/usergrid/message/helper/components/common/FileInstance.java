package com.easemob.usergrid.message.helper.components.common;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by zhouhu on 2/10/2016.
 */
public abstract class FileInstance {
    protected String org;
    protected String app;
    protected long timestamp = 0;
    protected String expeiredTime;
    protected String name;
    protected String path;
    // name for storage, e.g. oss or s3
    protected String aliasName;
    protected InputStream content;
    protected Map<String, Object> properties;

    public FileInstance(String path) {
        this.path = path;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getExpeiredTime() {
        return expeiredTime;
    }

    public void setExpeiredTime(String expeiredTime) {
        this.expeiredTime = expeiredTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public InputStream getContent() {
        return content;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
