package cn.ddns.util;

import cn.ddns.aliddns.AliyunDDNS;
import cn.ddns.config.CommonConstants;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by  liuyang
 * 2019/9/19    18:40
 * cn.ddns.util
 * All Right Reserved by liuyang.
 **/

public class PropertiesUtils {
    public static void init(String path) {
        System.out.println("config.properties path:" + path);
        InputStream in = null;
        Properties prop = new Properties();
        try {
            in = new BufferedInputStream(new FileInputStream(new File(path)));
            prop.load(in);
            Iterator<Map.Entry<Object, Object>> it = prop.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Object, Object> entry = it.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                System.out.println(entry.getKey() + " "+ value);
                if (key.equals("KEY_ACCESS_KEY")) {
                    CommonConstants.KEY_ACCESS_KEY = String.valueOf(value);
                } else if (key.equals("KEY_ACCESS_SECRET")) {
                    CommonConstants.KEY_ACCESS_SECRET = String.valueOf(value);
                } else if (key.equals("KEY_DOMAIN")) {
                    CommonConstants.KEY_DOMAIN = String.valueOf(value);
                } else if (key.equals("KEY_RR")) {
                    CommonConstants.KEY_RR = String.valueOf(value);
                }
            }
            System.out.println("********************");
            System.out.println(CommonConstants.KEY_ACCESS_KEY);
            System.out.println(CommonConstants.KEY_ACCESS_SECRET);
            System.out.println(CommonConstants.KEY_DOMAIN);
            System.out.println(CommonConstants.KEY_RR);
            System.out.println("********************");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
