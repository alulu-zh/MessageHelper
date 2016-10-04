package com.easemob.usergrid.message.helper.components.s3;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
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
public class S3Storage implements CommonStorage {
    private static final Logger logger = LoggerFactory.getLogger(S3Storage.class);
    private static final Logger upload_logger = LoggerFactory.getLogger("failed_upload_file");
    private AmazonS3 client;
    private String bucket;

    public S3Storage(StorageProperties.Config storageConfig) {
        createStorageClient(storageConfig);
    }

    @Override
    public void upload(FileInstance file) {
        if (Objects.isNull(file)) {
            logger.error("upload | oss upload file is null");
        }
        if (Objects.isNull(client)) {
            logger.error("upload | Failed to upload file: {} for s3 client is null", file.getPath());
            upload_logger.error("Failed path: {}", file.getPath());
        }
        try {
            InputStream inputStream = file.getContent();
            String key = file.getAliasName();
            client.putObject(new PutObjectRequest(bucket, key, inputStream, new ObjectMetadata()));
        } catch (Exception e) {
            logger.error("upload | Failed to upload file: {} by s3 client", file.getPath(), e);
            upload_logger.error("Failed path: {}", file.getPath());
            throw new UploadFileException(String.format("Failed to upload file: %s by s3 client", file.getPath()), e);
        } finally {
            FileReaderUtil.close(file);
        }
    }

    private void createStorageClient(StorageProperties.Config storageConfig) {
        AWSCredentials awsCredentials = createAWSCredentials(storageConfig.getCredential());
        ClientConfiguration awsConfiguration = createAWSConfiguation(storageConfig.getProperties());
        client = new AmazonS3Client(awsCredentials, awsConfiguration);
        Region region = Region.getRegion(Regions.fromName(storageConfig.getRegion()));
        client.setRegion(region);
        bucket = storageConfig.getBucket();
    }

    private AWSCredentials createAWSCredentials(StorageProperties.Credential credential) {
        String accessKeyId = credential.getAccessKeyId();
        String accessKeySecret = credential.getAccessKeySecret();
        return new BasicAWSCredentials(accessKeyId, accessKeySecret);
    }

    private ClientConfiguration createAWSConfiguation(StorageProperties.Properties properties) {
        ClientConfiguration conf = new ClientConfiguration();
        conf.setMaxConnections(properties.getClientMaxConn());
        conf.setConnectionTimeout(properties.getClientConnTimeOut());
        conf.setMaxErrorRetry(properties.getClientMaxErrRetry());
        conf.setSocketTimeout(properties.getClientSocketTimeOut());
        return conf;
    }
}
