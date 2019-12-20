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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PerfilCliente extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Session session;
    String id_cliente;
    String id_user;
    int id_int_cliente = -1;
    int id_int_user = -1;
    EditText nome;
    EditText telemovel;
    EditText ano;
    EditText matricula;
    EditText apelido;
    String[] marcas;
    String marca_nome;
    int marca_id;
    int modelo_id;
    int idmarca;
    int idmodelo;
    int idveiculo = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cliente);
        session = new Session(this);

        //  nome = (EditText) findViewById(R.id.nome);
//        ano = (EditText) findViewById(R.id.ano);
        //    matricula = (EditText) findViewById(R.id.matricula);

        nome = (EditText) findViewById(R.id.nome);
        telemovel = (EditText) findViewById(R.id.telemovel);
        ano = (EditText) findViewById(R.id.ano);
        matricula = (EditText) findViewById(R.id.matricula);
        apelido = (EditText) findViewById(R.id.apelido);
       // fillSpinner_marca();
        Button btn1 = (Button)findViewById(R.id.button1);

        if(!session.loggedIn()) {
            Intent intent = new Intent(PerfilCliente.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        SharedPreferences result = getSharedPreferences("myApp", Context.MODE_PRIVATE);
        final int id_user = result.getInt("ID_USER", -1);
        getIDCliente(id_user);
        fillPerfil(id_user);
       // Toast.makeText(PerfilCliente.this, "" + id_user, Toast.LENGTH_SHORT).show();
       // fillSpinner_marca(id_user);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nome.getText().toString().isEmpty() && !apelido.getText().toString().isEmpty() && !telemovel.getText().toString().isEmpty() && !ano.getText().toString().isEmpty() && !matricula.getText().toString().isEmpty()) {
                //cria_veiculo();
                checkMatricula(matricula.getText().toString());
                //edita_cliente(id_int_cliente);
               // Toast.makeText(PerfilCliente.this, "" + id_int_cliente, Toast.LENGTH_SHORT).show();
                //Toast.makeText(PerfilCliente.this, "" + idmarca, Toast.LENGTH_SHORT).show();
                //Toast.makeText(PerfilCliente.this, "" + idveiculo, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(PerfilCliente.this, MenuUtilizador.class);
                startActivity(i);
                finish();

                } else {
                    Toast.makeText(PerfilCliente.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    public void getIDCliente (final int id_user) {
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
                                if_perfil_completo(id_user);

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

    public void checkMatricula (String matricula) {
        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/veiculo/matriculacheck/" + matricula;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if(status) {
                                JSONObject obj = response.getJSONObject(("DATA"));
                                idveiculo =  Integer.parseInt(obj.getString("id"));
                                //Toast.makeText(PerfilCliente.this, "STATUS: " + status, Toast.LENGTH_SHORT).show();
                                edita_veiculo(idveiculo);
                               // edita_cliente(id_int_cliente);
                            }
                            else {
                                //Toast.makeText(PerfilCliente.this, "STATUS: " + status, Toast.LENGTH_SHORT).show();
                                cria_veiculo();
                            }
                        }
                        catch (JSONException ex) {
                            Log.d("MATRICULA", "" + ex);
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



    public void if_perfil_completo(final int id) {
        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/cliente/perfil_completo/" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if (status) {

                                //Toast.makeText(PerfilCliente.this,response.getString("MSG") , Toast.LENGTH_SHORT).show();
                                fillSpinner_marca(id, 0);

                            }
                            else {
                               // Toast.makeText(PerfilCliente.this,response.getString("MSG") , Toast.LENGTH_SHORT).show();
                                fillSpinner_marca(id, 1);
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

    private void fillPerfil(int id) {

        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/getclientes/" + id;
        //Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("DATA");
                    // arrayContacto.clear();
                    boolean status = response.getBoolean("status");
                    // Toast.makeText(MainActivity.this, "" + status, Toast.LENGTH_SHORT).show();
                    if(status == true) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            //Toast.makeText(MainActivity.this, obj.getString("nome"), Toast.LENGTH_SHORT).show()
                           /* arrayContacto.add(new Contacto(obj.getInt("id"), obj.getString("nome"), obj.getString("apelido"), obj.getInt("numero"), obj.getString("email"), obj.getString("morada"), obj.getInt("idade"), iduser));
                            CustomArrayAdapter itemsAdapter =
                                    new CustomArrayAdapter(MainActivity.this, arrayContacto);
                            ((ListView) findViewById(R.id.lista)).setAdapter(itemsAdapter);*/

                           String getnome = obj.getString("nome");
                           String getapelido = obj.getString("apelido");
                           String gettelemovel = String.valueOf(obj.getInt("telemovel"));
                           String getano = obj.getString("ano");
                           String getmatricula = obj.getString("matricula");


                            nome.setText(getnome);
                            apelido.setText(getapelido);
                            telemovel.setText(gettelemovel);
                            ano.setText(getano);
                            matricula.setText(getmatricula);

                            Log.d("valores", "nome: " + getnome + " apelido: " + getapelido + " telemovel: " + gettelemovel);


                        }
                    }
                    else  {
                    }
                } catch (JSONException ex) {
                    Log.d("fillPerfil", "" + ex);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public  void onErrorResponse(VolleyError error) {
                // Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);



       /* ArrayList<Contacto> arrayContacto = new ArrayList<>();
        arrayContacto.add(new Contacto("Miguel", "Oliveira", 966400474, "teste@teste.pt", "Rua", 20));
        CustomArrayAdapter itemsAdapter =
                new CustomArrayAdapter(this, arrayContacto);
        ((ListView) findViewById(R.id.lista)).setAdapter(itemsAdapter);*/
    }


    public void edita_cliente(int id) {
        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/cliente/editar/" + id;

        Map<String, String> jsonParams = new HashMap<String, String>();
        String getnome = nome.getText().toString();
        String getapelido = apelido.getText().toString();
        String gettelemovel = telemovel.getText().toString();
        jsonParams.put("nome", getnome);
        jsonParams.put("apelido", getapelido);
        jsonParams.put("telemovel", gettelemovel);
        jsonParams.put("veiculo_id", String.valueOf(idveiculo));
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                               // Toast.makeText(PerfilCliente.this,response.getString("MSG") , Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PerfilCliente.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void cria_veiculo() {
        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/cria_veiculo";

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("marca_id", String.valueOf(idmarca));
        jsonParams.put("modelo_id", String.valueOf(idmodelo));
        jsonParams.put("ano", ano.getText().toString());
        jsonParams.put("matricula", matricula.getText().toString());
        Log.d("TESTE", "" + ano.getText().toString() + " " + matricula.getText().toString());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                idveiculo = response.getInt("VEICULO_ID");
                                //Toast.makeText(PerfilCliente.this, "IDVEICULO: " + idveiculo, Toast.LENGTH_SHORT).show();
                                //Toast.makeText(PerfilCliente.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                                edita_cliente(id_int_cliente);
                            } else {
                                //Toast.makeText(PerfilCliente.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex) {
                            Toast.makeText(PerfilCliente.this, "" + ex, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PerfilCliente.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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




    public void edita_veiculo(int id) {
        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/edita_veiculo/" + id;

        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("marca_id", String.valueOf(idmarca));
        jsonParams.put("modelo_id", String.valueOf(idmodelo));
        jsonParams.put("ano", ano.getText().toString());
        jsonParams.put("matricula", matricula.getText().toString());
        //Log.d("TESTE", "" + ano.getText().toString() + " " + matricula.getText().toString());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                //idveiculo = response.getInt("VEICULO_ID");
                               // Toast.makeText(PerfilCliente.this, "IDVEICULO: " + idveiculo, Toast.LENGTH_SHORT).show();
                               // Toast.makeText(PerfilCliente.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                                edita_cliente(id_int_cliente);
                            } else {
                               // Toast.makeText(PerfilCliente.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                                edita_cliente(id_int_cliente);
                            }
                        } catch (JSONException ex) {
                            Toast.makeText(PerfilCliente.this, "" + ex, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PerfilCliente.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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



/*
    public void fillSpinner_marca(){



        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/getmarcas";


        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                marcas = new String[response.length()];


                try {
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject obj = response.getJSONObject(i);
                        marcas[i] = obj.getString("marca");

                    }

                }
                catch (JSONException ex) {

                }

                final Spinner spinner_marca = (Spinner) findViewById(R.id.spinner_marca);
                ArrayAdapter<String> marca = new ArrayAdapter<String>(PerfilCliente.this,android.R.layout.simple_spinner_item, marcas);
                spinner_marca.setOnItemSelectedListener(PerfilCliente.this);
                marca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_marca.setAdapter(marca);

                spinner_marca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if(parent.getItemAtPosition(position).equals("Audi")) {
                            getModelo_audi();
                        }
                        if(parent.getItemAtPosition(position).equals("BMW")) {
                            getModelo_bmw();
                        }
                        if(parent.getItemAtPosition(position).equals("Honda")) {
                            getModelo_honda();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(req);

    }
*/
    private void fillSpinner_marca(final int user_id, final int ifnovo) {

        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/getmarcas/" + user_id;
        //Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("DATA");
                    marcas = new String[arr.length()];
                    // arrayContacto.clear();
                    boolean status = response.getBoolean("status");


                    if(ifnovo == 0)
                    marca_id = response.getInt("MARCA");


                    // Toast.makeText(MainActivity.this, "" + status, Toast.LENGTH_SHORT).show();
                    if(status == true) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            marcas[i] = obj.getString("marca");

                        }
                    }


                    else  {


                    }
                } catch (JSONException ex) {
                    Log.d("fillLista", "" + ex);
                }

                final Spinner spinner_marca = (Spinner) findViewById(R.id.spinner_marca);
                ArrayAdapter<String> marca = new ArrayAdapter<String>(PerfilCliente.this,android.R.layout.simple_spinner_item, marcas);
                spinner_marca.setOnItemSelectedListener(PerfilCliente.this);
                marca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_marca.setAdapter(marca);
                if(ifnovo == 0) {
                    spinner_marca.setSelection(marca_id - 1);
                }
                spinner_marca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if(parent.getItemAtPosition(position).equals("Audi")) {
                            idmarca = 1;
                            if(ifnovo == 1)
                            getModelo_audi(user_id,1);

                            if(ifnovo == 0)
                                getModelo_audi(user_id, 0);
                        }
                        if(parent.getItemAtPosition(position).equals("BMW")) {
                            idmarca = 2;
                            if(ifnovo == 1)
                                getModelo_bmw(user_id,1);

                            if(ifnovo == 0)
                                getModelo_bmw(user_id, 0);
                        }
                        if(parent.getItemAtPosition(position).equals("Honda")) {
                            idmarca = 3;
                            if(ifnovo == 1)
                                getModelo_honda(user_id,1);

                            if(ifnovo == 0)
                                getModelo_honda(user_id, 0);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });





            }
        }, new Response.ErrorListener() {
            @Override
            public  void onErrorResponse(VolleyError error) {
                // Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);


    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        marca_nome = marcas[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

/*
    public void getModelo_audi() {


        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/getmodelo_audi";

        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                marcas = new String[response.length()];


                try {

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject obj = response.getJSONObject(i);
                        marcas[i] = obj.getString("modelo");

                    }

                }
                catch (JSONException ex) {

                }

                Spinner spinner_modelo = (Spinner) findViewById(R.id.spinner_modelo);
                ArrayAdapter<String> marca = new ArrayAdapter<String>(PerfilCliente.this,android.R.layout.simple_spinner_item, marcas);
                spinner_modelo.setOnItemSelectedListener(PerfilCliente.this);
                marca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_modelo.setAdapter(marca);

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(req);

    }
*/

    private void getModelo_audi(int user_id, final int ifnovo) {

        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/getmodelo_audi/" + user_id;
        //Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("DATA");
                    marcas = new String[arr.length()];
                    if(ifnovo == 0)
                    modelo_id = response.getInt("MODELO");


                    // arrayContacto.clear();
                    boolean status = response.getBoolean("status");
                    //marca_id = response.getInt("MARCA");
                    // Toast.makeText(MainActivity.this, "" + status, Toast.LENGTH_SHORT).show();
                    if(status == true) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            marcas[i] = obj.getString("modelo");

                        }
                    }


                    else  {


                    }
                } catch (JSONException ex) {

                }

                Spinner spinner_modelo = (Spinner) findViewById(R.id.spinner_modelo);
                ArrayAdapter<String> marca = new ArrayAdapter<String>(PerfilCliente.this,android.R.layout.simple_spinner_item, marcas);
                spinner_modelo.setOnItemSelectedListener(PerfilCliente.this);
                marca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_modelo.setAdapter(marca);
                if(ifnovo == 0) {
                    spinner_modelo.setSelection(modelo_id - 1);
                }

                spinner_modelo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if(position == 0)
                                idmodelo = 1;

                            if(position == 1)
                                idmodelo = 2;

                            if(position == 2)
                                idmodelo = 3;

                            if(position == 3)
                                idmodelo = 4;

                            if(position == 4)
                                idmodelo = 5;

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });







            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }


