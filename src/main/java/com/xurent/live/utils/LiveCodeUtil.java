package com.xurent.live.utils;

import com.xurent.live.config.LiveConfig;
import com.xurent.live.model.LiveUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@Component
public class LiveCodeUtil {


    @Autowired
    private LiveConfig config;


    @Autowired
    private  LiveUrl urls;



    public  LiveUrl getUrls( String randomStr,int day){


        //过期时间
        String expirationTime = setLong(day);
        //Unix时间戳
        Long unixTime = getUnixTime(expirationTime);


        String streamId = config.getBizId()+"_"+randomStr;
        //时间戳16进制
        String txTime = Integer.toHexString(unixTime.intValue()).toUpperCase();

        //获取md5 txSecret
        String txSecret = getMd5(config.getKey()+streamId+txTime);

        //视频推送url
        String pushUrl = "rtmp://"+config.getBizId()+".livepush.myqcloud.com/live/"+streamId+"?bizid="+config.getBizId()+"&txSecret="+txSecret+"&txTime="+txTime;

        //视频播放url rtmp
        String playUrlRtmp = "rtmp://"+config.getPlayUrl()+streamId;

        //视频播放url flv
        String playUrlFlv = "http://"+config.getPlayUrl()+streamId+".flv";

        //视频播放url hls
        String playUrlHls = "http://"+config.getPlayUrl()+streamId+".m3u8";

        urls.setPushUrl(pushUrl);
        urls.setPlayUrlRtmp(playUrlRtmp);
        urls.setPlayUrlFlv(playUrlFlv);
        urls.setPlayUrlHls(playUrlHls);

        return urls;
    }



    public static String setLong(int num){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(new Date());
            //日期转string
        Date sDate = null;
        try {
            sDate = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(sDate);
         //增加7天
        cal.add(Calendar.DAY_OF_MONTH, num);
        //Calendar转为Date类型
        Date date=cal.getTime();
        //将增加后的日期存入另一字段

        return   format.format(date);

    }


    /**
     * 获取unix时间戳
     * @return
     * @throws Exception
     */
    public static Long getUnixTime (String dateStr) {

        try {

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            long epoch = df.parse(dateStr).getTime();

            return epoch/1000;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0L;
    }

    /**
     * 获取md5字符串
     * @param str
     * @return
     */
    public static String getMd5(String str) {

        MessageDigest md5 = null;
        try {

            md5 = MessageDigest.getInstance("MD5");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] bs = md5.digest(str.getBytes());
        StringBuilder sb = new StringBuilder(40);
        for(byte x:bs) {
            if((x & 0xff)>>4 == 0) {
                sb.append("0").append(Integer.toHexString(x & 0xff));
            } else {
                sb.append(Integer.toHexString(x & 0xff));
            }
        }
        return sb.toString();
    }






}
