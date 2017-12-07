package com.example.gilbertosoares.cantinus3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import sinfo.ufrn.br.japi.JApi;

public class MenuCliente extends AppCompatActivity {
    private static final String TAG = "MenuCliente";

    double largura;
    double altura;

    SharedPreferences sharedPreferences;

    private TextView mTextTitle;

    private TextView mTextSaldo;

    private TextView mTextID;

    private String usuario;

    // Banco de Dados
    private SQLiteDatabase database;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_cliente);

        SharedPreferences token1 = this.getSharedPreferences("USER_INFO", 0);
        String token2=token1.getString("ACCESS_TOKEN", null);



        loginTask authorizationTask = new loginTask(this);

        //authorizationTask.delegate = JApiWebView.this;
        try {
            authorizationTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }






        Button addProduto = (Button) findViewById(R.id.buttonAddProduto);
        addProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecionarCardapio();
            }
        });

        Button acompanharProduto = (Button) findViewById(R.id.buttonAcompanhar);
        acompanharProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent listaPedidosCliente = new Intent(MenuCliente.this,ListaPedidosClienteActivity.class);

                startActivity(listaPedidosCliente);
            }
        });

        CantinusSQLOpenHelper helper = new CantinusSQLOpenHelper(this);
        database = helper.getReadableDatabase();

        mTextTitle = (TextView) findViewById(R.id.greetClient);
        mTextSaldo = (TextView) findViewById(R.id.textSaldo);
        mTextID = (TextView) findViewById(R.id.idCliente);



        sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String resultLoginSinfo = sharedPreferences.getString("user_info", "");
        usuario = resultLoginSinfo;

        //sharedPreferences = getSharedPreferences("user_name", Context.MODE_PRIVATE);
        String resultName = sharedPreferences.getString("user_name", "");

        mTextTitle.setText(mTextTitle.getText() + "\n" + resultName);
        mTextTitle.setGravity(Gravity.CENTER);
        mTextID.setText("ID: "+usuario);

        double saldo = getCredito();
        String textoSaldo = "Saldo: R$ " + saldo;
        textoSaldo = textoSaldo.replace('.', ',');
        mTextSaldo.setText(textoSaldo);
    }


    public void redirecionarCardapio(){

        Intent clienteCardapio = new Intent(MenuCliente.this,CardapioCliente.class);

        startActivity(clienteCardapio);

    }

    public double getCredito() {

        SQLiteDatabase db = database;
        Cursor cursor;

        cursor = db.rawQuery("SELECT Credito FROM CREDITOS WHERE Matricula = '" + usuario + "'", null);                     //The sort order

        int cursorCount = cursor.getCount();

        if (cursorCount <= 0) {
            cursor.close();
            db.execSQL("INSERT INTO CREDITOS (Matricula,Credito) VALUES (" + usuario + ", 0.0);");
            cursor.close();
        }

        cursor = db.rawQuery("SELECT Credito FROM CREDITOS WHERE Matricula = '" + usuario + "'", null);                     //The sort order

        if (cursor != null) {

            cursor.moveToFirst();

            String retorno = cursor.getString(cursor.getColumnIndex("Credito"));
            cursor.close();
            return Double.parseDouble(retorno);
        }
        return -1;
    }

    @Override
    public void onStart(){
        super.onStart();

        double saldo = getCredito();
        String textoSaldo = "Saldo: R$ " + saldo;
        textoSaldo = textoSaldo.replace('.', ',');
        mTextSaldo.setText(textoSaldo);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();

        JApi.deslogar(this);

    }




}
