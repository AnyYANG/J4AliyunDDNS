package cn.ddns.util;

import cn.ddns.aliddns.AliyunDDNS;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * Created by  liuyang
 * 2019/9/19    18:40
 * cn.ddns.util
 * All Right Reserved by liuyang.
 **/

public class PropertiesUtils {

    public static void init() {
        ClassLoader classLoader = PropertiesUtils.class.getClassLoader();
        URL resource = classLoader.getResource("config.properties");
        String path = resource.getPath();
        System.out.println(path);
        InputStream in = null;
        Properties prop = new Properties();
        try {
            in = new BufferedInputStream(new FileInputStream(new File(path)));
            prop.load(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
