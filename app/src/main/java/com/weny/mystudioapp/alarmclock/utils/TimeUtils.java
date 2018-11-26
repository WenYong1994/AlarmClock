package com.weny.mystudioapp.alarmclock.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static Date string2Date(String time){
        try {
            SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.parse(time);
        }catch (Exception e){

        }
        return new Date(0);
    }

    public static String long2String(long time){
        try {
            SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.format(new Date(time));
        }catch (Exception e){

        }
        return "";
    }

}
