package com.wlt;

import redis.clients.jedis.Jedis;

import java.util.Date;

/** 
 * Created by leitao on 2017/3/3. 
 */  
public class TestPublish {  
    public static  void  main(String[] args) throws Exception{  
        Jedis jedis = new Jedis("127.0.0.1",6379,0);
        jedis.auth("123456");
//        jedis.publish("redisChat", "Redis is a great caching technique");
//        Thread.sleep(5000);
//        jedis.publish("redisChat", "build your dream");
//        Thread.sleep(5000);
        while(true){
            Thread.sleep(1000);
            System.out.println("wltredis");
            jedis.publish("test", "即将生成日志文件-连接建立成功！即将生成日志文件-连接建立成功！即将生成日志文件-连接建立成功！即将生成日志文件-连接建立成功！即将生成日志文件-连接建立成功！即将生成日志文件-连接建立成功！即将生成日志文件-连接建立成功！即将生成日志文件-连接建立成功！即将生成日志文件-连接建立成功！即将生成日志文件-连接建立成功！"+new Date().getSeconds());
        }
    }
}  