package com.example.dell.instagramlogin.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.dell.instagramlogin.model.ModelInstaProfile;
import com.example.dell.webmobtechpractical.model.ModelInstaProfile;
import com.google.gson.Gson;

/**
 * Created by dell on 11/4/2017.
 */

public class UserPreferences {

    private Context mconContext;
    private String PREF_NAME = "webmob_pref";
    private String DATA_USERINFO = "userinfo";
    private SharedPreferences sharedPreferences;

    public UserPreferences(Context mconContext) {
        this.mconContext = mconContext;
        sharedPreferences = mconContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserInfo(ModelInstaProfile modelInstaProfile) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DATA_USERINFO, new Gson().toJson(modelInstaProfile));
        editor.commit();
    }

    public ModelInstaProfile getUserInfo() {
        String data = sharedPreferences.getString(DATA_USERINFO, null);
        return new Gson().fromJson(data, ModelInstaProfile.class);
    }
}
