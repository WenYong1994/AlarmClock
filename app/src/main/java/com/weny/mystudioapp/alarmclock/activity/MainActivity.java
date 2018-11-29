package com.weny.mystudioapp.alarmclock.activity;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.weny.mystudioapp.alarmclock.R;
import com.weny.mystudioapp.alarmclock.control.AlarmControl;
import com.weny.mystudioapp.alarmclock.control.SoundContorl;
import com.weny.mystudioapp.alarmclock.model.DateHepler;
import com.weny.mystudioapp.alarmclock.service.AlarmService;
import com.weny.mystudioapp.alarmclock.service.MusicService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_IGNORE_BATTERY_CODE = 100;
    TextView alarmTime;

    Button chioceTime;

    Button turnoffAlarm;
    JobScheduler mJobScheduler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initValueView();
        initListenr();
        checkPower();
        isIgnoreBatteryOption(this);
    }

    private void checkPower() {





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(getPackageName(), AlarmService.class.getName()));

            builder.setPeriodic(60 * 1000); //每隔60秒运行一次
            builder.setRequiresCharging(true);
            builder.setPersisted(true);  //设置设备重启后，是否重新执行任务
            builder.setRequiresDeviceIdle(true);

//            if (mJobScheduler.schedule(builder.build()) <= 0) {
//                //If something goes wrong
//            }
        }


        Intent intent =new Intent(this,AlarmService.class);
        startService(intent);

    }

    /**
     * 针对N以上的Doze模式
     *
     * @param activity
     */
    public static void isIgnoreBatteryOption(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Intent intent = new Intent();
                String packageName = activity.getPackageName();
                PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
//               intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    activity.startActivityForResult(intent, REQUEST_IGNORE_BATTERY_CODE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initValueView() {
        alarmTime.setText("提示时间:"+AlarmControl.getIntence().getAlarmTime());
    }

    private void initView() {
        alarmTime = findViewById(R.id.alarm_time);
        chioceTime = findViewById(R.id.chioce_time);
        turnoffAlarm = findViewById(R.id.turnoff_alarm);
    }

    private void initListenr() {
        turnoffAlarm.setOnClickListener(this);
        chioceTime.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.chioce_time:
                TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener(){
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        long time = date.getTime();
                        if(time<=System.currentTimeMillis()){//预计归还时间比预计借出时间早
                            Toast.makeText(MainActivity.this, "提示时间不能比现在低", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String timeStr = simpleDateFormat.format(date.getTime());
                        alarmTime.setText("提示时间:"+timeStr);
                        AlarmControl.getIntence().setAlarmTime(MainActivity.this,timeStr);
                    }
                }).build();
                pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                pvTime.show();
                break;
            case R.id.turnoff_alarm:
                AlarmControl.getIntence().turnoffAlarm(MainActivity.this);
                alarmTime.setText("提示时间:暂时无闹钟");
                break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        AlarmControl.getIntence().neeoNotification = true;//记录是否需要提示通知
    }


    @Override
    protected void onStart() {
        super.onStart();
        AlarmControl.getIntence().neeoNotification = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AlarmControl.relase();
        Intent intent =new Intent(this,MusicService.class);
        stopService(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IGNORE_BATTERY_CODE){
                //TODO something
            }
        }else if (resultCode == RESULT_CANCELED){

        }
    }

}
