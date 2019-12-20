package com.example.projeto_final.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.projeto_final.R;
import com.example.projeto_final.entities.Pedido;

import java.util.ArrayList;

public class CustomArrayAdapter extends ArrayAdapter<Pedido> {

    public CustomArrayAdapter(Context context, ArrayList<Pedido> users) {
        super(context,0,users);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Pedido p = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_linha, parent, false);
        ((TextView) convertView.findViewById(R.id.assunto)).setText(p.getAssunto());
        ((TextView) convertView.findViewById(R.id.localizacao_user)).setText("" + p.getLocalizacao());
        return convertView;

    }
}
