package com.wlt.controller;

import com.wlt.jedismsg.ComsumerJedisPool;
import com.wlt.jedismsg.ComsumerJedisThread;
import com.wlt.util.JedisPoolUtil;
import com.wlt.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@ServerEndpoint("/wsMonitor/{timestamp}")
public class WSMonitorController {
    private static final Logger logger = LoggerFactory.getLogger(WSMonitorController.class);
    private JedisPoolUtil jedisPoolUtil = SpringUtils.getBean("jedisPoolUtil");
  /**
   * 在线的客户端session集合，只在第一次new的时候初始化。
   */
    public static Map<String, Session> sessionMap=new HashMap<String, Session>();
    private static final String USER = "WSREDISMONITOR";
  /**
   * 接收信息事件
   * @param message 客户端发来的消息
   * @param session 当前会话
   */
  @OnMessage
  public void onMessage(String message,Session session,@PathParam(value="timestamp")String timestamp)throws Exception {
    ComsumerJedisThread comsumerJedisThread = ComsumerJedisPool.getComsumer(timestamp);
    String oldChannel = null;
    if (comsumerJedisThread != null) {
      oldChannel = comsumerJedisThread.getChannel();
    }
    if (!message.equals(oldChannel)) {
      ComsumerJedisPool.deleteComsumer(timestamp);
      comsumerJedisThread = new ComsumerJedisThread(jedisPoolUtil, message, timestamp);
      ComsumerJedisPool.addComsumer(comsumerJedisThread);
      //先清理旧频道再添加新频道
      Iterator<String> it = sessionMap.keySet().iterator();
      while(it.hasNext()){
        String key = it.next();
        if (!key.matches("^.*"+timestamp+"$")) {
          continue;
        }
        it.remove();
      }
      sessionMap.put(message+timestamp,session);
    } else {
      logger.info("重复订阅-{}", message);
    }
  }
  
  /**
   * 打开连接事件
 * @ throws Exception
   */
  @OnOpen
  public void onOpen(Session session,@PathParam(value="timestamp") String timestamp) throws Exception {
    logger.info("打开连接成功！当前对象：{}-{}", timestamp,sessionMap.size());
  }

  /**
   * 关闭连接事件
   */
  @OnClose
  public void onClose(Session session,@PathParam(value="timestamp")String timestamp) {
    Iterator<String> it = sessionMap.keySet().iterator();
    while(it.hasNext()){
      String key = it.next();
      if (!key.matches("^.*"+timestamp+"$")) {
        continue;
      }
      it.remove();
    }
    ComsumerJedisPool.deleteComsumer(timestamp);
    logger.info("关闭连接成功！清楚ws连接对象：{}-{}", timestamp,sessionMap.size());
  }
  
  /**
   * 错误信息响应事件
   * @param session
   * @param throwable
   */
  @OnError
  public void OnError(Session session,Throwable throwable,@PathParam(value="timestamp")String timestamp) {
        ComsumerJedisPool.deleteComsumer(timestamp);
	    logger.info("异常："+throwable.getMessage());
  }
  
}