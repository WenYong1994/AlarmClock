package com.weny.mystudioapp.alarmclock.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.weny.mystudioapp.alarmclock.app.App;

public class DateHepler {
    public static DateHepler instence;

    private int alarmCount;//提醒次数
    private String alarmTime;//提醒时间格式 2018-11-11 01:01:01


    public static String ALARM_COUNT = "alarmCount";

    public static String ALARM_TIME = "alarmTime";

    public static String SP_NAME = "com.weny.mystudioapp";

    private DateHepler(){
        //初始化所有的值，存了文件主要是因为有可能程序在后台是会被清楚数据，但是数据小没必要使用数据库
        getIntValueOnFile(ALARM_COUNT);
        getStringValueOnFile(ALARM_TIME);
    }

    public static DateHepler getInstence(){
        if(instence == null ){
            synchronized (DateHepler.class){
                if(instence==null){
                    instence= new DateHepler();
                }

            }
        }
        return instence;
    }


    private int getIntValueOnFile(String key) {
        SharedPreferences sharedPreferences = App.getApp().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    private String getStringValueOnFile(String key) {
        SharedPreferences sharedPreferences = App.getApp().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    private void setIntValueOnFile(String key,int value) {
        SharedPreferences sharedPreferences = App.getApp().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(key, value);
        edit.apply();
    }


    private void setStringValueOnFile(String key,String value) {
        SharedPreferences sharedPreferences = App.getApp().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.apply();
    }



    public int getAlarmCount() {
        if(alarmCount==0){
            getIntValueOnFile(ALARM_COUNT);
        }
        return alarmCount;
    }

    public void setAlarmCount(int alarmCount) {
        this.alarmCount = alarmCount;
        setIntValueOnFile(ALARM_COUNT,alarmCount);
    }


    public String getAlarmTime(){
        if(alarmTime==null){
            alarmTime = getStringValueOnFile(ALARM_TIME);
        }
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
        setStringValueOnFile(ALARM_TIME,alarmTime);
    }
}
