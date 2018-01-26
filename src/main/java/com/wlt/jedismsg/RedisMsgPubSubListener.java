package com.wlt.jedismsg;


import com.wlt.controller.WSMonitorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * redis消息订阅发布接口
 * @author wlt
 */  
public class RedisMsgPubSubListener extends JedisPubSub {
    private static final Logger logger = LoggerFactory.getLogger(RedisMsgPubSubListener.class);

    /**
     * 每个session维持的日志变量
     */
    public static Map<String,String> logMap = new HashMap<>(16);
    @Override  
    public void onMessage(String channel, String message) {
        logger.debug("channel:{},收到 message :{}", channel, message);
        Map<String, Session> sessionMap = WSMonitorController.sessionMap;
        try {
            Iterator<String> it = sessionMap.keySet().iterator();
            //循环给每个客户端发送信息
            while(it.hasNext()){
                String key = it.next();
                if (!key.matches("^"+channel+".*$")) {
                    continue;
                }
                Session value = sessionMap.get(key);
                if (value!=null) {
                    setLog(message,key);
                    value.getBasicRemote().sendText(message);
                } else {
                    logger.info("ws通信对象不存在："+key);
                }
            }
        } catch (Exception e) {
            logger.info("接收消息事件异常!");
        }
    }

    /**
     * 删除本用户的日志缓存变量
     * @param timestamp
     */
    public static void deleteLogVariable(String timestamp){
        synchronized (logMap) {
            Iterator<String> it = logMap.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                if (!key.matches("^.*" + timestamp + "$")) {
                    continue;
                }
                it.remove();
            }
        }
    }
    /**
     * 持续更新对应session的log变量值
     * @param message
     * @param key
     */
    private void setLog(String message, String key){
        synchronized (logMap) {
            String value = logMap.get(key);
            if (value == null) {
                logMap.put(key,message);
            } else {
                value += "\r\n";
                value += message;
                logMap.put(key,value);
            }
        }

    }

    @Override  
    public void onSubscribe(String channel, int subscribedChannels) {
        logger.info("channel:{},被订阅，订阅剩余:{}", channel, subscribedChannels);
    }  
  
    @Override  
    public void onUnsubscribe(String channel, int subscribedChannels) {
        logger.info("channel:{},被取消订阅，订阅剩余:{}", channel, subscribedChannels);
    }  
}