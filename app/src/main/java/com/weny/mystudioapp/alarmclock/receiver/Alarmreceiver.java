package com.weny.mystudioapp.alarmclock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.weny.mystudioapp.alarmclock.control.AlarmControl;

public class Alarmreceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //开始长达一分钟的响铃
        AlarmControl.getIntence().onReciver(context,intent);

    }

}
