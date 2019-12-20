package com.example.projeto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuUtilizador extends AppCompatActivity {

    Session session;
    Button b1,b2,b3,b4,b5;
    int id_int_user = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utilizador);


        session = new Session(this);

        SharedPreferences result = getSharedPreferences("myApp", Context.MODE_PRIVATE);
        int id_cliente = result.getInt("ID_CLIENTE", -1);
        b1 = (Button)findViewById(R.id.perfil);
        b2 = (Button)findViewById(R.id.pedido_assistencia);
        b3 = (Button)findViewById(R.id.pedido_efetuados);
        b4 = (Button)findViewById(R.id.logoutt);
        b5 = (Button)findViewById(R.id.pedido_deccorrer);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuUtilizador.this, PerfilCliente.class);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuUtilizador.this, PedidoAssistencia.class);
                startActivity(intent);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MenuUtilizador.this, PedidosEfetuados.class);
                //startActivity(intent);
            }
        });


        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLoggedIn(false, id_int_user);
                Intent intent = new Intent(MenuUtilizador.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MenuUtilizador.this, PedidosDecorrer.class);
                //startActivity(intent);
            }
        });
    }
}

