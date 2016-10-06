package com.easemob.usergrid.message.helper.config;

import com.easemob.usergrid.message.helper.components.common.CommonReader;
import com.easemob.usergrid.message.helper.components.hdfs.HDFSProperties;
import com.easemob.usergrid.message.helper.components.hdfs.reader.HDFSReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Created by zhouhu on 5/10/2016.
 */
public class ReaderConfig {
    public static final String HDFS_MODE = "HDFS";
    @Value("${service.config.readerMode:HDFS}")
    private String readerMode;

    @ConditionalOnMissingBean(name = "HDFSProperties")
    @ConfigurationProperties(prefix = "HDFS", ignoreInvalidFields = true)
    @Bean(name = "HDFSProperties")
    public HDFSProperties hdfsProperties() {
        return new HDFSProperties();
    }

    @ConditionalOnMissingBean(name = "CommonReader")
    @Bean(name = "CommonReader")
    public CommonReader getCommonReader(HDFSProperties hdfsProperties) {
        if (readerMode.equalsIgnoreCase(HDFS_MODE)) {
            return new HDFSReader(hdfsProperties);
        }
        return null;
    }

    private HDFSReader getHDFSReader(HDFSProperties hdfsProperties) {
        return new HDFSReader(hdfsProperties);
    }
}
