package com.example.alarme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_CANCEL_ALARM = "com.example.alarme.ACTION_CANCEL_ALARM";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_CANCEL_ALARM.equals(intent.getAction())) {
            // Alarme cancelado pelo usuário
            Toast.makeText(context, "Alarme cancelado pelo usuário", Toast.LENGTH_SHORT).show();
        }
    }
}
