package com.easemob.usergrid.message.helper.config;

import com.easemob.usergrid.message.helper.components.common.CommonStorage;
import com.easemob.usergrid.message.helper.components.common.StorageProperties;
import com.easemob.usergrid.message.helper.components.oss.OSSStorage;
import com.easemob.usergrid.message.helper.components.s3.S3Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhouhu on 5/10/2016.
 */
@Configuration
public class StorageConfig {
    @Value("${service.config.storageMode:oss}")
    private String storageMode;
    private static final String OSS_MODE = "oss";
    private static final String S3_MODE = "s3";
    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix = "storage", ignoreInvalidFields = true)
    @Bean
    public StorageProperties storageProperties() {
        return new StorageProperties();
    }

    @ConditionalOnMissingBean(name = "OSSStorage")
    @Bean(name = "OSSStorage")
    public CommonStorage getOSSStorage(StorageProperties storageProperties) {
        if (storageMode.equalsIgnoreCase(OSS_MODE)) {
            return new OSSStorage(storageProperties.getOss());
        }
        return null;
    }

    @ConditionalOnMissingBean(name = "S3Storage")
    @Bean(name = "S3Storage")
    public CommonStorage getS3Storage(StorageProperties storageProperties) {
        if (storageMode.equalsIgnoreCase(S3_MODE)) {
            return new S3Storage(storageProperties.getS3());
        }
        return null;
    }
}
