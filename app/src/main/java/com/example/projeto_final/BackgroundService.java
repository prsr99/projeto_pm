package com.example.projeto_final;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class BackgroundService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    int npedidos = -1;
    private NotificationManagerCompat notificationManager;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();
        notificationManager = NotificationManagerCompat.from(this);
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                notificaPedido();
                Log.d("SERVICO", "SERVIÇO A CORRER");
                //Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();
                handler.postDelayed(runnable, 5000);
            }
        };

        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        handler.removeCallbacks(runnable);
        //Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        //Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
    }

    public void notificaPedido () {
        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/pedidos/nregistos";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if(status) {
                                // Toast.makeText(LoginActivity.this, ""+ status, Toast.LENGTH_SHORT).show();
                                //id = response.optString("id");
                                if (npedidos != response.getInt("DATA")) {
                                    sendOnChannel1();
                                }
                                npedidos = response.getInt("DATA");
                            }

                            else {

                            }
                        }
                        catch (JSONException ex) {
                            Log.d("login", "" + ex);
                        }

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        //Toast.makeText(LoginActivity.this, "" + id, Toast.LENGTH_SHORT).show();
    }

    public void sendOnChannel1() {
        Notification notification = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("SERVIÇOS")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentText("NOVO SERVIÇO DISPONIVEL")
                .build();
        notificationManager.notify(1, notification);

    }


}
