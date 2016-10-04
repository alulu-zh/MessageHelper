package com.easemob.usergrid.message.helper.Utils;

import com.easemob.usergrid.message.helper.components.common.FileInstance;
import com.easemob.usergrid.message.helper.components.hdfs.reader.HDFSConnectionPoolManager;
import com.easemob.usergrid.message.helper.components.hdfs.reader.HDFSReader;
import com.easemob.usergrid.message.helper.exceptions.CommonRuntimeException;
import org.apache.hadoop.fs.FileSystem;

import java.util.Map;
import java.util.Objects;

/**
 * Created by zhouhu on 4/10/2016.
 */
public class FileReaderUtil {
    public static void close(FileInstance file) {
        if (!Objects.isNull(file)) {
            if (!Objects.isNull(file.getContent())) {
                try {
                    file.getContent().close();
                    releaseConnection(file);
                } catch (Exception e) {
                    throw new CommonRuntimeException(String.format("Failed to close file: %s", file.getPath()), e);
                }
            }
        }
    }

    private static void releaseConnection(FileInstance file) {
        Map<String, Object> properties = file.getProperties();
        if (properties.containsKey(HDFSReader.HDFS_CONNECTION) && properties.containsKey(HDFSReader.HDFS_CONNECTION_POOL_MANAGER)) {
            FileSystem fs = (FileSystem) properties.get(HDFSReader.HDFS_CONNECTION);
            HDFSConnectionPoolManager poolManager = (HDFSConnectionPoolManager) properties.get(HDFSReader.HDFS_CONNECTION_POOL_MANAGER);
            poolManager.releaseConnection(fs);
        }
    }

    private FileReaderUtil() {
        throw new IllegalAccessError("FileReaderUtil is utility class");
    }
}
