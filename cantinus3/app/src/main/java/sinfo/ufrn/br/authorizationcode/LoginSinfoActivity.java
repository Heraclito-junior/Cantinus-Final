package sinfo.ufrn.br.authorizationcode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.gilbertosoares.cantinus3.LoginActivity;
import com.example.gilbertosoares.cantinus3.MenuCliente;
import com.example.gilbertosoares.cantinus3.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ca.mimic.oauth2library.OAuth2Client;
import ca.mimic.oauth2library.OAuthResponse;
import sinfo.ufrn.br.japi.JApiWebView;

public class LoginSinfoActivity extends AppCompatActivity {

    private static final String CLIENT_ID_VALUE = "cantinus-id";
    private static final String SECRET_KEY = "segredo";
    private static final String STATE = "NgcKbvIMQ0hu10qEwlI5USBdskZGfME22ntDDcmE";

    private static final String REDIRECT_URI = "https://apitestes.info.ufrn.br";
    private static final String AUTHORIZATION_URL = "https://autenticacao.ufrn.br/authz-server/oauth/authorize";
    private static final String ACCESS_TOKEN_URL = "https://autenticacao.ufrn.br/authz-server/oauth/token";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String RESPONSE_TYPE_VALUE = "code";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String STATE_PARAM = "state";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";

    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";

    private WebView webView;
    private ProgressDialog pd;

    public boolean jaEntrou = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sinfo);

        webView = (WebView) findViewById(R.id.japiwebview);
        webView.requestFocus(View.FOCUS_DOWN);

        pd = ProgressDialog.show(this, "", "Carregando...", true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl) {
                if (authorizationUrl.startsWith(REDIRECT_URI)) {
                    Uri uri = Uri.parse(authorizationUrl);

                    String authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE);
                    if (authorizationToken == null) {
                        return true;
                    }

                    new PostRequestAsyncTask().execute(authorizationToken);

                } else {
                    if(jaEntrou){
                        jaEntrou = false;
                        Intent intent = new Intent(LoginSinfoActivity.this, MenuCliente.class);
                        finish();
                        startActivity(intent);
                    }
                    if(!jaEntrou) {
                        jaEntrou = true;
                        webView.loadUrl(authorizationUrl);
                    }
                    Log.w("LoginSinfoActivity", "else!!");
                }
                return true;
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        String authUrl = getAuthorizationUrl();
        webView.loadUrl(authUrl);
    }

    private static String getAuthorizationUrl() {
        return AUTHORIZATION_URL
                + QUESTION_MARK + RESPONSE_TYPE_PARAM + EQUALS + RESPONSE_TYPE_VALUE
                + AMPERSAND + CLIENT_ID_PARAM + EQUALS + CLIENT_ID_VALUE
                + AMPERSAND + STATE_PARAM + EQUALS + STATE
                + AMPERSAND + REDIRECT_URI_PARAM + EQUALS + REDIRECT_URI;
    }

    private class PostRequestAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            Log.w("LoginSinfoActivity", "onPreExecute!");
            pd = ProgressDialog.show(LoginSinfoActivity.this, "", "loading", true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Log.w("doInBackground", "doInBackground");
            try {
                OAuth2Client client;
                Map<String, String> map = new HashMap<>();
                map.put(REDIRECT_URI_PARAM, REDIRECT_URI);
                map.put(RESPONSE_TYPE_VALUE, params[0]);

                client = new OAuth2Client.Builder(CLIENT_ID_VALUE, SECRET_KEY, ACCESS_TOKEN_URL)
                        .grantType(GRANT_TYPE)
                        .parameters(map)
                        .build();

                OAuthResponse response = client.requestAccessToken();
                if (response.isSuccessful()) {
                    Log.w("LoginSinfoActivity", "doInBackground: Consegui acessar!");
                    savePreferences(response);
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean status) {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (status) {
                Log.w("LoginSinfoActivity", "onPostExecute!!");
                Intent startProfileActivity = new Intent(LoginSinfoActivity.this, MenuCliente.class);
                LoginSinfoActivity.this.startActivity(startProfileActivity);
            }
        }

    }

    private void savePreferences(OAuthResponse response) {
        String accessToken = response.getAccessToken();
        String refreshToken = response.getRefreshToken();
        Long expiresIn = response.getExpiresIn();

        SharedPreferences preferences = this.getSharedPreferences("user_info", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.KEY_ACCESS_TOKEN, accessToken);
        editor.putString(Constants.KEY_REFRESH_TOKEN, refreshToken);
        editor.putLong(Constants.KEY_EXPIRES_IN, expiresIn);
        editor.commit();
    }
}
