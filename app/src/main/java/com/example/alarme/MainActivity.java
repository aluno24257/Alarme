package com.example.alarme;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "alarm_channel";
    private static final int NOTIFICATION_ID = 1;

    private AlarmManager alarmManager;
    private TimePicker timePicker;
    private Button setButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        timePicker = findViewById(R.id.timePicker);
        setButton = findViewById(R.id.setButton);
        cancelButton = findViewById(R.id.cancelButton);

        createNotificationChannel();

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });
    }

    private void setAlarm() {
        int hour, minute;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        long alarmTime = calendar.getTimeInMillis();

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);

        String alarmTimeString = String.format("%02d:%02d", hour, minute);
        Toast.makeText(this, "Alarme definido para " + alarmTimeString, Toast.LENGTH_SHORT).show();

        showNotification(hour, minute);
    }

    private void cancelAlarm() {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(pendingIntent);

        Toast.makeText(this, "Alarme cancelado", Toast.LENGTH_SHORT).show();

        hideNotification();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(int hour, int minute) {
        String alarmTime = String.format("%02d:%02d", hour, minute);

        Intent mainIntent = new Intent(this, MainActivity.class);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent cancelIntent = new Intent(this, NotificationBroadcastReceiver.class);
        cancelIntent.setAction(NotificationBroadcastReceiver.ACTION_CANCEL_ALARM);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(this, 0, cancelIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle("Alarme definido")
                .setContentText("Alarme definido para " + alarmTime)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(mainPendingIntent)
                .setAutoCancel(false)
                .addAction(R.drawable.ic_cancel, "Cancelar", cancelPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void hideNotification() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