/*
    public void getModelo_bmw() {


        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/getmodelo_bmw";

        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                marcas = new String[response.length()];


                try {

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject obj = response.getJSONObject(i);
                        marcas[i] = obj.getString("modelo");

                    }

                }
                catch (JSONException ex) {

                }

                Spinner spinner_modelo = (Spinner) findViewById(R.id.spinner_modelo);
                ArrayAdapter<String> marca = new ArrayAdapter<String>(PerfilCliente.this,android.R.layout.simple_spinner_item, marcas);
                spinner_modelo.setOnItemSelectedListener(PerfilCliente.this);
                marca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_modelo.setAdapter(marca);

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(req);

    }

 */

    private void getModelo_bmw(int user_id, final int ifnovo) {

        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/getmodelo_bmw/" + user_id;
        //Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("DATA");
                    marcas = new String[arr.length()];


                    if(ifnovo == 0)
                    modelo_id = response.getInt("MODELO");



                    // arrayContacto.clear();
                    boolean status = response.getBoolean("status");
                    //marca_id = response.getInt("MARCA");
                    // Toast.makeText(MainActivity.this, "" + status, Toast.LENGTH_SHORT).show();
                    if(status == true) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            marcas[i] = obj.getString("modelo");

                        }
                    }


                    else  {


                    }
                } catch (JSONException ex) {

                }

                Spinner spinner_modelo = (Spinner) findViewById(R.id.spinner_modelo);
                ArrayAdapter<String> marca = new ArrayAdapter<String>(PerfilCliente.this,android.R.layout.simple_spinner_item, marcas);
                spinner_modelo.setOnItemSelectedListener(PerfilCliente.this);
                marca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_modelo.setAdapter(marca);
                if(ifnovo == 0) {
                    spinner_modelo.setSelection(modelo_id - 6);
                }

                spinner_modelo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if(position == 0)
                            idmodelo = 6;

                        if(position == 1)
                            idmodelo = 7;

                        if(position == 2)
                            idmodelo = 8;

                        if(position == 3)
                            idmodelo = 9;

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });



            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

