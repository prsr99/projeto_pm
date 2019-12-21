package com.example.projeto_final;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public Session(Context ctx) {
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("myApp",Context.MODE_PRIVATE);
        editor = prefs.edit();      // abrir sharedPreferences em modo de edição
    }

    public void setLoggedInCliente( boolean loggedIn, int id_user) {
        editor.putBoolean("loggedInCliente",loggedIn);
        editor.putInt("ID_USER", id_user);
        editor.commit();
    }

    public void setClienteID(int id_cliente) {
        editor.putInt("ID_CLIENTE", id_cliente);
        editor.commit();
    }

    public void setLoggedInMecanico(boolean loggedIn, int id_mecanico) {
        editor.putInt("ID_MECANICO", id_mecanico);
        editor.putBoolean("loggedInMecanico",loggedIn);
        editor.commit();
    }

    public boolean loggedInCliente() {
        return prefs.getBoolean("loggedInCliente",false);
    }

    public boolean loggedInMecanico() {
        return prefs.getBoolean("loggedInMecanico",false);
    }
}
