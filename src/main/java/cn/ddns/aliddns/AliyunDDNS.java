package cn.ddns.aliddns;

/**
 * Created by  liuyang
 * 2019/9/18    18:41
 * cn.ddns.aliddns
 * All Right Reserved by liuyang.
 **/

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.ddns.config.CommonConstants;
import cn.ddns.util.HttpUtil;
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

    private static final String getInternetIP(String url, String regexPattern) {
        String ret = null;
        String html = HttpUtil.sendGet(url, null);
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            ret = matcher.group(0);
        }
        return ret;
    }

    private static void updateIP() throws Exception {
        String ip = getInternetIP(CommonConstants.KEY_IP_URL, CommonConstants.KEY_IP_REGEX_PATTERN);
        IClientProfile clientProfile = DefaultProfile.getProfile("cn-hangzhou", CommonConstants.KEY_ACCESS_KEY, CommonConstants.KEY_ACCESS_SECRET);
        IAcsClient client = new DefaultAcsClient(clientProfile);
        DescribeDomainRecordsRequest request = new DescribeDomainRecordsRequest();
        request.setDomainName(CommonConstants.KEY_DOMAIN);
        DescribeDomainRecordsResponse response;
        boolean flag = false;
        response = client.getAcsResponse(request);
        for (Record record : response.getDomainRecords()) {
            if (record.getRR().equalsIgnoreCase(CommonConstants.KEY_RR) && record.getType().equalsIgnoreCase("A")) {
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
                    System.out.println("修改成功! oldIP=" + old_ip + " newIP" + cur_ip);
                } else {
                    System.out.println("不需要修改! 当前ip:" + cur_ip);
                }
            }
        }
        if (flag == false) {
            throw new RuntimeException("无法找到RR=" + CommonConstants.KEY_RR);
        }

    }


    public static void main(String[] args) throws IOException {
        try {
            updateIP();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}