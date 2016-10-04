package com.easemob.usergrid.message.helper.components.hdfs.reader;

import com.easemob.usergrid.message.helper.Utils.TimeUtil;
import com.easemob.usergrid.message.helper.components.hdfs.HDFSProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created by zhouhu on 3/10/2016.
 */
public class HDFSConnectionPoolManager {
    private static final Logger logger = LoggerFactory.getLogger(HDFSConnectionPoolManager.class);
    private HDFSProperties hdfsProperties;
    private List<FileSystemWrapper> pool = new LinkedList<>();
    private static final long DEFAULT_INTERVAL_TIME = 1 * 1000L;

    public HDFSConnectionPoolManager(HDFSProperties hdfsProperties) {
        this.hdfsProperties = hdfsProperties;
        init();
    }

    private void init() {
        if (Objects.isNull(hdfsProperties)) {
            logger.error("init | HDFSProperties is null");
            return;
        }
        String baseUri = hdfsProperties.getBaseUri();
        if (StringUtils.isBlank(baseUri)) {
            logger.error("init | HDFS baseUri is null");
            return;
        }
        int poolSize = 1;
        if (hdfsProperties.getMaxConnection() > 0) {
            poolSize = hdfsProperties.getMaxConnection();
        }
        for (int i = 0; i < poolSize; i++) {
            try {
                FileSystem fs = FileSystem.get(URI.create(baseUri), new Configuration());
                pool.add(new FileSystemWrapper(fs, FileSystemWrapper.UNUSED));
            } catch (IOException e) {
                logger.error("init | Failed to new FileSystem", e);
                continue;
            }
        }
    }

    public FileSystem getConnection() {
        Predicate<?> predicate = (o) -> true;
        return getConnection(predicate, null);
    }

    public FileSystem getConnection(int timeout) {
        Predicate<Long> predicate = (t) -> System.currentTimeMillis() < t;
        return getConnection(predicate, Long.valueOf(System.currentTimeMillis() + timeout * 1000L));
    }

    public FileSystem getConnection(Predicate predicate, Object param) {
        if (Objects.isNull(pool) || pool.size() <= 0) {
            logger.error("getConnection | HDFS connection pool is null or blank");
            return null;
        }
        while (predicate.test(param)) {
            for (FileSystemWrapper fsw : pool) {
                synchronized (fsw) {
                    if (fsw.getStatus() == FileSystemWrapper.UNUSED) {
                        fsw.setStatus(FileSystemWrapper.USED);
                        return fsw.getFs();
                    }
                }
            }
            TimeUtil.sleep(HDFSConnectionPoolManager.DEFAULT_INTERVAL_TIME);
        }
        logger.info("getConnection | Failed to get unused HDFS connection");
        return null;
    }

    public void releaseConnection(FileSystem fs) {
        if (Objects.isNull(pool) || pool.size() <= 0) {
            return;
        }
        for (FileSystemWrapper fsw : pool) {
            synchronized (fsw) {
                if (fsw.getFs() == fs && fsw.getStatus() != FileSystemWrapper.CLOSED) {
                    fsw.setStatus(FileSystemWrapper.UNUSED);
                    return;
                }
            }
        }
    }

    public void close() {
        if (Objects.isNull(pool)) {
            return;
        }
        for (FileSystemWrapper fsw : pool) {
            synchronized (fsw) {
                try {
                    fsw.setStatus(FileSystemWrapper.CLOSED);
                    fsw.getFs().close();
                    pool.remove(fsw);
                } catch (IOException e) {
                    logger.error("close | Failed to close FileSystem in the HDFS connection pool", e);
                }
            }
        }
    }

    private static class FileSystemWrapper {
        private static final int UNUSED = 0;
        private static final int USED = 1;
        private static final int CLOSED = 2;
        private FileSystem fs;
        private int status;

        public FileSystemWrapper(FileSystem fs, int status) {
            this.fs = fs;
            this.status = status;
        }

        public FileSystem getFs() {
            return fs;
        }

        public void setFs(FileSystem fs) {
            this.fs = fs;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
