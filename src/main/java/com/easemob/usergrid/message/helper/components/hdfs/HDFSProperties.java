package com.easemob.usergrid.message.helper.components.hdfs;

import org.apache.commons.lang.StringUtils;

/**
 * Created by zhouhu on 5/10/2016.
 */
public class HDFSProperties {
    private String baseUri;
    private String folder;
    private int maxConnection;

    public String getBaseUri() {
        baseUri = StringUtils.endsWith(baseUri, "/") ? StringUtils.substring(baseUri, 0, baseUri.length() - 2) : baseUri;
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public String getFolder() {
        folder = StringUtils.startsWith(folder, "/") ? folder : "/" + folder;
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public int getMaxConnection() {
        return maxConnection;
    }

    public void setMaxConnection(int maxConnection) {
        this.maxConnection = maxConnection;
    }
}
