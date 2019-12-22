package com.example.projeto_final;

import android.app.Application;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class NotificationChannel extends Application {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannels() {
        android.app.NotificationChannel channel1 = new android.app.NotificationChannel("1", "Channel 1", NotificationManager.IMPORTANCE_HIGH);
        channel1.setDescription("PEDIDOS");

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel1);

    }

}
