package com.example.projeto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.projeto_final.adapters.CustomArrayAdapter;
import com.example.projeto_final.entities.Pedido;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PedidosEfetuados extends AppCompatActivity {

    ListView lista;
    ArrayList<Pedido> Pedido = new ArrayList<>();
    int id_cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_efetuados);

        SharedPreferences result = getSharedPreferences("myApp", Context.MODE_PRIVATE);
        id_cliente = result.getInt("ID_CLIENTE", -1);

        lista = (ListView) findViewById(R.id.lista);
        fillLista(id_cliente);


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(PedidosEfetuados.this, DetalhesPedido.class);
                i.putExtra("assunto", Pedido.get(position).assunto);
                i.putExtra("mensagem", Pedido.get(position).mensagem);
                i.putExtra("localizacao", Pedido.get(position).localizacao);
                i.putExtra("nome_mecanico", Pedido.get(position).nome_mecanico);
                i.putExtra("apelido_mecanico", Pedido.get(position).apelido_mecanico);
                startActivity(i);
            }
        });


    }

    private void fillLista(int id) {

        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/pedidos/getpedidoscliente/" + id;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("DATA");

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);

                        Pedido.add(new Pedido(obj.getInt("id"),obj.getString("assunto"), obj.getString("mensagem"), obj.getString("localizacao"),
                                obj.getInt("if_aceite"), obj.getInt("if_terminado"), obj.getString("nome_mecanico"), obj.getString("apelido_mecanico"), obj.getInt("id_veiculo"),
                                obj.getString("nome_cliente"), obj.getString("apelido_cliente"), obj.getInt("telemovel") ));
                        CustomArrayAdapter itemsAdapter =
                                new CustomArrayAdapter(PedidosEfetuados.this, Pedido);
                        ((ListView) findViewById(R.id.lista)).setAdapter(itemsAdapter);
                    }
                } catch (JSONException ex) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PedidosEfetuados.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

    }
}
