package com.easemob.usergrid.message.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouhu on 4/10/2016.
 */
@Component
public class AppConfigManager {
    private static final Map<Long, String> map = new HashMap<>();
    private static final String APPCONIFG_PREFIX = "appconfig:usergrid";
    private static final String APPCONIFG_SUFFIX = "properties";
    private static final String APPCONFIG_EXPIRED_PROPERTY = "";
    private static final String APPCONFIG_SPLIT = ":";
    private static final String DEFAULT_EXPIRED_TIME = "3D";
    static {
        map.put(4320L, "3D");
        map.put(0L, DEFAULT_EXPIRED_TIME);
    }
    @Autowired
    @Qualifier("salveConfigJedisPool")
    private ShardedJedisPool salveConfigJedisPool;

    public String getExperiedTime(String appkey) {
        try (ShardedJedis jedis = salveConfigJedisPool.getResource()) {
            String key = getKey(appkey);
            if (jedis.exists(key)) {
                return jedis.hget(key, APPCONFIG_EXPIRED_PROPERTY);
            }
        }
        return null;
    }

    private String getKey(String appkey) {
        return APPCONIFG_PREFIX + APPCONFIG_SPLIT + appkey + APPCONIFG_SUFFIX;
    }
}
