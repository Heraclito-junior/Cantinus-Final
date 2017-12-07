package com.example.gilbertosoares.cantinus3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ActivityInicial extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_info", "");
        editor.apply();

        sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String resultLoginSinfo = sharedPreferences.getString("user_info", "");
        Log.w("LOGIN: ",resultLoginSinfo + "teste");

        Button buttonCliente = (Button) findViewById(R.id.buttonCliente);
        buttonCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent loginCliente = new Intent(ActivityInicial.this, LoginSinfoActivity.class);
                Intent loginCliente = new Intent(ActivityInicial.this, Logincliente.class);

                startActivity(loginCliente);
            }
        });

        Button buttonGerente = (Button) findViewById(R.id.buttonGerente);
        buttonGerente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginGerente = new Intent(ActivityInicial.this,LoginActivity.class);

                startActivity(loginGerente);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_info", "");
        editor.apply();
    }
}
