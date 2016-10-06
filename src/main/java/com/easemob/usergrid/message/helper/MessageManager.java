package com.easemob.usergrid.message.helper;

import com.easemob.usergrid.message.helper.components.common.FileInstance;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * Created by zhouhu on 5/10/2016.
 */
@Component
public class MessageManager {
    private static final Logger logger = LoggerFactory.getLogger(MessageManager.class);
    @Autowired
    private ReaderManager readerManager;

    @Autowired
    private AppConfigManager appConfigManager;

    @Autowired
    private StorageManager storageManager;

    @Value("service.config.thread.readTaskThreadCount:2")
    private int readTaskThreadCount;

    @Value("service.config.thread.uploadTaskThreadCount:30")
    private int uploadTaskThreadCount;

    @Value("${service.config.checkTimestamp:true}")
    boolean checkTimestamp;

    private static final String APPKEY_SPLIT = "#";
    private static final String UPLOAD_FILE_PATH_SPLIT = "/";
    private static final String UPLOAD_FILE_PATH_SUFFIX = ".gz";
    private static final int START_MINUTE = 20;

    @PostConstruct
    public void init() {
        ScheduledExecutorService readTaskPool = Executors.newScheduledThreadPool(readTaskThreadCount);
        ExecutorService uploadTaskPool = Executors.newFixedThreadPool(uploadTaskThreadCount);
        readTaskPool.scheduleAtFixedRate(
                new ReadTask(readerManager, appConfigManager, storageManager, uploadTaskThreadCount, uploadTaskPool, checkTimestamp),
                getInitailDelay(), 60 * 60 * 1000L, TimeUnit.MILLISECONDS);
    }

    private long getInitailDelay() {
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);
        long delay = 0L;
        if (minute < START_MINUTE) {
            long current = calendar.getTimeInMillis();
            calendar.set(Calendar.MINUTE, START_MINUTE);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            delay = calendar.getTimeInMillis() - current;
        } else {
            long current = calendar.getTimeInMillis();
            calendar.add(Calendar.HOUR, 1);
            calendar.set(Calendar.MINUTE, START_MINUTE);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            delay = calendar.getTimeInMillis() - current;
        }
        return delay;
    }

    private static class ReadTask implements Runnable {
        ReaderManager readerManager;
        AppConfigManager appConfigManager;
        StorageManager storageManager;
        int uploadTaskThreadCount;
        ExecutorService uploadTaskPool;
        boolean checkTimestamp = true;
        long current;
        long previous;

        public ReadTask(ReaderManager readerManager, AppConfigManager appConfigManager, StorageManager storageManager,
                int uploadTaskThreadCount, ExecutorService uploadTaskPool, boolean checkTimestamp) {
            this.readerManager = readerManager;
            this.appConfigManager = appConfigManager;
            this.storageManager = storageManager;
            this.uploadTaskThreadCount = uploadTaskThreadCount;
            this.uploadTaskPool = uploadTaskPool;
            this.checkTimestamp = checkTimestamp;
            if (checkTimestamp) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                this.current = calendar.getTimeInMillis();
                this.previous = current - 60 * 60 * 1000L;
            }
        }

        @Override
        public void run() {
            if (Objects.isNull(readerManager) || Objects.isNull(appConfigManager) || Objects.isNull(storageManager)) {
                logger.error("run | readerManager, appConfigManager or storageManager was missing");
                return;
            }
            FileInstance[] files = readerManager.readFiles();
            for (FileInstance fi : files) {
                if (checkFileInstanceTimestamp(fi, checkTimestamp)) {
                    try {
                        getAliasName(fi);
                        uploadTaskPool.submit(new UploadTask(storageManager, fi));
                    } catch (Exception e) {
                        logger.error("run | ReadTask run method is failed", e);
                    }
                }
            }
        }

        private void getAliasName(FileInstance fileInstance) {
            if (StringUtils.isNotBlank(fileInstance.getAliasName())) {
                return;
            }
            if (Objects.isNull(fileInstance) || StringUtils.isBlank(fileInstance.getOrg()) || StringUtils.isBlank(fileInstance.getApp())
                    || fileInstance.getTimestamp() <= 0) {
                logger.error("getAliasName | fileInstance, org, app or timestamp was missing");
                return;
            }
            addExpiredTime(fileInstance);
            if (StringUtils.isBlank(fileInstance.getExpeiredTime())) {
                logger.error("getAliasName | fileinstance expired time is blank");
                return;
            }
            String aliasName = fileInstance.getExpeiredTime() + UPLOAD_FILE_PATH_SPLIT + fileInstance.getOrg() + UPLOAD_FILE_PATH_SPLIT
                    + fileInstance.getApp() + UPLOAD_FILE_PATH_SPLIT + fileInstance.getTimestamp() + UPLOAD_FILE_PATH_SUFFIX;
            fileInstance.setAliasName(aliasName);
        }

        private void addExpiredTime(FileInstance fileInstance) {
            if (StringUtils.isNotBlank(fileInstance.getExpeiredTime())) {
                return;
            }
            if (Objects.isNull(fileInstance) || StringUtils.isBlank(fileInstance.getOrg()) || StringUtils.isBlank(fileInstance.getApp())) {
                logger.error("addExpiredTime | fileInstance, org or app was missing");
                return;
            }
            String appkey = fileInstance.getOrg() + APPKEY_SPLIT + fileInstance.getApp();
            fileInstance.setExpeiredTime(appConfigManager.getExperiedTime(appkey));
        }

        private boolean checkFileInstanceTimestamp(FileInstance instance, boolean checkTimestamp) {
            if (!checkTimestamp) {
                return true;
            }
            if (Objects.isNull(instance) || instance.getTimestamp() <= 0) {
                return false;
            }
            long timestamp = instance.getTimestamp();
            return timestamp >= previous && timestamp <= current;
        }
    }

    private static class UploadTask implements Callable<Object> {
        StorageManager storageManager;
        FileInstance fileInstance;

        public UploadTask(StorageManager storageManager, FileInstance fileInstance) {
            this.storageManager = storageManager;
            this.fileInstance = fileInstance;
        }

        @Override
        public Object call() throws Exception {
            try {
                storageManager.upload(fileInstance);
            } catch (Exception e) {
                logger.error("call | UploadTask call method is failed", e);
            }
            return null;
        }
    }

}
