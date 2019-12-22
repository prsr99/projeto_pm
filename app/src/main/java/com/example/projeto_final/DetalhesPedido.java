package com.example.projeto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetalhesPedido extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pedido);

        TextView assunto = (TextView) findViewById(R.id.assunto_tv);
        TextView mensagem = (TextView) findViewById(R.id.mensagem_tv);
        TextView localizacao = (TextView) findViewById(R.id.localizacao_tv);
        TextView mecanico = (TextView) findViewById(R.id.nome_mecanico_tv);

        assunto.setText(" Assunto: " + getIntent().getStringExtra("assunto"));
        mensagem.setText(" Mensagem: " + getIntent().getStringExtra("mensagem"));
        localizacao.setText(" Localização: " + getIntent().getStringExtra("localizacao"));
        if (getIntent().getStringExtra("nome_mecanico").equals("null")) {
            mecanico.setText("Estado: Pendente");
        }
        else {
            mecanico.setText(" Atendido por: " + getIntent().getStringExtra("nome_mecanico") + " " + getIntent().getStringExtra("apelido_mecanico"));
        }

    }
}
