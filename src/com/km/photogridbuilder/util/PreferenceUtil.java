package com.km.photogridbuilder.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil
{

    public static final String IS_CAMERA = "iscamera";
    public static final String IS_FACEBOOK = "isfacebook";
    public static final String IS_GALLARY = "isgallary";
    public static final String IS_INSTAGRAM = "isinstagram";

    public PreferenceUtil()
    {
    }

    public static boolean getIscamera(Context context)
    {
        return context.getSharedPreferences(context.getString(0x7f060023), 0).getBoolean("iscamera", true);
    }

    public static boolean getIsfacebook(Context context)
    {
        return context.getSharedPreferences(context.getString(0x7f060023), 0).getBoolean("isfacebook", true);
    }

    public static boolean getIsgallary(Context context)
    {
        return context.getSharedPreferences(context.getString(0x7f060023), 0).getBoolean("isgallary", true);
    }

    public static boolean getIsinstagram(Context context)
    {
        return context.getSharedPreferences(context.getString(0x7f060023), 0).getBoolean("isinstagram", true);
    }

    public static int getcountSelected(Context context)
    {
        return context.getSharedPreferences(context.getString(0x7f060023), 0).getInt("countSelected", 4);
    }

    public static void setIscamera(Context context, boolean flag)
    {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(0x7f060023), 0).edit();
        editor.putBoolean("iscamera", flag);
        editor.commit();
    }

    public static void setIsfacebook(Context context, boolean flag)
    {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(0x7f060023), 0).edit();
        editor.putBoolean("isfacebook", flag);
        editor.commit();
    }

    public static void setIsgallary(Context context, boolean flag)
    {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(0x7f060023), 0).edit();
        editor.putBoolean("isgallary", flag);
        editor.commit();
    }

    public static void setIsinstagram(Context context, boolean flag)
    {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(0x7f060023), 0).edit();
        editor.putBoolean("isinstagram", flag);
        editor.commit();
    }

    public static void setcountSelected(Context context, int i)
    {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(0x7f060023), 0).edit();
        editor.putInt("countSelected", i);
        editor.commit();
    }
}
