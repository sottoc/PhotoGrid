package com.km.photogridbuilder.shapecut;

import android.content.res.Resources;
import android.graphics.*;

public class ScalingUtilities
{
    public enum ScalingLogic
    {
    	CROP("CROP", 0),
        FIT("FIT", 1);
        static 
        {
            ScalingLogic ascalinglogic[] = new ScalingLogic[2];
            ascalinglogic[0] = CROP;
            ascalinglogic[1] = FIT;
        }

        private ScalingLogic(String s, int i)
        {
        }
    }


    public ScalingUtilities()
    {
    }

    public static Rect calculateDstRect(int i, int j, int k, int l, ScalingLogic scalinglogic)
    {
        if(scalinglogic == ScalingLogic.FIT)
        {
            float f = (float)i / (float)j;
            if(f > (float)k / (float)l)
            {
                return new Rect(0, 0, k, (int)((float)k / f));
            } else
            {
                return new Rect(0, 0, (int)(f * (float)l), l);
            }
        } else
        {
            return new Rect(0, 0, k, l);
        }
    }

    public static int calculateSampleSize(int i, int j, int k, int l, ScalingLogic scalinglogic)
    {
        if(scalinglogic == ScalingLogic.FIT)
        {
            if((float)i / (float)j > (float)k / (float)l)
            {
                return i / k;
            } else
            {
                return j / l;
            }
        }
        if((float)i / (float)j > (float)k / (float)l)
        {
            return j / l;
        } else
        {
            return i / k;
        }
    }

    public static Rect calculateSrcRect(int i, int j, int k, int l, ScalingLogic scalinglogic)
    {
        if(scalinglogic == ScalingLogic.CROP)
        {
            float f = (float)i / (float)j;
            float f1 = (float)k / (float)l;
            if(f > f1)
            {
                int k1 = (int)(f1 * (float)j);
                int l1 = (i - k1) / 2;
                return new Rect(l1, 0, l1 + k1, j);
            } else
            {
                int i1 = (int)((float)i / f1);
                int j1 = (j - i1) / 2;
                return new Rect(0, j1, i, j1 + i1);
            }
        } else
        {
            return new Rect(0, 0, i, j);
        }
    }

    public static Bitmap createScaledBitmap(Bitmap bitmap, int i, int j, ScalingLogic scalinglogic)
    {
        Rect rect = calculateSrcRect(bitmap.getWidth(), bitmap.getHeight(), i, j, scalinglogic);
        Rect rect1 = calculateDstRect(bitmap.getWidth(), bitmap.getHeight(), i, j, scalinglogic);
        Bitmap bitmap1 = Bitmap.createBitmap(rect1.width(), rect1.height(), android.graphics.Bitmap.Config.ARGB_8888);
        (new Canvas(bitmap1)).drawBitmap(bitmap, rect, rect1, new Paint(2));
        return bitmap1;
    }

    public static Bitmap decodeResource(Resources resources, int i, int j, int k, ScalingLogic scalinglogic)
    {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, i, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, j, k, scalinglogic);
        return BitmapFactory.decodeResource(resources, i, options);
    }
}