/*
    public void getModelo_honda() {


        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/getmodelo_honda";

        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                marcas = new String[response.length()];


                try {

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject obj = response.getJSONObject(i);
                        marcas[i] = obj.getString("modelo");

                    }

                }
                catch (JSONException ex) {

                }

                Spinner spinner_modelo = (Spinner) findViewById(R.id.spinner_modelo);
                ArrayAdapter<String> marca = new ArrayAdapter<String>(PerfilCliente.this,android.R.layout.simple_spinner_item, marcas);
                spinner_modelo.setOnItemSelectedListener(PerfilCliente.this);
                marca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_modelo.setAdapter(marca);

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(req);

    }

 */

    private void getModelo_honda(int user_id, final int ifnovo) {

        String url = "https://inactive-mosses.000webhostapp.com/myslim/api/getmodelo_honda/" + user_id;
        //Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("DATA");
                    marcas = new String[arr.length()];
                    if(ifnovo == 0)
                    modelo_id = response.getInt("MODELO");

                    // arrayContacto.clear();
                    boolean status = response.getBoolean("status");
                    //marca_id = response.getInt("MARCA");
                    // Toast.makeText(MainActivity.this, "" + status, Toast.LENGTH_SHORT).show();
                    if(status == true) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);

                            marcas[i] = obj.getString("modelo");

                        }
                    }


                    else  {


                    }
                } catch (JSONException ex) {

                }

                Spinner spinner_modelo = (Spinner) findViewById(R.id.spinner_modelo);
                ArrayAdapter<String> marca = new ArrayAdapter<String>(PerfilCliente.this,android.R.layout.simple_spinner_item, marcas);
                spinner_modelo.setOnItemSelectedListener(PerfilCliente.this);
                marca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_modelo.setAdapter(marca);
                if(ifnovo == 0) {
                    spinner_modelo.setSelection(modelo_id - 10);
                }

                spinner_modelo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if(position == 0)
                            idmodelo = 10;

                        if(position == 1)
                            idmodelo = 11;

                        if(position == 2)
                            idmodelo = 12;

                        if(position == 3)
                            idmodelo = 13;

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });



            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Access the RequestQueue through your singleton class.
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
                session.setLoggedIn(false, id_int_user);
                Intent intent = new Intent(PerfilCliente.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}