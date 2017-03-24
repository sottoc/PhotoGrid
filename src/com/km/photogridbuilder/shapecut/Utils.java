package com.km.photogridbuilder.shapecut;

import android.app.Activity;
import android.content.Context;
import android.content.pm.*;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils
{

    public Utils()
    {
    }

    public static int getDeviceHeight(Context context)
    {
        Point point = new Point();
        Display display = ((WindowManager)context.getSystemService("window")).getDefaultDisplay();
        if(android.os.Build.VERSION.SDK_INT >= 11)
        {
            display.getSize(point);
            return point.y;
        } else
        {
            return display.getWidth();
        }
    }

    public static int getDeviceWidth(Context context)
    {
        Point point = new Point();
        Display display = ((WindowManager)context.getSystemService("window")).getDefaultDisplay();
        if(android.os.Build.VERSION.SDK_INT >= 11)
        {
            display.getSize(point);
            return point.x;
        } else
        {
            return display.getWidth();
        }
    }

    public static String printKeyHash(Activity activity)
    {
        int i;
        String s;
        i = 0;
        s = null;
        Signature asignature[];
        int j;
        try{
	        String s1 = activity.getApplicationContext().getPackageName();
	        PackageInfo packageinfo = activity.getPackageManager().getPackageInfo(s1, 64);
	        Log.e("Package Name=", activity.getApplicationContext().getPackageName());
	        asignature = packageinfo.signatures;
	        j = asignature.length;
	        String s2 = null;
	        while(i < j){
	        	Signature signature = asignature[i];
	            MessageDigest messagedigest = MessageDigest.getInstance("SHA");
	            messagedigest.update(signature.toByteArray());
	            s = new String(Base64.encode(messagedigest.digest(), 0));
	            Log.e("Key Hash=", s);
	            i++;
	            s2 = s;
	        }
	        return s2;
        }catch(Exception e){}
        return null;
    }

    public static boolean saveImage(Bitmap bitmap, int i, int j, String s)
    {
        File file = new File((new StringBuilder()).append(Environment.getExternalStorageDirectory()).append("/").append("PhotoShapeCut").append("/").toString());
        if(!file.exists())
        {
            file.mkdirs();
        }
        File file1 = new File(file, (new StringBuilder(String.valueOf(s))).append(".png").toString());
        try
        {
            FileOutputStream fileoutputstream = new FileOutputStream(file1);
            bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, fileoutputstream);
            fileoutputstream.flush();
            fileoutputstream.close();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return false;
        }
        return true;
    }
}
