package com.example.projeto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import java.util.regex.Pattern;

public class registar extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");


    EditText e1;
    EditText e2;
    EditText e3;
    Button btn;
    boolean exist;
    Context mContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);

        e1 = (EditText) findViewById(R.id.email);
        e2 = (EditText) findViewById(R.id.pass);
        e3 = (EditText) findViewById(R.id.conf_pass);
        btn = (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e1.getText().toString();
                String password = e2.getText().toString();
                String confirm_pass = e3.getText().toString();

                boolean check_email = validadeEmail();
                boolean check_password = validatePassword();

                if (password.equals(confirm_pass)) {
                    if(check_email && check_password) {
                        email_exists(email,password);
                    }
                }
                else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setCancelable(true);
                    builder.setMessage(R.string.pass_no_match);
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });


                    AlertDialog dialog = builder.create();
                    dialog.show();



                    e3.setError(getResources().getString(R.string.pass_no_match));
                }
            }
        });

    }


    public void email_exists(final String email, final String password) {
        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/users/usercheck/" + email;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if (status) {
                                Toast.makeText(registar.this, getResources().getString(R.string.email_existe), Toast.LENGTH_SHORT).show();

                            }
                            else {
                                Registar(email,password);
                                //Intent i = new Intent(registar.this, Login.class);
                                //startActivity(i);
                                finish();
                            }

                        } catch (JSONException ex) {
                            Toast.makeText(registar.this, "" + ex, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(registar.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }


    public void Registar(String email, String password) {
        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/users";

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("email", email);
        jsonParams.put("password", password);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                Toast.makeText(registar.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(registar.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex) {
                            Toast.makeText(registar.this, "" + ex, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(registar.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    public boolean validatePassword() {
        String passwordInput = e2.getEditableText().toString().trim();

        if(passwordInput.isEmpty()) {
            e2.setError(getResources().getString(R.string.campo_vazio));
            return false;
        }
        else if(!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            e2.setError(getResources().getString(R.string.weak_pass));
            return false;
        }
        else {
            e2.setError(null);
            return true;
        }
    }

    public boolean validadeEmail() {
        String emailInput = e1.getEditableText().toString().trim();

        if (emailInput.isEmpty()) {
            e1.setError(getResources().getString(R.string.campo_vazio));
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) { // CTRL + B
            e1.setError(getResources().getString(R.string.email_valido));
            return false;
        }
        else{
            e1.setError(null);
            return true;
        }
    }
}