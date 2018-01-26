package com.wlt.jedismsg;


import com.wlt.util.JedisPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * @author 魏霖涛
 * @since 2018/1/23 0023
 */
public class ComsumerJedisThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(ComsumerJedisThread.class);
    private JedisPoolUtil jedisPoolUtil;
    private String channel;
    private String key;
    private Jedis jedis;
    private RedisMsgPubSubListener listener;

    public String getKey() {
        return key;
    }

    public String getChannel() {
        return channel;
    }

    public ComsumerJedisThread(JedisPoolUtil jedisPoolUtil, String channel, String key ) {
        this.jedisPoolUtil = jedisPoolUtil;
        this.channel = channel;
        this.key = key;
        this.jedis = jedisPoolUtil.getJedis();
        this.listener = new RedisMsgPubSubListener();
    }

    public void unSubscribe(){
        logger.info("准备取消订阅...");
        listener.unsubscribe();
    }
    @Override
    public void run() {
        //启动消息订阅
        logger.info("订阅redis频道...");
        jedis.subscribe(listener, channel);
    }
}
