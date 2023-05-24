package com.example.alarme;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

public class AlarmWidget extends AppWidgetProvider {
    private static final String ACTION_CANCEL_ALARM = "com.example.alarme.ACTION_CANCEL_ALARM";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            Intent cancelIntent = new Intent(context, AlarmWidget.class);
            cancelIntent.setAction(ACTION_CANCEL_ALARM);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, cancelIntent, 0);
            views.setOnClickPendingIntent(R.id.widgetCancelButton, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (ACTION_CANCEL_ALARM.equals(intent.getAction())) {
            cancelAlarm(context);
        }
    }

    private void cancelAlarm(Context context) {
        // Coloque aqui a lógica para cancelar o alarme
        // por exemplo, usando AlarmManager.cancel() ou similar

        Toast.makeText(context, "Alarme cancelado pelo widget", Toast.LENGTH_SHORT).show();
    }

    public static void updateWidgets(Context context) {
        Intent intent = new Intent(context, AlarmWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int[] widgetIds = widgetManager.getAppWidgetIds(new ComponentName(context, AlarmWidget.class));

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
        context.sendBroadcast(intent);
    }

}
