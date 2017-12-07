package com.example.gilbertosoares.cantinus3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ca.mimic.oauth2library.OAuthResponse;
import sinfo.ufrn.br.japi.interfaces.AsyncResponse;


/**
 * Created by victor on 28/09/17.
 */

public class loginTask extends AsyncTask<String, Void, OAuthResponse> {
    private static final String TAG = "logintask";

    private Activity mActivity;
    private Class mClass;
    private ProgressDialog mProgressDialog;
    SharedPreferences sharedPreferences;

    public AsyncResponse delegate = null;

    private Context mContext;
/**
    public loginTask (Context context){
        mContext = context;
    }
*/
    public loginTask(Context context) {

        mContext = context;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected OAuthResponse doInBackground(String... params) {

        sharedPreferences = mContext.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String result = sharedPreferences.getString("user_info", "");

        SharedPreferences token1 = mContext.getSharedPreferences("USER_INFO", 0);
        String token2=token1.getString("ACCESS_TOKEN", null);


        SharedPreferences token3 = mContext.getSharedPreferences("USER_INFO", 0);
        String token4=token3.getString("TOKEN_TYPE", null);
        String AuthorizationInfo="bearer "+token2;


        String A;
        URL url;
        HttpURLConnection urlConnection = null;
        String resultado="";
        try {
            url = new URL("https://apitestes.info.ufrn.br/usuario/v0.1/usuarios/info");

            urlConnection = (HttpURLConnection) url
                    .openConnection();


            urlConnection.setRequestProperty("Authorization",AuthorizationInfo);

            urlConnection.setRequestProperty("x-api-key"," NgcKbvIMQ0hu10qEwlI5USBdskZGfME22ntDDcmE");

            InputStream in = urlConnection.getInputStream();



            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                System.out.print(current);
                resultado+=current;

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }




        int i=0;
        String id="";

        String indice=""+resultado.indexOf("id-usuario");


        String fim="";
        int j=resultado.indexOf("id-usuario")+12;

        while(resultado.charAt(j)!=','){
            fim+=resultado.charAt(j);
            j++;
        }

        String nome="";
        int k=resultado.indexOf("nome-pessoa")+14;

        while(resultado.charAt(k)!='"'){
            nome+=resultado.charAt(k);
            k++;
        }




        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_info", fim);
        editor.putString("user_name", nome);
        editor.apply();

        return null;


    }

    @Override
    public void onPostExecute(OAuthResponse result) {

    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

}
