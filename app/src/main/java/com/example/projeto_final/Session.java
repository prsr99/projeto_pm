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

    public void setLoggedIn( boolean loggedIn, int id_user) {
        editor.putBoolean("loggedInMode",loggedIn);
        editor.putInt("ID_USER", id_user);
        editor.commit();
    }

    public boolean loggedIn() {
        return prefs.getBoolean("loggedInMode",false);
    }
}
