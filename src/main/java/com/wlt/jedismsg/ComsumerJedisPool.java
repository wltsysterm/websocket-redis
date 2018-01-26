package com.wlt.jedismsg;


import com.wlt.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 魏霖涛
 * @since 2018/1/23 0023
 */
public class ComsumerJedisPool {
    private static final Logger logger = LoggerFactory.getLogger(ComsumerJedisPool.class);
    private static TaskExecutor taskExecutor = SpringUtils.getBean("taskExecutor");
    /**
     * 订阅redis消息的comsumer对象
     */
    private static Map<String,ComsumerJedisThread> comsumerPool = new HashMap<>(16);
    public static ComsumerJedisThread getComsumer(String key){
        return comsumerPool.get(key);
    }
    public static void addComsumer(ComsumerJedisThread comsumerJedisThread){
        synchronized (comsumerPool) {
            comsumerPool.put(comsumerJedisThread.getKey(),comsumerJedisThread);
            taskExecutor.execute(comsumerJedisThread);
        }
    }
    public static void deleteComsumer(String timestamp){
        synchronized (comsumerPool) {
            ComsumerJedisThread comsumerJedisThread = comsumerPool.get(timestamp);
            if (comsumerJedisThread != null) {
                comsumerJedisThread.unSubscribe();
                comsumerPool.remove(timestamp);
            }
            RedisMsgPubSubListener.deleteLogVariable(timestamp);
        }
    }
}
