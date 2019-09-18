package cn.ddns.aliddns;

/**
 * Created by  liuyang
 * 2019/9/18    18:41
 * cn.ddns.aliddns
 * All Right Reserved by liuyang.
 **/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.ddns.config.CommonConstants;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse.Record;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;


public class AliyunDDNS {


    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + ((param == null || param.isEmpty()) ? "" : ("?" + param));
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("contentType", CommonConstants.CHARSET_ENCONDING);
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 建立实际的连接
            connection.connect();

            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), CommonConstants.CHARSET_ENCONDING));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    private static final String getInternetIP(String url, String regexPattern) {
        String ret = null;
        String html = sendGet(url, null);
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            ret = matcher.group(0);
        }
        return ret;
    }

    private static void updateIP(Map<String, String> config) throws Exception {
        String ip = getInternetIP(config.get(CommonConstants.KEY_IP_URL), config.get(CommonConstants.KEY_IP_REGEX_PATTERN));
        IClientProfile clientProfile = DefaultProfile.getProfile("cn-hangzhou", config.get(CommonConstants.KEY_ACCESS_KEY), config.get(CommonConstants.KEY_ACCESS_SECRET));
        IAcsClient client = new DefaultAcsClient(clientProfile);
        DescribeDomainRecordsRequest request = new DescribeDomainRecordsRequest();
        request.setDomainName(config.get(CommonConstants.KEY_DOMAIN));
        DescribeDomainRecordsResponse response;
        boolean flag = false;
        response = client.getAcsResponse(request);
        for (Record record : response.getDomainRecords()) {
            if (record.getRR().equalsIgnoreCase(config.get(CommonConstants.KEY_RR)) && record.getType().equalsIgnoreCase("A")) {
                String old_ip = record.getValue();
                String cur_ip = ip;
                flag = true;
                if (!old_ip.equals(cur_ip)) {
                    UpdateDomainRecordRequest udr_req = new UpdateDomainRecordRequest();
                    udr_req.setValue(cur_ip);
                    udr_req.setType(record.getType());
                    udr_req.setTTL(record.getTTL());
                    udr_req.setPriority(record.getPriority());
                    udr_req.setLine(record.getLine());
                    udr_req.setRecordId(record.getRecordId());
                    udr_req.setRR(record.getRR());
                    UpdateDomainRecordResponse udr_resp = client.getAcsResponse(udr_req);
                    System.out.println(udr_resp.toString());
                } else {
                    System.out.println("不需要修改");
                }
            }
        }
        if (flag == false) {
            throw new RuntimeException("无法找到RR=" + config.get(CommonConstants.KEY_RR));
        }

    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> ret = new HashMap<String, String>();
        for (int i = 0; i < args.length; ) {
            if (args[i].startsWith("-")) {
                ret.put(args[i], args[i + 1]);
                i += 2;
            } else {
                i++;
            }
        }
        return ret;
    }


    /**
     * -k xxx -s xxxx -d xxx.cn -rr www -url xxxx -reg (\d+\.){3}\d+
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            String ip =  getInternetIP(CommonConstants.KEY_IP_URL,CommonConstants.KEY_IP_REGEX_PATTERN);
            System.out.println(ip);
            Map<String, String> config = parseArgs(args);
            updateIP(config);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}