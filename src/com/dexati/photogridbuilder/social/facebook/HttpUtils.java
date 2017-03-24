package com.dexati.photogridbuilder.social.facebook;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HttpUtils
{

    public HttpUtils()
    {
    }

    public static boolean isNetworkAvail(Context context)
    {
        NetworkInfo networkinfo = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
        return networkinfo != null && networkinfo.isConnected();
    }
}
