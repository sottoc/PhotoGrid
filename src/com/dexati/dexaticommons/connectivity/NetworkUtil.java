package com.dexati.dexaticommons.connectivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.widget.Toast;

import java.lang.reflect.Method;

import com.photoapp.picturegridbuilder.R;

public class NetworkUtil
{

    public NetworkUtil()
    {
    }

    @SuppressLint("NewApi")
	public static boolean isAirplaneEnabled(Context context)
    {
        if(android.os.Build.VERSION.SDK_INT >= 17){
        	if(android.provider.Settings.Global.getInt(context.getContentResolver(), "airplane_mode_on", 0) != 1)
                return false;
        	return true;
        }
        if(android.provider.Settings.System.getInt(context.getContentResolver(), "airplane_mode_on", 0) != 1)
        	return false;
        return true;
    }

    public static boolean isAutoBrightEnabled(Context context)
    {
        return android.provider.Settings.System.getInt(context.getContentResolver(), "screen_brightness_mode", 0) == 1;
    }

    public static boolean isAutoRotateEnabled(Context context)
    {
        return android.provider.Settings.System.getInt(context.getContentResolver(), "accelerometer_rotation", 0) == 1;
    }

    public static boolean isBlueToothEnabled()
    {
        BluetoothAdapter bluetoothadapter = BluetoothAdapter.getDefaultAdapter();
        boolean flag;
        if(bluetoothadapter.getState() == 12)
        {
            flag = true;
        } else
        {
            int i = bluetoothadapter.getState();
            flag = false;
            if(i == 10)
            {
                return false;
            }
        }
        return flag;
    }

    public static boolean isGPSEnabled(Context context)
    {
        return ((LocationManager)context.getSystemService("location")).isProviderEnabled("gps");
    }

    public static boolean isMobDataEnabled(Context context)
    {
        ConnectivityManager connectivitymanager = (ConnectivityManager)context.getSystemService("connectivity");
        boolean flag;
        try
        {
            Method method = Class.forName(connectivitymanager.getClass().getName()).getDeclaredMethod("getMobileDataEnabled", new Class[0]);
            method.setAccessible(true);
            flag = ((Boolean)method.invoke(connectivitymanager, new Object[0])).booleanValue();
        }
        catch(Exception exception)
        {
            return false;
        }
        return flag;
    }

    public static boolean isNFCEnabled(Context context)
    {
        NfcAdapter nfcadapter = ((NfcManager)context.getSystemService("nfc")).getDefaultAdapter();
        return nfcadapter != null && nfcadapter.isEnabled();
    }

    public static boolean isOnline(Context context)
    {
        NetworkInfo networkinfo = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
        if(networkinfo != null && networkinfo.isAvailable() && networkinfo.isConnected())
        {
            return true;
        } else
        {
            Toast.makeText(context, "No Internet", 0).show();
            return false;
        }
    }

    public static boolean isRingerEnabled(Context context)
    {
        return ((AudioManager)context.getSystemService("audio")).getRingerMode() == 2;
    }

    public static boolean isVibrateEnabled(Context context)
    {
        return ((AudioManager)context.getSystemService("audio")).getRingerMode() == 1;
    }

    public static boolean isWifiEnabled(Context context)
    {
        return ((WifiManager)context.getSystemService("wifi")).isWifiEnabled();
    }
}
