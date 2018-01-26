package com.wlt.controller;


import com.wlt.jedismsg.RedisMsgPubSubListener;
import com.wlt.util.DateUtil;
import com.wlt.util.DownloadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * websocket控制器
 * @author wlt
 */
@Controller
@RequestMapping("/wsController")
public class WSController {
    private static final Logger logger = LoggerFactory.getLogger(WSController.class);

    @RequestMapping("/downloadLog")
    public void downloadLog(HttpServletResponse response, String timestamp, String channel) throws Exception {
        StringBuffer logFileName = new StringBuffer();
        logFileName.append(channel).append("-").append(DateUtil.getCurrentTimeFull("yyyyMMddHHmmss")).append(".log");
        logger.info("即将生成日志文件-{}",logFileName);
        String log = RedisMsgPubSubListener.logMap.get(channel+timestamp);
        log = channel + "\r\n" + log;
        Map map = new HashMap(16);
        log.replaceAll("/r/n","\r\n");
        map.put("日志频道",log);
        DownloadUtils.downLoad(response, map, logFileName.toString());
    }
}
