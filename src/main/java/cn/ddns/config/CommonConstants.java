package cn.ddns.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by  liuyang
 * 2019/9/18    18:44
 * cn.ddns.config
 * All Right Reserved by liuyang.
 **/

public class CommonConstants {
    /**
     * HTTP请求格式
     */
    public static String CHARSET_ENCONDING = "utf-8";
    /**
     * 获取ip的链接，获取网页，从网页上获得ip
     * 备用地址 https://www.ipip.net/ip.html
     */
    public static String KEY_IP_URL = "https://www.ip.cn";

    //获取ip的正则表达式
    public static String KEY_IP_REGEX_PATTERN = "(?=(\\b|\\D))(((\\d{1,2})|(1\\d{1,2})|(2[0-4]\\d)|(25[0-5]))\\.){3}((\\d{1,2})|(1\\d{1,2})|(2[0-4]\\d)|(25[0-5]))(?=(\\b|\\D))";
    /**
     * accesskey
     */
    public static String KEY_ACCESS_KEY = "L";
    /**
     * 密钥
     */
    public static String KEY_ACCESS_SECRET = "1";
    //主域名
    public static String KEY_DOMAIN = "";
    //主机记录
    public static String KEY_RR = "";

    public static void main(String args[]) throws IOException {
        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = CommonConstants.class.getClassLoader().getResourceAsStream("config.properties");
        // 使用properties对象加载输入流
        properties.load(in);
        //获取key对应的value值
       String  domain =  properties.getProperty("KEY_DOMAIN");
        System.out.println(domain);
    }
}
