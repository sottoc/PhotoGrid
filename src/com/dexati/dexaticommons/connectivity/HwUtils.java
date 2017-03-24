package com.dexati.dexaticommons.connectivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

public class HwUtils
{

    public HwUtils()
    {
    }

    public static boolean isBTAvailable(Context context)
    {
        return context.getPackageManager().hasSystemFeature("android.hardware.bluetooth");
    }

    public static boolean isGPSAvailable(Context context)
    {
        return context.getPackageManager().hasSystemFeature("android.hardware.location.gps");
    }

    public static boolean isSimAvailable(Context context)
    {
        switch(((TelephonyManager)context.getSystemService("phone")).getSimState())
        {
        default:
            return false;

        case 1: // '\001'
            return false;

        case 4: // '\004'
            return false;

        case 2: // '\002'
            return true;

        case 3: // '\003'
            return true;

        case 5: // '\005'
            return true;

        case 0: // '\0'
            return false;
        }
    }
}
