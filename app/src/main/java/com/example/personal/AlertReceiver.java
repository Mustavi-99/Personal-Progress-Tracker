package com.example.personal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;


public class AlertReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "Sample Channel";
    @Override
    public void onReceive(Context context, Intent intent) {
        int notiID=intent.getIntExtra("notiId",0);
        ManageAlarm manageAlarm = new ManageAlarm();
        DatabaseHelper db = new DatabaseHelper(context);

        Cursor cursor;
        String ID = intent.getStringExtra("id");
        cursor = db.getCurrent(ID);
        int catIndex= cursor.getColumnIndex( "Category");
        int subIndex= cursor.getColumnIndex("Subject");
        String sub= cursor.getString(subIndex);
        String cat= cursor.getString(catIndex);
        Intent alertintent = new Intent(context,InputActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0,alertintent,0);

        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence channel_name = "My Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel= new NotificationChannel(CHANNEL_ID,channel_name,importance);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_transparent)
                .setContentTitle(cat)
                .setContentText(sub)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        notificationManager.notify(notiID,builder.build());
        Vibrator v=(Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        VibrationEffect vibrationEffect = (VibrationEffect.createOneShot(100000,10));
        v.vibrate(vibrationEffect);
        MediaPlayer mediaPlayer=MediaPlayer.create(context,R.raw.ringtone);
        mediaPlayer.start();
    }
}