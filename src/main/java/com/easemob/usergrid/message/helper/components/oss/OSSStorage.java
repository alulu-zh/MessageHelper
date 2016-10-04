package com.easemob.usergrid.message.helper.components.oss;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.easemob.usergrid.message.helper.Utils.FileReaderUtil;
import com.easemob.usergrid.message.helper.components.common.CommonStorage;
import com.easemob.usergrid.message.helper.components.common.FileInstance;
import com.easemob.usergrid.message.helper.components.common.StorageProperties;
import com.easemob.usergrid.message.helper.exceptions.UploadFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Objects;

/**
 * Created by zhouhu on 5/10/2016.
 */
public class OSSStorage implements CommonStorage {
    private static final Logger logger = LoggerFactory.getLogger(OSSStorage.class);
    private static final Logger upload_logger = LoggerFactory.getLogger("failed_upload_file");
    private OSSClient client;
    private String bucket;

    public OSSStorage(StorageProperties.Config storageConfig) {
        createStorageClient(storageConfig);
    }

    @Override
    public void upload(FileInstance file) {
        if (Objects.isNull(file)) {
            logger.error("upload | oss upload file is null");
        }
        if (Objects.isNull(client)) {
            logger.error("upload | Failed to upload file: {} for oss client is null", file.getPath());
            upload_logger.error("Failed path: {}", file.getPath());
        }
        try {
            InputStream inputStream = file.getContent();
            String key = file.getAliasName();
            client.putObject(bucket, key, inputStream);
        } catch (Exception e) {
            logger.error("upload | Failed to upload file: {} by oss client", file.getPath(), e);
            upload_logger.error("Failed path: {}", file.getPath());
            throw new UploadFileException(String.format("Failed to upload file: %s by oss client", file.getPath()), e);
        } finally {
            FileReaderUtil.close(file);
        }
    }

    private void createStorageClient(StorageProperties.Config storageConfig) {
        StorageProperties.Credential credential = storageConfig.getCredential();
        String region = storageConfig.getRegion();
        String accessKeyId = credential.getAccessKeyId();
        String accessKeySecret = credential.getAccessKeySecret();
        ClientConfiguration conf = createOSSConfiguation(storageConfig.getProperties());
        client = new OSSClient(region, accessKeyId, accessKeySecret, conf);
        bucket = storageConfig.getBucket();
    }

    private ClientConfiguration createOSSConfiguation(StorageProperties.Properties properties) {
        ClientConfiguration conf = new ClientConfiguration();
        conf.setMaxConnections(properties.getClientMaxConn());
        conf.setConnectionTimeout(properties.getClientConnTimeOut());
        conf.setMaxErrorRetry(properties.getClientMaxErrRetry());
        conf.setSocketTimeout(properties.getClientSocketTimeOut());
        return conf;
    }
}
