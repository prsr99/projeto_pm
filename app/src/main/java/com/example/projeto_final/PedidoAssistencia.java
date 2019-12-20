package com.example.projeto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PedidoAssistencia extends AppCompatActivity {

    EditText assunto;
    EditText mensagem;
    EditText localizacao;
    Button btn;
    int id_cliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_assistencia);

        SharedPreferences result = getSharedPreferences("myApp", Context.MODE_PRIVATE);
         id_cliente = result.getInt("ID_CLIENTE", -1);

        assunto = (EditText) findViewById(R.id.assunto);
        mensagem = (EditText) findViewById(R.id.mensagem);
        localizacao = (EditText) findViewById(R.id.localizacao);
        btn = (Button) findViewById(R.id.button_pedido);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!assunto.getText().toString().isEmpty() && !mensagem.getText().toString().isEmpty() && !localizacao.getText().toString().isEmpty()) {
                    efetuar_pedido();

                    /*Intent i = new Intent(PerfilCliente.this, MenuUtilizador.class);
                    startActivity(i);
                    finish();

                     */

                } else {
                    Toast.makeText(PedidoAssistencia.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void efetuar_pedido() {
        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/pedidos/cria_pedido";

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("id_cliente", String.valueOf(id_cliente));
        jsonParams.put("assunto", assunto.getText().toString());
        jsonParams.put("mensagem", mensagem.getText().toString());
        jsonParams.put("localizacao", localizacao.getText().toString());

        //Log.d("TESTE", "" + ano.getText().toString() + " " + matricula.getText().toString());

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                Toast.makeText(PedidoAssistencia.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(PedidoAssistencia.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex) {
                            Toast.makeText(PedidoAssistencia.this, "" + ex, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PedidoAssistencia.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset-utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(postRequest);
     }

}
