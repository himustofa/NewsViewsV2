package com.apps.newsviews.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "userLoginInfo";
    private static final String USER_EMAIL = "email";
    private static final String USER_IS_LOGGED = "isLoggedIn";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //===============================================| Save SharedPreferences
    public boolean saveSharedPref(String email, boolean isLogged){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_EMAIL, email);
        editor.putBoolean(USER_IS_LOGGED, isLogged);
        editor.apply();
        return true;
    }

    //===============================================| Fetch/Get SharedPreferences
    public boolean getSharedPrefIsLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getBoolean(USER_IS_LOGGED, false);
    }

    public String getSharedPrefEmail(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(USER_EMAIL, null);
    }

    //===============================================| Remove SharedPreferences
    public void removeSharedPref(){
        SharedPreferences pre = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.clear(); //Remove from login.xml file
        editor.commit();
    }

}
