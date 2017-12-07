package sinfo.ufrn.br.japi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import ca.mimic.oauth2library.OAuthResponse;

/**
 * Created by victor on 28/09/17.
 */

public class JApi {
    private static final String KEY_ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String KEY_REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String KEY_EXPIRES_IN = "EXPIRES_IN";
    private static final String KEY_TOKEN_TYPE = "TOKEN_TYPE";
    private static final String KEY_USER_INFO = "USER_INFO";
    private static final String TAG = "japi";


    public static Context mContext;

    public void savePreferences(OAuthResponse response, Context context) {
        mContext=context;

        String accessToken = response.getAccessToken();
        String refreshToken = response.getRefreshToken();
        String tokenType = response.getTokenType();
        Long expiresIn = response.getExpiresIn();

        SharedPreferences preferences = context.getSharedPreferences(KEY_USER_INFO, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.putString(KEY_TOKEN_TYPE, tokenType);
        editor.putLong(KEY_EXPIRES_IN, expiresIn);
        editor.commit();
    }

/**
    public static Context getmContext() {
        return mContext;
    }
 */

    public static String getAccessToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_USER_INFO, 0);
        return preferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public static String getRefreshToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_USER_INFO, 0);
        return preferences.getString(KEY_REFRESH_TOKEN, null);
    }

    public static Long getExpiresIn(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_USER_INFO, 0);
        return preferences.getLong(KEY_EXPIRES_IN, 0);
    }

    public static String getTokenType(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_USER_INFO, 0);
        return preferences.getString(KEY_TOKEN_TYPE, null);
    }

     public static void deslogar(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_USER_INFO, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
         cookieClean();
        JApiCache.deleteCache(context);
    }

    public static String cookieClean() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Log.d(TAG, "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            Log.d(TAG, "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(mContext);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
        return null;
    }

        public Context retornoContexto() {
            return mContext;
        }




}
