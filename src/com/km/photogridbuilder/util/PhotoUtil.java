package com.km.photogridbuilder.util;

import android.graphics.*;
import android.util.Log;

public class PhotoUtil
{

    public PhotoUtil()
    {
    }

    public static Bitmap changeToOld(Bitmap bitmap)
        throws OutOfMemoryError
    {
        int i;
        int j;
        Bitmap bitmap1;
        int ai[];
        int k;
        i = bitmap.getWidth();
        j = bitmap.getHeight();
        bitmap1 = Bitmap.createBitmap(i, j, android.graphics.Bitmap.Config.RGB_565);
        ai = new int[i * j];
        bitmap.getPixels(ai, 0, i, 0, 0, i, j);
        k = 0;
        while(true){
	        if(k >= j)
	        {
	            bitmap1.setPixels(ai, 0, i, 0, 0, i, j);
	            return bitmap1;
	        }
	        int l = 0;
	        while(l < i)
            {
            	int i1 = ai[l + i * k];
	            int j1 = Color.red(i1);
	            int k1 = Color.green(i1);
	            int l1 = Color.blue(i1);
	            int i2 = (int)(0.39300000000000002D * (double)j1 + 0.76900000000000002D * (double)k1 + 0.189D * (double)l1);
	            int j2 = (int)(0.34899999999999998D * (double)j1 + 0.68600000000000005D * (double)k1 + 0.16800000000000001D * (double)l1);
	            int k2 = (int)(0.27200000000000002D * (double)j1 + 0.53400000000000003D * (double)k1 + 0.13100000000000001D * (double)l1);
	            int l2;
	            int i3;
	            int j3;
	            int k3;
	            if(i2 > 255)
	            {
	                l2 = 255;
	            } else
	            {
	                l2 = i2;
	            }
	            if(j2 > 255)
	            {
	                i3 = 255;
	            } else
	            {
	                i3 = j2;
	            }
	            if(k2 > 255)
	            {
	                j3 = 255;
	            } else
	            {
	                j3 = k2;
	            }
	            k3 = Color.argb(255, l2, i3, j3);
	            ai[l + i * k] = k3;
	            l++;
            }
            k++;
    	}
    }

    public static Bitmap createSepiaToningEffect(Bitmap bitmap, int i, double d, double d1, double d2)
    {
        int j;
        int k;
        Bitmap bitmap1;
        int l;
        j = bitmap.getWidth();
        k = bitmap.getHeight();
        Log.v("bitmap", (new StringBuilder("src ")).append(bitmap).toString());
        bitmap1 = Bitmap.createBitmap(j, k, android.graphics.Bitmap.Config.ARGB_8888);
        l = 0;
        while(true){
	        if(l >= j)
	        {
	            return bitmap1;
	        }
	        int i1 = 0;
	        while(i1 < k)
	        {
	        	int j1 = bitmap.getPixel(l, i1);
	            int k1 = Color.alpha(j1);
	            int l1 = Color.red(j1);
	            int i2 = Color.green(j1);
	            int j2 = Color.blue(j1);
	            int k2 = (int)(0.29999999999999999D * (double)l1 + 0.58999999999999997D * (double)i2 + 0.11D * (double)j2);
	            int l2 = (int)((double)k2 + d * (double)i);
	            if(l2 > 255)
	            {
	                l2 = 255;
	            }
	            int i3 = (int)((double)k2 + d1 * (double)i);
	            if(i3 > 255)
	            {
	                i3 = 255;
	            }
	            int j3 = (int)((double)k2 + d2 * (double)i);
	            if(j3 > 255)
	            {
	                j3 = 255;
	            }
	            int k3 = Color.argb(k1, l2, i3, j3);
	            bitmap1.setPixel(l, i1, k3);
	            i1++;
	        }
	        l++;
        }
    }

    public static Bitmap toBlue(Bitmap bitmap)
    {
        return createSepiaToningEffect(bitmap, 40, 1.2D, 0.87D, 2.1000000000000001D);
    }

    public static Bitmap toGray(Bitmap bitmap)
    {
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap1);
        Paint paint = new Paint(1);
        ColorMatrix colormatrix = new ColorMatrix();
        colormatrix.setSaturation(0.0F);
        paint.setColorFilter(new ColorMatrixColorFilter(colormatrix));
        canvas.drawBitmap(bitmap, 0.0F, 0.0F, paint);
        return bitmap1;
    }

    public static Bitmap toGreen(Bitmap bitmap)
    {
        return createSepiaToningEffect(bitmap, 40, 0.88D, 2.4500000000000002D, 1.4299999999999999D);
    }

    public static Bitmap toRed(Bitmap bitmap)
    {
        return createSepiaToningEffect(bitmap, 40, 1.5D, 0.59999999999999998D, 0.12D);
    }
}
