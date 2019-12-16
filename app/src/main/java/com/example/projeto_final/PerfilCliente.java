package com.example.projeto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PerfilCliente extends AppCompatActivity {
    Session session;
    String id_cliente;
    String id_user;
    int id_int_cliente = -1;
    int id_int_user = -1;
    EditText nome;
    EditText telemovel;
    EditText ano;
    EditText matricula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cliente);
        session = new Session(this);

        nome = (EditText) findViewById(R.id.nome);
        telemovel = (EditText) findViewById(R.id.telemovel);
        ano = (EditText) findViewById(R.id.ano);
        matricula = (EditText) findViewById(R.id.matricula);
        Button btn1 = (Button)findViewById(R.id.button1);

        if(!session.loggedIn()) {
            Intent intent = new Intent(PerfilCliente.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        SharedPreferences result = getSharedPreferences("myApp", Context.MODE_PRIVATE);
        final int id_cliente = result.getInt("ID_CLIENTE", -1);
        final int id_user = result.getInt("ID_USER", -1);
        getIDCliente(id_user);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (!nome.getText().toString().isEmpty() && !telemovel.getText().toString().isEmpty() && !ano.getText().toString().isEmpty() && !matricula.getText().toString().isEmpty()) {
                    preenche_perfil(id_cliente);
                    Toast.makeText(PerfilCliente.this, "" + id_cliente, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(PerfilCliente.this, MenuUtilizador.class);
                    startActivity(i);
                    finish();
                //}
            }
        });




    }

    public void getIDCliente (int id_user) {
        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/cliente/" + id_user;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if(status) {
                                JSONObject obj = response.getJSONObject(("DATA"));
                                id_cliente = obj.optString("id");
                                id_int_cliente = Integer.parseInt(id_cliente);
                                if_perfil_completo(id_int_cliente);

                            }
                            else {
                                id_cliente = null;
                            }
                        }
                        catch (JSONException ex) {

                        }

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PerfilCliente.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        //Toast.makeText(LoginActivity.this, "" + id, Toast.LENGTH_SHORT).show();
    }

    public void if_perfil_completo(int id) {
            String url = "https://inactive-mosses.000webhostapp.com/myslim/api/cliente/perfil_completo/" + id;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                boolean status = response.getBoolean("status");
                                if (status) {
                                    Toast.makeText(PerfilCliente.this,response.getString("MSG") , Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(PerfilCliente.this, MenuUtilizador.class);
                                    startActivity(i);
                                    finish();

                                }
                                else {
                                    Toast.makeText(PerfilCliente.this,response.getString("MSG") , Toast.LENGTH_SHORT).show();

                                }

                            } catch (JSONException ex) {
                                Toast.makeText(PerfilCliente.this, "" + ex, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(PerfilCliente.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            // Access the RequestQueue through your singleton class.
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    public void preenche_perfil(int id) {
        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/cliente/editar/" + id;
        /*
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("nome", data.getStringExtra(Utils.PARAM_NOME));
        jsonParams.put("apelido", data.getStringExtra(Utils.PARAM_APELIDO));
        jsonParams.put("numero", String.valueOf(data.getIntExtra(Utils.PARAM_NUMERO, -1)));
        jsonParams.put("email", data.getStringExtra(Utils.PARAM_EMAIL));
        jsonParams.put("idade", String.valueOf(data.getIntExtra(Utils.PARAM_IDADE, -1)));
        jsonParams.put("cidade_id",data.getStringExtra(Utils.PARAM_CIDADE));

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                Toast.makeText(PerfilCliente.this,response.getString("MSG") , Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PerfilCliente.this,response.getString("MSG"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

         */
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
                session.setLoggedIn(false, id_int_user,id_int_cliente);
                Intent intent = new Intent(PerfilCliente.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
