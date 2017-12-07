package com.example.gilbertosoares.cantinus3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sinfo.ufrn.br.japi.JApi;
import sinfo.ufrn.br.japi.JApiWebView;

public class Logincliente extends AppCompatActivity {

    private static final String CLIENT_ID_VALUE = "cantinus-id";
    private static final String SECRET_KEY = "segredo";
    private static final String STATE = "NgcKbvIMQ0hu10qEwlI5USBdskZGfME22ntDDcmE";
    private static final String TAG = "MyActivity";

    private static final String REDIRECT_URI = "https://apitestes.info.ufrn.br";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logincliente);

        JApiWebView japiWebView = (JApiWebView) findViewById(R.id.japiwebview);
        japiWebView.loadJapiWebView(REDIRECT_URI, CLIENT_ID_VALUE, SECRET_KEY, this, MenuCliente.class);



        // Pegar o Access Token
        JApi.getAccessToken(this);
        // Pegar o Refresh Token
        JApi.getRefreshToken(this);
        // Pegar o Expires In
        JApi.getExpiresIn(this);
        // Pegar o Token Type
        JApi.getTokenType(this);
    }
}



