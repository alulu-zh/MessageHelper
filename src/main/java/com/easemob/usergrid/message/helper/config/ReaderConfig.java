package com.easemob.usergrid.message.helper.config;

import com.easemob.usergrid.message.helper.components.hdfs.HDFSProperties;
import com.easemob.usergrid.message.helper.components.hdfs.reader.HDFSReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Created by zhouhu on 5/10/2016.
 */
public class ReaderConfig {
    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix = "HDFS", ignoreInvalidFields = true)
    @Bean
    public HDFSProperties hdfsProperties() {
        return new HDFSProperties();
    }

    @ConditionalOnMissingBean(name = "HDFSReader")
    @Bean(name = "HDFSReader")
    public HDFSReader getHDFSReader(HDFSProperties hdfsProperties) {
        return new HDFSReader(hdfsProperties);
    }
}
