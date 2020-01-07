package com.example.projeto_final;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.projeto_final.adapters.CustomArrayAdapter;
import com.example.projeto_final.adapters.CustomArrayAdapterMecanico;
import com.example.projeto_final.entities.Pedido;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PedidosMecanico extends AppCompatActivity {

    ListView lista;
    Session session;
    ArrayList<com.example.projeto_final.entities.Pedido> Pedido = new ArrayList<>();
    int id_mecanico;
    Handler handler = new Handler();
    int delay = 5000; //milliseconds
    int npedidos = -1;
    private NotificationManagerCompat notificationManager;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_mecanico);
        notificationManager = NotificationManagerCompat.from(this);
        session = new Session(this);

        SharedPreferences result = getSharedPreferences("myApp", Context.MODE_PRIVATE);
        id_mecanico = result.getInt("ID_MECANICO", -1);

        lista = (ListView) findViewById(R.id.lista_mecanico);
        fillLista_mecanicos();

        if(!session.loggedInMecanico()) {
            Intent intent = new Intent(PedidosMecanico.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(PedidosMecanico.this, DetalhesPedidoMecanico.class);
                i.putExtra("id", Pedido.get(position).id);
                i.putExtra("assunto", Pedido.get(position).assunto);
                i.putExtra("mensagem", Pedido.get(position).mensagem);
                i.putExtra("localizacao", Pedido.get(position).localizacao);
                i.putExtra("nome", Pedido.get(position).nome_cliente);
                i.putExtra("apelido", Pedido.get(position).apelido_cliente);
                i.putExtra("telemovel", Pedido.get(position).telemovel_cliente);
                i.putExtra("id_veiculo", Pedido.get(position).id_veiculo);
                startActivity(i);
            }
        });

        //scheduleJob();
         handler.postDelayed(new Runnable(){
            public void run(){
                //do something
                fillLista_mecanicos();
                handler.postDelayed(this, delay);
            }
        }, delay);
        startService(new Intent(this, BackgroundService.class));

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
                                    npedidos = response.getInt("DATA");
                                    sendOnChannel1();
                                }
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


    private void fillLista_mecanicos() {
        Pedido.removeAll(Pedido);
        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/pedidos/getpedidosnaoaceites";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("DATA");

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);

                        Pedido.add(new Pedido(obj.getInt("id"),obj.getString("assunto"), obj.getString("mensagem"), obj.getString("localizacao"),
                                obj.getInt("if_aceite"), obj.getInt("if_terminado"), obj.getString("nome_mecanico"), obj.getString("apelido_mecanico"), obj.getInt("id_veiculo"),
                                obj.getString("nome_cliente"), obj.getString("apelido_cliente"), obj.getInt("telemovel")));
                        CustomArrayAdapterMecanico itemsAdapter =
                                new CustomArrayAdapterMecanico(PedidosMecanico.this, Pedido);
                        ((ListView) findViewById(R.id.lista_mecanico)).setAdapter(itemsAdapter);
                        itemsAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException ex) {
                    Log.d("EXCEPTION: ", "" + ex);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PedidosMecanico.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                session.setLoggedInMecanico(false, id_mecanico);
                Intent intent = new Intent(PedidosMecanico.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
