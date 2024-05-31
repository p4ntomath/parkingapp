package com.example.parkingapp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class removeBooking extends BroadcastReceiver {
    private static final String CHANNEL_ID = "RemovedBooking";
    private static final String CHANNEL_NAME = "RemoveBooking";
    private static final int NOTIFICATION_ID = 2;
    @Override
    public void onReceive(Context context, Intent intent) {
        bookingManager bookingManager = new bookingManager(context);
        BookingSession bookingSession = new BookingSession(context);
        String exitTime = bookingSession.getLeavingTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        Calendar currentNow = Calendar.getInstance();
        int hour = currentNow.get(Calendar.HOUR_OF_DAY);
        int minute = currentNow.get(Calendar.MINUTE);
        String currentTime = String.format("%02d:%02d", hour, minute);
        LocalTime time = LocalTime.parse(exitTime, formatter);
        LocalTime currentTimeObj = LocalTime.parse(currentTime, formatter);
        if(currentTimeObj.isAfter(time)){
            bookingManager.deleteFromDatabase().thenAccept(success -> {
                if (success) {
                    showNotification(context);
                }
            });
        }else if(!currentTimeObj.isBefore(time)){
            bookingManager.deleteFromDatabase().thenAccept(success -> {
                if (success) {
                    showNotification(context);
                }
            });
        }

    }
    public static void showNotification(Context context) {
        String contentTitle = "Booking";
        String contentText = "Your Booking Has Been Removed";

        // Create intent to launch the app when notification is clicked
        Intent intent = new Intent(context, reserve_fragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Create Notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(contentTitle)
                        .setContentText(contentText)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)  // Set the intent to open when notification is clicked
                        .setAutoCancel(true); // Dismiss notification on click


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
            }
        }

        // Show notification
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }


}
class NotificationRemove {

    @SuppressLint("ScheduleExactAlarm")
    public static void scheduleNotification(Context context, long delay) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, removeBooking.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT); // Use FLAG_UPDATE_CURRENT
        long triggerTime = System.currentTimeMillis() + delay; // Adjust trigger time based on delay
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
    }
}