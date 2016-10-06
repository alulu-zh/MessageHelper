package com.easemob.usergrid.message.helper;

import com.easemob.usergrid.message.helper.components.common.CommonReader;
import com.easemob.usergrid.message.helper.components.common.FileInstance;
import com.easemob.usergrid.message.helper.components.hdfs.HDFSProperties;
import com.easemob.usergrid.message.helper.config.ReaderConfig;
import com.easemob.usergrid.message.helper.exceptions.IllegalParametersException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Objects;

/**
 * Created by zhouhu on 5/10/2016.
 */
@Component
public class ReaderManager {
    private static final Logger logger = LoggerFactory.getLogger(ReaderManager.class);
    @Value("${service.config.readerMode:HDFS}")
    private String readerMode;

    @Autowired
    @Qualifier("CommonReader")
    private CommonReader reader;

    @Autowired
    @Qualifier("HDFSProperties")
    private HDFSProperties hdfsProperties;

    public FileInstance[] readFiles() {
        if (readerMode.equalsIgnoreCase(ReaderConfig.HDFS_MODE)) {
            return readHDFSFiles();
        }
        return null;
    }

    private FileInstance[] readHDFSFiles() {
        if (Objects.isNull(hdfsProperties) || Objects.isNull(reader)) {
            logger.error("readFiles | reader or properties was missing");
            throw new IllegalParametersException("reader or properties was missing");
        }
        FileInstance[] files = reader.list(hdfsProperties.getFolder());
        initialFileInstances(files);
        return files;
    }

    private void initialFileInstances(FileInstance[] files) {
        if (Objects.isNull(files)) {
            logger.error("initialFileInstances | file instances were missing");
            throw new IllegalParametersException("file instances were missing");
        }
        for (FileInstance fi : files) {
            InputStream inputStream = reader.read(fi.getPath());
            fi.setContent(inputStream);
        }
    }
}
