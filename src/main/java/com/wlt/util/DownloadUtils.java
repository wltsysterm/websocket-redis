package com.wlt.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 证书下载工具类
 * Created by wang on 2017/7/25.
 */
public class DownloadUtils {
    public static void downLoad(HttpServletResponse resp, Map<String, Object> props, String fileName) throws Exception {
        if (resp == null || props == null || StringUtils.isEmpty(fileName)) {
            throw new Exception("参数异常");
        }
        try {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, Object> entry : props.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\r\n");
            }


            setResponseHeader(resp, fileName);
            PrintWriter writer = resp.getWriter();
            writer.write(sb.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
