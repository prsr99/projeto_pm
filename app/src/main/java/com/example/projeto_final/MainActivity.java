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
    int id_int = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      final EditText et1 = (EditText)findViewById(R.id.user);
      final EditText et2 = (EditText)findViewById(R.id.password);
        Button btn1 = (Button)findViewById(R.id.button1);
        Button btn2 = (Button)findViewById(R.id.button2);

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
                                id_int = Integer.parseInt(id);
                               // Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                //intent.putExtra("ID", id_int);
                               // managePrefs();
                                //startActivity(intent);
                                Toast.makeText(MainActivity.this, "Login efetuado com sucesso com o id: " + id, Toast.LENGTH_SHORT).show();
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







}
