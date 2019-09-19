package cn.ddns.config;

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
    public static  String CHARSET_ENCONDING = "utf-8";
    /**
     * 获取ip的链接，获取网页，从网页上获得ip
     *  备用地址 https://www.ipip.net/ip.html
     */
    public static  String KEY_IP_URL = "https://www.ip.cn";

    //获取ip的正则表达式
    public static  String KEY_IP_REGEX_PATTERN = "(?=(\\b|\\D))(((\\d{1,2})|(1\\d{1,2})|(2[0-4]\\d)|(25[0-5]))\\.){3}((\\d{1,2})|(1\\d{1,2})|(2[0-4]\\d)|(25[0-5]))(?=(\\b|\\D))";
    /**
     * accesskey
     */
    public static  String KEY_ACCESS_KEY = "LT";
    /**
     * 密钥
     */
    public static  String KEY_ACCESS_SECRET = "15X";
    //主域名
    public static  String KEY_DOMAIN = "liuy.cc";
    //主机记录
    public static  String KEY_RR = "c";
}
