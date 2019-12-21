package com.example.projeto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.projeto_final.adapters.CustomArrayAdapterMecanico;
import com.example.projeto_final.entities.Pedido;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetalhesPedidoMecanico extends AppCompatActivity {
    TextView assunto ;
    TextView mensagem;
    TextView localizacao;
    TextView nome;
    TextView apelido;
    TextView telemovel ;
    TextView marca;
    TextView modelo;
    TextView ano;
    TextView matricula;
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pedido_mecanico);

        btn = (Button) findViewById(R.id.button_aceitar);
        assunto = (TextView) findViewById(R.id.assunto_pedido);
        mensagem = (TextView) findViewById(R.id.mensagem_pedido);
        localizacao = (TextView) findViewById(R.id.localizacao_pedido);

        nome = (TextView) findViewById(R.id.nome_cliente);
        telemovel = (TextView) findViewById(R.id.telemovel_cliente);

        marca = (TextView) findViewById(R.id.marca_veiculo);
        modelo = (TextView) findViewById(R.id.modelo_veiculo);
        ano = (TextView) findViewById(R.id.ano_veiculo);
        matricula = (TextView) findViewById(R.id.matricula_veiculo);

        SharedPreferences result = getSharedPreferences("myApp", Context.MODE_PRIVATE);
        final int id_mecanico = result.getInt("ID_MECANICO", -1);


        assunto.setText(" Assunto: " + getIntent().getStringExtra("assunto"));
        mensagem.setText(" Mensagem: " + getIntent().getStringExtra("mensagem"));
        localizacao.setText(" Localização: " + getIntent().getStringExtra("localizacao"));

        nome.setText(" Nome: " + getIntent().getStringExtra("nome") + " " + getIntent().getStringExtra("apelido"));
        telemovel.setText(" Telemovel: " + getIntent().getIntExtra("telemovel",-1));

        getDetalhesVeiculo(getIntent().getIntExtra("id_veiculo",-1));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aceita_pedido(getIntent().getIntExtra("id", -1), id_mecanico);

            }
        });


    }

    public void getDetalhesVeiculo (int id) {
        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/veiculo/" + id;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if(status) {
                                JSONArray arr = response.getJSONArray("DATA");

                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    marca.setText(" Marca: " + obj.getString("marca"));
                                    modelo.setText(" Modelo: " + obj.getString("modelo"));
                                    ano.setText(" Ano: " + obj.getString("ano"));
                                    matricula.setText(" Matricula: " + obj.getString("matricula"));

                                }

                            }

                            else {

                                Toast.makeText(DetalhesPedidoMecanico.this, "" + status, Toast.LENGTH_SHORT).show();

                            }
                        }
                        catch (JSONException ex) {
                            //Log.d("login", "" + ex);
                        }

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetalhesPedidoMecanico.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    public void aceita_pedido(int id_pedido, final int id_mecanico) {
        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/pedidos/edita_pedido/" + id_pedido;

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("if_aceite", String.valueOf(1));
        jsonParams.put("mecanicos_id", String.valueOf(id_mecanico));
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                // Toast.makeText(PerfilCliente.this,response.getString("MSG") , Toast.LENGTH_SHORT).show();

                                set_mecanico_ocupado(id_mecanico);

                            } else {
                                // Toast.makeText(PerfilCliente.this,response.getString("MSG"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex) {
                            Log.d("PRE: ","" + ex);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetalhesPedidoMecanico.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(postRequest);


    }


    public void set_mecanico_ocupado(int id_mecanico) {
        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/pedidos/edita_mecanico/" + id_mecanico;

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("if_disponivel", String.valueOf(0));
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                // Toast.makeText(PerfilCliente.this,response.getString("MSG") , Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(DetalhesPedidoMecanico.this, TerminarPedido.class);
                                i.putExtra("id", getIntent().getIntExtra("id", -1));
                                i.putExtra("assunto", getIntent().getStringExtra("assunto"));
                                i.putExtra("mensagem",getIntent().getStringExtra("mensagem"));
                                i.putExtra("localizacao", getIntent().getStringExtra("mensagem"));
                                i.putExtra("nome", getIntent().getStringExtra("nome"));
                                i.putExtra("apelido", getIntent().getStringExtra("apelido"));
                                i.putExtra("telemovel", getIntent().getIntExtra("telemovel", -1));
                                i.putExtra("id_veiculo", getIntent().getIntExtra("id_veiculo", -1));
                                startActivity(i);
                                finish();



                            } else {
                                // Toast.makeText(PerfilCliente.this,response.getString("MSG"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex) {
                            Log.d("PRE: ","" + ex);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetalhesPedidoMecanico.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(postRequest);


    }




}
