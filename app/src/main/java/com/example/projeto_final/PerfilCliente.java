package com.example.projeto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PerfilCliente extends AppCompatActivity {
    int id;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cliente);
        Button btn1 = (Button)findViewById(R.id.button1);
        session = new Session(this);
        if(!session.loggedIn()) {
            Intent intent = new Intent(PerfilCliente.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        SharedPreferences result = getSharedPreferences("myApp", Context.MODE_PRIVATE);
        final int id = result.getInt("ID", -1);
        //Toast.makeText(PerfilCliente.this, "" + id, Toast.LENGTH_SHORT).show();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    session.setLoggedIn(false, id);
                    Intent intent = new Intent(PerfilCliente.this, MainActivity.class);
                    startActivity(intent);
                    finish();

            }
        });
    }
}
