package com.example.personal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ManageAlarm {
    int hour,min;
    long miliTime;

    public ManageAlarm(){

    }
    public ManageAlarm(int hour,int min){
        this.hour=hour;
        this.min=min;
    }
    public void pptTime(int requestCode, long milisec, Context context){
        Intent intent=new Intent(context,AlertReceiver.class);
        intent.putExtra("id",String.valueOf(requestCode));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,requestCode,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,milisec,pendingIntent);

    }

}
