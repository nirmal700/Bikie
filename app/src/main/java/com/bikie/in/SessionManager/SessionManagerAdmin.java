package com.bikie.in.SessionManager;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagerAdmin {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SessionManagerAdmin(Context context) {
        sharedPreferences = context.getSharedPreferences("CustomerAppKeyAdmin", 0);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void setAdminLogin(boolean login) {
        editor.putBoolean("KEY_ADMIN_LOGIN", login);
        editor.commit();
    }

    public boolean getAdminLogin() {

        return sharedPreferences.getBoolean("KEY_ADMIN_LOGIN", false);
    }


    public void setDetails(String phoneNo, String password, String name, String mailId) {
        editor.putString("KEY_ADMIN_PHONE", phoneNo);
        editor.putString("KEY_ADMIN_PASSWORD", password);
        editor.putString("KEY_ADMIN_NAME",name);
        editor.putString("KEY_ADMIN_MAIL_ID",mailId);


        editor.commit();

    }

    public String getAdminPhone() {
        return sharedPreferences.getString("KEY_ADMIN_PHONE", "");
    }
    public String getAdminPassword() {
        return sharedPreferences.getString("KEY_ADMIN_PASSWORD", "");
    }
    public String getAdminName() {
        return sharedPreferences.getString("KEY_ADMIN_NAME", "");
    }

    public String getAdminMailId() {
        return sharedPreferences.getString("KEY_ADMIN_MAIL_ID", "");
    }


}
