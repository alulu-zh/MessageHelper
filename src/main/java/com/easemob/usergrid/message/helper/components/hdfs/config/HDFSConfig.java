package com.easemob.usergrid.message.helper.components.hdfs.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhouhu on 2/10/2016.
 */
@Configuration
public class HDFSConfig {
    @Value("${service.config.HDFS.baseUri:127.0.0.1:8020}")
    private String baseUri;
    @Value("${service.config.HDFS.folder:/}")
    private String folder;
    @Value("${service.config.HDFS.maxConnection:20}")
    private int maxConnection;

    private String uri;

    public String getUri() {
        if (StringUtils.isBlank(uri)) {
            return uri = getBaseUri() + getFolder();
        }
        return uri;
    }

    public String getBaseUri() {
        baseUri = StringUtils.endsWith(baseUri, "/") ? StringUtils.substring(baseUri, 0, baseUri.length() - 2) : baseUri;
        return baseUri;
    }

    public String getFolder() {
        folder = StringUtils.startsWith(folder, "/") ? folder : "/" + folder;
        return folder;
    }

    public int getMaxConnection() {
        return maxConnection;
    }
}
