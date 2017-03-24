package com.dexati.photogridbuilder.social.instagram.oauth;

import android.content.Context;
import android.content.SharedPreferences;

public class InstagramSession
{

    private static final String API_ACCESS_TOKEN = "access_token";
    private static final String API_ID = "id";
    private static final String API_NAME = "name";
    private static final String API_PROFLEPIC = "profile_picture";
    private static final String API_USERNAME = "username";
    private static final String SHARED = "Instagram_Preferences";
    private android.content.SharedPreferences.Editor editor;
    private SharedPreferences sharedPref;

    public InstagramSession(Context context)
    {
        sharedPref = context.getSharedPreferences("Instagram_Preferences", 0);
        editor = sharedPref.edit();
    }

    public String getAccessToken()
    {
        return sharedPref.getString("access_token", null);
    }

    public String getId()
    {
        return sharedPref.getString("id", null);
    }

    public String getName()
    {
        return sharedPref.getString("name", null);
    }

    public String getProfilePic()
    {
        return sharedPref.getString("profile_picture", null);
    }

    public String getUsername()
    {
        return sharedPref.getString("username", null);
    }

    public void resetAccessToken()
    {
        editor.putString("id", null);
        editor.putString("name", null);
        editor.putString("access_token", null);
        editor.putString("username", null);
        editor.putString("profile_picture", null);
        editor.commit();
    }

    public void storeAccessToken(String s)
    {
        editor.putString("access_token", s);
        editor.commit();
    }

    public void storeAccessToken(String s, String s1, String s2, String s3, String s4)
    {
        editor.putString("id", s1);
        editor.putString("name", s3);
        editor.putString("access_token", s);
        editor.putString("username", s2);
        editor.putString("profile_picture", s4);
        editor.commit();
    }
}
