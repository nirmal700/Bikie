package com.bikie.in.SessionManager;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences("CustomerAppKey", 0);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    // Set Login
    public void setUserLogin(boolean login) {
        editor.putBoolean("KEY_LOGIN", login);
        editor.commit();
    }

    public boolean getUserLogin() {

        return sharedPreferences.getBoolean("KEY_LOGIN", false);
    }

    public void setDetails(String phoneNo, String password, String name, String dlNo,String aadharNo, String dlURL, String aadharURL1, String aadharURL2,String mailId,String profilePictureURL) {
        editor.putString("KEY_PHONE", phoneNo);
        editor.putString("KEY_PASSWORD", password);
        editor.putString("KEY_NAME",name);
        editor.putString("KEY_DL_NO",dlNo);
        editor.putString("KEY_AADHAR_NO",aadharNo);
        editor.putString("KEY_DL_IMG_URL",dlURL);
        editor.putString("KEY_AADHAR_IMG_URL1",aadharURL1);
        editor.putString("KEY_AADHAR_IMG_URL2",aadharURL2);
        editor.putString("KEY_PROFILE_PICTURE_IMG_URL",profilePictureURL);
        editor.putString("KEY_USER_MAIL_ID",mailId);






        editor.commit();

    }


    public String getPhone() {
        return sharedPreferences.getString("KEY_PHONE", "");
    }
    public String getPassword() {
        return sharedPreferences.getString("KEY_PASSWORD", "");
    }
    public String getName() {
        return sharedPreferences.getString("KEY_NAME", "");
    }
    public String getDlNo() {
        return sharedPreferences.getString("KEY_DL_NO", "");
    }
    public String getMailId() {
        return sharedPreferences.getString("KEY_USER_MAIL_ID", "");
    }
    public String getDlImageURL() {
        return sharedPreferences.getString("KEY_DL_IMG_URL", "");
    }
    public String getProfilePictureImageURL() {
        return sharedPreferences.getString("KEY_PROFILE_PICTURE_IMG_URL", "");
    }
    public String getAadharNo() {
        return sharedPreferences.getString("KEY_AADHAR_NO", "");
    }
    public String getAadharFrontURL() {
        return sharedPreferences.getString("KEY_AADHAR_IMG_URL1", "");
    }
    public String getAadharBackURL() {
        return sharedPreferences.getString("KEY_AADHAR_IMG_URL2", "");
    }



    public void logoutUserFromSession() {
        editor.clear();
        editor.commit();
    }
}
