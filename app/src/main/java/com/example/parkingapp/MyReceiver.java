package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;

public class MyReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "exitRemainder";
    private static final String CHANNEL_NAME = "Bookings";
    private static final int NOTIFICATION_ID = 1;

    @SuppressLint("NotificationTrampoline")
    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }

    public static void showNotification(Context context) {
        String contentTitle = "Exit Time Approaching";
        String contentText = "Your exit time is approaching!";

        // Create Notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(contentTitle)
                        .setContentText(contentText)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true); // Dismiss notification on click

        // Show the notification
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if Android version is Oreo or higher and create notification channel if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            } else {
            }
        }

        // Show notification
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        } else {
        }
    }
}

class NotificationScheduler {

    @SuppressLint("ScheduleExactAlarm")
    public static void scheduleNotification(Context context, long delay) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT); // Use FLAG_UPDATE_CURRENT
        long triggerTime = System.currentTimeMillis() + delay; // Adjust trigger time based on delay
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        Log.d("TAG", "Notification scheduled");
    }
}