package com.easemob.usergrid.message.helper;

import com.easemob.usergrid.message.helper.components.common.CommonStorage;
import com.easemob.usergrid.message.helper.components.common.FileInstance;
import com.easemob.usergrid.message.helper.exceptions.IllegalParametersException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Created by zhouhu on 5/10/2016.
 */
@Component
public class StorageManager {
    private static final Logger logger = LoggerFactory.getLogger(StorageManager.class);
    @Autowired
    @Qualifier("CommonStorage")
    private CommonStorage storage;

    public void upload(FileInstance file) {
        if (Objects.isNull(file)) {
            logger.error("upload | file instance was missing");
            throw new IllegalParametersException("file instance was missing");
        }
        storage.upload(file);
    }
}
