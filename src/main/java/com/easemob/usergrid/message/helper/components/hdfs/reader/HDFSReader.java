package com.easemob.usergrid.message.helper.components.hdfs.reader;

import com.easemob.usergrid.message.helper.Utils.CommonConstant;
import com.easemob.usergrid.message.helper.components.common.CommonReader;
import com.easemob.usergrid.message.helper.components.common.FileInstance;
import com.easemob.usergrid.message.helper.components.hdfs.HDFSProperties;
import com.easemob.usergrid.message.helper.exceptions.NullFileSystemException;
import com.easemob.usergrid.message.helper.exceptions.NullPathException;
import com.easemob.usergrid.message.helper.exceptions.ReadHDFSException;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by zhouhu on 1/10/2016.
 */
public class HDFSReader implements CommonReader {
    public static final String HDFS_CONNECTION = "HDFS.CONNECTION";
    public static final String HDFS_CONNECTION_POOL_MANAGER = "HDFS.CONNECTION.POOL.MANAGER";
    private static final Logger logger = LoggerFactory.getLogger(HDFSReader.class);
    private static final Logger list_logger = LoggerFactory.getLogger(CommonConstant.FAILED_LIST_FILES_LOGGER);
    private static final Logger read_logger = LoggerFactory.getLogger(CommonConstant.FAILED_READ_FILE_LOGGER);
    private static final int DEFAULT_TIMEOUT = 5;
    private HDFSProperties hdfsProperties;
    private HDFSConnectionPoolManager poolManager;

    public HDFSReader(HDFSProperties hdfsProperties) {
        this.hdfsProperties = hdfsProperties;
        init();
    }

    /**
     * Read file content from HDFS
     *
     * @return
     */
    @Override
    public InputStream read(String path) {
        if (StringUtils.isBlank(path)) {
            logger.error("read | path is null");
            throw new NullPathException("path is null");
        }
        logger.info("read | read HDFS file: {}", path);
        FileSystem fs = poolManager.getConnection(HDFSReader.DEFAULT_TIMEOUT);
        if (Objects.isNull(fs)) {
            throw new NullFileSystemException("FileSystem object is null");
        }
        FSDataInputStream FsInputStream = null;
        try {
            FsInputStream = fs.open(new Path(path));
        } catch (Exception e) {
            logger.error("read | Failed to read from HDFS path: {}", path, e);
            read_logger.error("failed path: {}", path);
            throw new ReadHDFSException("Failed to read file from HDFS", e);
        }
        return FsInputStream;
    }

    /**
     * List files status of a folder from HDFS
     *
     * @return
     */
    @Override
    public FileInstance[] list(String path) {
        if (StringUtils.isBlank(path)) {
            logger.error("read | path is null");
            throw new NullPathException("path is null");
        }
        logger.info("list | list HDFS files in path: {}", path);
        FileSystem fs = poolManager.getConnection(HDFSReader.DEFAULT_TIMEOUT);
        FileInstance[] fileInstances = null;
        try {
            FileStatus[] files = fs.listStatus(new Path(path));
            if (!Objects.isNull(files) && files.length > 0) {
                fileInstances = new FileInstance[files.length];
                for (int i = 0; i < files.length; i++) {
                    fileInstances[i] = createFileInstance(fs, files[i]);
                }
            }
        } catch (Exception e) {
            logger.error("list | Failed to list files from HDFS path: {}", path, e);
            list_logger.error("failed path: {}", path);
            throw new ReadHDFSException("Failed to list files from HDFS", e);
        }
        return fileInstances;
    }

    private void init() {
        poolManager = new HDFSConnectionPoolManager(hdfsProperties);
    }

    private FileInstance createFileInstance(FileSystem fs, FileStatus status) {
        if (Objects.isNull(status)) {
            return null;
        }
        Path path = status.getPath();
        FileInstance instance = new HDFSFileInstance(path.toString());
        instance.setName(path.getName());
        instance.setOrg(instance.getOrg());
        instance.setApp(instance.getApp());
        instance.setTimestamp(instance.getTimestamp());
        Map<String, Object> properties = new HashMap<>();
        properties.put(HDFSReader.HDFS_CONNECTION, fs);
        properties.put(HDFSReader.HDFS_CONNECTION_POOL_MANAGER, poolManager);
        instance.setProperties(properties);
        return instance;
    }
}
