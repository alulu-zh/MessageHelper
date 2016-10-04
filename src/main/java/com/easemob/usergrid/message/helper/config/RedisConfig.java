package com.easemob.usergrid.message.helper.config;

import com.easemob.usergrid.message.helper.components.redis.RedisConfigurationProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouhu on 4/10/2016.
 */
@Configuration
public class RedisConfig {
    public final static String JEDISPOOLCONFIG_BEAN = "jedisPoolConfig";

    @Value("${redis.pool.maxTotal:100}")
    private Integer poolMaxTotal;

    @ConditionalOnMissingBean(name = JEDISPOOLCONFIG_BEAN)
    @Bean(name = JEDISPOOLCONFIG_BEAN)
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(poolMaxTotal);
        return poolConfig;
    }

    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix = "cache", ignoreInvalidFields = true)
    @Bean
    public RedisConfigurationProperties redisConfigProperties() {
        return new RedisConfigurationProperties();
    }

    @ConditionalOnMissingBean(name = "salveConfigJedisPool")
    @Bean(name = "salveConfigJedisPool")
    public ShardedJedisPool salveConfigJedisPool(RedisConfigurationProperties redisConfigurationProperties,
            @Qualifier(JEDISPOOLCONFIG_BEAN) JedisPoolConfig config) {
        return createJedisPool(config, redisConfigurationProperties.getAppconfig(), false);
    }

    private static JedisShardInfo createJedisInfo(URI uri) {
        return new JedisShardInfo(uri.getHost(), uri.getPort());
    }

    private static ShardedJedisPool createJedisPool(JedisPoolConfig config, RedisConfigurationProperties.RedisCache file,
            boolean isMaster) {
        return new ShardedJedisPool(config, getShardInfo(file, isMaster));
    }

    private static List<JedisShardInfo> getShardInfo(RedisConfigurationProperties.RedisCache file, boolean isMaster) {
        List<JedisShardInfo> list = new ArrayList<>(2);
        RedisConfigurationProperties.RedisCacheCluster cluster = isMaster ? file.getMaster() : file.getSlave();
        list.add(createJedisInfo(cluster.getNode1()));
        list.add(createJedisInfo(cluster.getNode2()));
        return list;
    }
}
