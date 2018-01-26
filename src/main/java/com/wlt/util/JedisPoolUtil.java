package com.wlt.util;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisPoolUtil {
    private JedisPool jedisPool;

    public JedisPoolUtil(String host, int port, int maxIdle, int maxTotal, boolean testOnBorrow, int maxWaitMillis, int timeout, String password) {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMaxTotal(maxTotal);
        genericObjectPoolConfig.setTestOnBorrow(testOnBorrow);
        genericObjectPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPool = new JedisPool(genericObjectPoolConfig, host.trim(), port, timeout, password.trim());
    }

    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    public void destroy() {
        jedisPool.destroy();
    }
}
