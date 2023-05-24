package com.example.alarme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Vibrar o dispositivo
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            // Verificar se a vibração é suportada
            if (vibrator.hasVibrator()) {
                // Definir padrão de vibração
                long[] pattern = {0, 1000, 1000}; // Padrão de vibração: [pausa, vibração, pausa, vibração, ...]
                int repeat = -1; // -1 para repetir continuamente, 0 para não repetir
                vibrator.vibrate(pattern, repeat);
            }
        }

        Toast.makeText(context, "Alarme tocando!", Toast.LENGTH_SHORT).show();
    }
}
