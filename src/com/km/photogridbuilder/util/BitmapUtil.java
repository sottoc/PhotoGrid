package com.km.photogridbuilder.util;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.*;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import java.io.File;
import java.io.FileNotFoundException;

// Referenced classes of package com.km.photogridbuilder.util:
//            IOUtilities

public class BitmapUtil
{

    private static final String TAG = "retro";

    public BitmapUtil()
    {
    }

    public static int calculateInSampleSize(android.graphics.BitmapFactory.Options options, int i, int j)
    {
        int k;
        int l;
        int i1;
        k = options.outHeight;
        l = options.outWidth;
        i1 = 1;
        if(k <= j && l <= i)
        	return i1;
        float f;
        float f1;
        if(l > k)
        {
            i1 = Math.round((float)k / (float)j);
        } else
        {
            i1 = Math.round((float)l / (float)i);
        }
        f = l * k;
        f1 = 2 * (i * j);
        while(f / (float)(i1 * i1) > f1){
        	i1++;
        }
        return i1;
    }

    public static Bitmap getBitmap(Context context, String s, int i, int j)
    {
        Uri uri;
        java.io.InputStream inputstream;
        Bitmap bitmap;
        Matrix matrix;
        uri = getImageUri(s);
        inputstream = null;
        bitmap = null;
        try{
	        matrix = new Matrix();
	        int l = (new ExifInterface(s)).getAttributeInt("Orientation", 1);
	        int k = 90;
	        if(l != 6){
	        	if(l == 3)
	            {
	                k = 180;
	            } else
	            {
	                k = 0;
	                if(l == 8)
	                {
	                    k = 270;
	                }
	            }
	        }
	        matrix.postRotate(k);
	        inputstream = context.getContentResolver().openInputStream(uri);
	        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        bitmap = BitmapFactory.decodeStream(inputstream, null, options);
	        options.inSampleSize = calculateInSampleSize(options, i, j);
	        options.inSampleSize = (int)(1.5F * (float)options.inSampleSize);
	        inputstream = context.getContentResolver().openInputStream(uri);
	        options.inJustDecodeBounds = false;
	        bitmap = BitmapFactory.decodeStream(inputstream, null, options);
	        if(k == 0)
	        {
	            return bitmap;
	        }
	        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	        bitmap = bitmap1;
	        IOUtilities.closeStream(inputstream);
        }catch(Exception e){}
        return bitmap;
    }

    private static Uri getImageUri(String s)
    {
        return Uri.fromFile(new File(s));
    }

    private static int getRadious(float f, float f1, int i, int j)
    {
        Rect rect = new Rect(0, 0, i, j);
        float f2 = f - (float)rect.centerX();
        double d = Math.toDegrees(Math.atan((f1 - (float)rect.centerY()) / f2));
        int k = Math.max(rect.height() / 2, rect.width() / 2);
        int l = Math.min(rect.height() / 2, rect.width() / 2);
        double d1 = Math.sqrt((double)(l * l) * (Math.cos(d) * Math.cos(d)) + (double)(k * k) * (Math.sin(d) * Math.sin(d)));
        int i1 = (int)((double)(k * l) / d1);
        int j1 = 0;
        do
        {
            if(j1 >= 361)
            {
                return i1;
            }
            double d2 = Math.sqrt((double)(l * l) * (Math.cos(j1) * Math.cos(j1)) + (double)(k * k) * (Math.sin(j1) * Math.sin(j1)));
            Log.e("retro", (new StringBuilder("radius: ")).append(d2).append("(").append(j1).append(")").toString());
            j1++;
        } while(true);
    }

    public static int getScreenWidth(Context context)
    {
        return ((WindowManager)context.getSystemService("window")).getDefaultDisplay().getWidth();
    }

    public static void setFramePaint(Paint paint, int i, int j)
    {
        float _tmp = (float)(i / 20);
        int _tmp1 = 4 * (Math.max(i, j) / 20);
        float f;
        if(i > j)
        {
            f = 0.01F * (float)i;
        } else
        {
            f = 0.01F * (float)j;
        }
        float _tmp2 = f;
        getRadious(50F, 50F, i, j);
    }
}
