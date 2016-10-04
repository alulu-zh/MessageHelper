package com.easemob.usergrid.message.helper.components.redis;

import java.net.URI;

/**
 * Created by zhouhu on 4/10/2016.
 */
public class RedisConfigurationProperties {
    private RedisCache appconfig = new RedisCache();

    public RedisCache getAppconfig() {
        return appconfig;
    }

    public void setAppconfig(RedisCache appconfig) {
        this.appconfig = appconfig;
    }

    public static class RedisCache {
        private RedisCacheCluster master;
        private RedisCacheCluster slave;

        public RedisCacheCluster getMaster() {
            return master;
        }

        public void setMaster(RedisCacheCluster master) {
            this.master = master;
        }

        public RedisCacheCluster getSlave() {
            return slave;
        }

        public void setSlave(RedisCacheCluster slave) {
            this.slave = slave;
        }
    }

    public static class RedisCacheCluster {
        private URI node1;
        private URI node2;

        public URI getNode1() {
            return node1;
        }

        public void setNode1(URI node1) {
            this.node1 = node1;
        }

        public URI getNode2() {
            return node2;
        }

        public void setNode2(URI node2) {
            this.node2 = node2;
        }
    }
}
