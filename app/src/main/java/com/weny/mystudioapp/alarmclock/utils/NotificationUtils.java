package com.weny.mystudioapp.alarmclock.utils;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.weny.mystudioapp.alarmclock.R;
import com.weny.mystudioapp.alarmclock.activity.MainActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NotificationUtils {
    private static final String PUSH_CHANNEL_ID = "PUSH_NOTIFY_ID";
    private static final String PUSH_CHANNEL_NAME = "PUSH_NOTIFY_NAME";


    public static void notification(Context context,int count){
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }



        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,PUSH_CHANNEL_ID);

        builder.setTicker("第"+count+"次提醒");            // 通知弹出时状态栏的提示文本

        builder.setContentInfo("");//

        builder.setContentTitle("第"+count+"次提醒")    // 通知标题

                .setContentText(TimeUtils.long2String(System.currentTimeMillis()))  // 通知内容

                .setSmallIcon(R.mipmap.ic_launcher);    // 通知小图标

        builder.setDefaults(Notification.DEFAULT_SOUND);    // 设置声音/震动等

        Intent intent = new Intent(context,MainActivity.class);

        //getIntent().getStringExtra("msg");

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0,   //请求码
                intent, //意图对象

                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        builder.setFullScreenIntent(pendingIntent,true);

        // 设置通知的点击行为：自动取消/跳转等
        builder.setAutoCancel(true);

        Notification notification = builder.build();

        // 通过NotificationManager发送通知
        mNotificationManager.notify(1, notification);

    }






    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    @SuppressLint("NewApi")
    public static boolean isNotificationEnabled(Context context) {

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }


}
