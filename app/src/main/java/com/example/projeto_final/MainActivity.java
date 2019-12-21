package com.example.projeto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Context mContext = this;
    String id;
    int id_int_user = -1;
    int id_int_cliente = -1;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText et1 = (EditText)findViewById(R.id.user);
        final EditText et2 = (EditText)findViewById(R.id.password);
        Button btn1 = (Button)findViewById(R.id.button1);
        Button btn2 = (Button)findViewById(R.id.button2);
        session = new Session(this);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, registar.class);
                startActivity(intent);
            }
        });


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et1.getText().toString();
                String password = et2.getText().toString();
//                //String[] cValues = {username, pwc};
                //Cursor cursor = db.query(Contrato.User.TABLE_NAME, columns, "USERNAME=? AND PASSWORD=?", cValues, null, null, null);


                if (email.isEmpty() || password.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setCancelable(true);

                    if(email.isEmpty()) {
                        builder.setMessage(R.string.preencheloginuser);
                    }

                    if(password.isEmpty()) {
                        builder.setMessage(R.string.preencheloginpass);
                    }

                    if(email.isEmpty() && password.isEmpty()) {
                        builder.setMessage(R.string.alertapreenche);
                    }

                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });


                    AlertDialog dialog = builder.create();
                    dialog.show();

                }

                else {
                    // String teste = Login(username, password);
                    // Toast.makeText(LoginActivity.this, teste, Toast.LENGTH_SHORT).show();
                    //Login(username, password);
                    Login(email, password);
                    //managePrefs();
                }
            }
        });



        if(session.loggedInCliente()) {
            Intent i = new Intent(MainActivity.this, MenuUtilizador.class);
            startActivity(i);
            finish();
        }

        if(session.loggedInMecanico()) {
            Intent i = new Intent(MainActivity.this, PedidosMecanico.class);
            startActivity(i);
            finish();
        }
    }

    public void Login(final String email, String password) {

        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/users/login";
        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("email", email);
        jsonParams.put("password", password);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,

                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            boolean status = response.getBoolean("status");

                            if (status) {
                                getIDUser(email);
                                //Toast.makeText(LoginActivity.this, "" + status, Toast.LENGTH_LONG).show();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setCancelable(true);
                                builder.setMessage(R.string.loginerro);
                                builder.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });


                                AlertDialog dialog = builder.create();
                                dialog.show();
                                // Toast.makeText(LoginActivity.this, "" + status, Toast.LENGTH_SHORT).show();
                                id = null;

                            }


                        } catch (JSONException ex) {
                            Log.d("login", "" + ex);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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


    public void if_perfil_completo(final int id) {
        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/cliente/perfil_completo/" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if (status) {

                                //Toast.makeText(MainActivity.this,response.getString("MSG") , Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MainActivity.this, MenuUtilizador.class);
                                startActivity(i);
                                finish();

                            }
                            else {
                                 //Toast.makeText(MainActivity.this,response.getString("MSG") , Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException ex) {
                            Toast.makeText(MainActivity.this, "" + ex, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    public void getIDUser (String email) {
        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/users/" + email;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if(status) {
                                // Toast.makeText(LoginActivity.this, ""+ status, Toast.LENGTH_SHORT).show();
                                //id = response.optString("id");

                                JSONObject obj = response.getJSONObject(("DATA"));
                                id = obj.optString("id");
                                //Toast.makeText(LoginActivity.this, "" + id, Toast.LENGTH_SHORT).show();
                                id_int_user = Integer.parseInt(id);
                                //session.setLoggedIn(true, id_int);
                                ifCliente(id_int_user);

                            }

                            else {
                              /* AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                               builder.setCancelable(true);
                               builder.setMessage("User não existe");
                               builder.setPositiveButton("OK",
                                       new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {
                                           }
                                       });


                               AlertDialog dialog = builder.create();
                               dialog.show();*/




                                Toast.makeText(MainActivity.this, "" + status, Toast.LENGTH_SHORT).show();
                                id = null;
                            }
                        }
                        catch (JSONException ex) {
                            Log.d("login", "" + ex);
                        }

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        //Toast.makeText(LoginActivity.this, "" + id, Toast.LENGTH_SHORT).show();
    }

    public void getIDMecanico (int id) {
        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/mecanicos/" + id;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if(status) {
                                // Toast.makeText(LoginActivity.this, ""+ status, Toast.LENGTH_SHORT).show();
                                //id = response.optString("id");

                                JSONObject obj = response.getJSONObject(("DATA"));

                                int id_mecanico  = obj.optInt("id");
                                //Toast.makeText(LoginActivity.this, "" + id, Toast.LENGTH_SHORT).show();
                                session.setLoggedInMecanico(true, id_mecanico);
                                Intent i = new Intent(MainActivity.this, PedidosMecanico.class);
                                startActivity(i);
                                finish();

                            }

                            else {
                              /* AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                               builder.setCancelable(true);
                               builder.setMessage("User não existe");
                               builder.setPositiveButton("OK",
                                       new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {
                                           }
                                       });


                               AlertDialog dialog = builder.create();
                               dialog.show();*/




                                Toast.makeText(MainActivity.this, "" + status, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException ex) {
                            Log.d("login", "" + ex);
                        }

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        //Toast.makeText(LoginActivity.this, "" + id, Toast.LENGTH_SHORT).show();
    }


public void criaCliente (int id) {

    String url = "https://inactive-mosses.000webhostapp.com/myslim/api/clientes";

    Map<String, String> jsonParams = new HashMap<String, String>();
    jsonParams.put("id_user", String.valueOf(id));
    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
            new JSONObject(jsonParams),
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getBoolean("status")) {
                            Toast.makeText(MainActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException ex) {
                        Toast.makeText(MainActivity.this, "" + ex, Toast.LENGTH_SHORT).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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


public void existeCliente (final int id) {
    String url = "https://inactive-mosses.000webhostapp.com/myslim/api/cliente/" + id;

    JsonObjectRequest jsObjRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        boolean status = response.getBoolean("status");
                        if(status) {
                            JSONObject obj = response.getJSONObject("DATA");
                            int idcliente = obj.getInt("id");
                            session.setLoggedInCliente(true, id_int_user);
                            session.setClienteID(idcliente);
                           // Intent i = new Intent(MainActivity.this, PerfilCliente.class);
                           // startActivity(i);
                            //finish();
                            if_perfil_completo(id);

                        }

                        else {

                            criaCliente(id);
                            session.setLoggedInCliente(true, id_int_user);

                            Intent i = new Intent(MainActivity.this, PerfilCliente.class);
                            startActivity(i);
                            finish();

                        }
                    }
                    catch (JSONException ex) {
                        Log.d("login", "" + ex);
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    //Toast.makeText(LoginActivity.this, "" + id, Toast.LENGTH_SHORT).show();

}


public void ifCliente(final int id) {
    String url = "https://inactive-mosses.000webhostapp.com/myslim/api/user/ifcliente/" + id;

    JsonObjectRequest jsObjRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        boolean status = response.getBoolean("status");
                        if(status) {
                            existeCliente(id);

                        }

                        else if(status == false){
                            getIDMecanico(id);
                        }

                        else {
                            Toast.makeText(MainActivity.this, "user nao existe", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException ex) {
                        Log.d("login", "" + ex);
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    //Toast.makeText(LoginActivity.this, "" + id, Toast.LENGTH_SHORT).show();

}



}
