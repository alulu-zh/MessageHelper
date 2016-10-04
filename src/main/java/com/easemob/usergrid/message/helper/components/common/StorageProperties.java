package com.easemob.usergrid.message.helper.components.common;

/**
 * Created by zhouhu on 5/10/2016.
 */
public class StorageProperties {
    private Config oss;
    private Config s3;

    public Config getOss() {
        return oss;
    }

    public void setOss(Config oss) {
        this.oss = oss;
    }

    public Config getS3() {
        return s3;
    }

    public void setS3(Config s3) {
        this.s3 = s3;
    }

    public static class Config {
        private Credential credential;
        private String region;
        private String bucket;
        private Properties properties;

        public Credential getCredential() {
            return credential;
        }

        public void setCredential(Credential credential) {
            this.credential = credential;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }
    }
    public static class Credential {
        private String accessKeyId;
        private String accessKeySecret;

        public String getAccessKeyId() {
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getAccessKeySecret() {
            return accessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
        }
    }
    public static class Properties {
        private int clientMaxConn;
        private int clientConnTimeOut;
        private int clientMaxErrRetry;
        private int clientSocketTimeOut;

        public int getClientMaxConn() {
            return clientMaxConn;
        }

        public void setClientMaxConn(int clientMaxConn) {
            this.clientMaxConn = clientMaxConn;
        }

        public int getClientConnTimeOut() {
            return clientConnTimeOut;
        }

        public void setClientConnTimeOut(int clientConnTimeOut) {
            this.clientConnTimeOut = clientConnTimeOut;
        }

        public int getClientMaxErrRetry() {
            return clientMaxErrRetry;
        }

        public void setClientMaxErrRetry(int clientMaxErrRetry) {
            this.clientMaxErrRetry = clientMaxErrRetry;
        }

        public int getClientSocketTimeOut() {
            return clientSocketTimeOut;
        }

        public void setClientSocketTimeOut(int clientSocketTimeOut) {
            this.clientSocketTimeOut = clientSocketTimeOut;
        }
    }
}
