package com.km.photogridbuilder.mirroreffects;

import android.graphics.*;

public class MirrorEffects
{

    public MirrorEffects()
    {
    }

    public static Bitmap get4DMirrored(Bitmap bitmap)
    {
        Bitmap bitmap1;
        Bitmap bitmap2;
        try
        {
            bitmap1 = getRightMirrored(bitmap);
            bitmap2 = Bitmap.createScaledBitmap(getBottomMirrored(bitmap1), bitmap.getWidth(), bitmap.getHeight(), true);
        }
        catch(OutOfMemoryError outofmemoryerror)
        {
            return bitmap;
        }
        if(bitmap1 != null)
        {
        	if(!bitmap1.isRecycled())
            {
                bitmap1.recycle();
            }
        }
        return bitmap2;
    }

    public static Bitmap getBottomMirrored(Bitmap bitmap)
    {
        Bitmap bitmap1;
        Bitmap bitmap2;
        try
        {
            int i = bitmap.getWidth();
            int j = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.preScale(1.0F, -1F);
            bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, i, j, matrix, false);
            bitmap2 = Bitmap.createBitmap(i, j * 2, android.graphics.Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bitmap2);
            canvas.drawBitmap(bitmap, 0.0F, 0.0F, null);
            canvas.drawBitmap(bitmap1, 0.0F, j - 4, null);
            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));
            canvas.drawRect(0.0F, j, i, -4 + bitmap2.getHeight(), paint);
        }
        catch(OutOfMemoryError outofmemoryerror)
        {
            return bitmap;
        }
        if(bitmap1 != null)
        {
        	if(bitmap1.isRecycled())
            {
                bitmap1.recycle();
            }
        }
        if(bitmap != null)
        {
        	if(bitmap.isRecycled())
            {
                bitmap.recycle();
            }
        }
        return bitmap2;
    }

    public static Bitmap getInvert4DMirrored(Bitmap bitmap)
    {
        Bitmap bitmap1;
        Bitmap bitmap2;
        try
        {
            bitmap1 = getTopMirrored(bitmap);
            bitmap2 = Bitmap.createScaledBitmap(getRightMirrored(bitmap1), bitmap.getWidth(), bitmap.getHeight(), true);
        }
        catch(OutOfMemoryError outofmemoryerror)
        {
            return bitmap;
        }
        if(bitmap1 != null)
        {
        	if(!bitmap1.isRecycled())
            {
                bitmap1.recycle();
            }
        }
        return bitmap2;
    }

    public static Bitmap getLeftHalfMirrored(Bitmap bitmap)
    {
        Bitmap bitmap1;
        Bitmap bitmap2;
        Bitmap bitmap3;
        try
        {
            int i = bitmap.getWidth();
            int j = bitmap.getHeight();
            bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, i / 2, j, null, false);
            Matrix matrix = new Matrix();
            matrix.preScale(-1F, 1.0F);
            bitmap2 = Bitmap.createBitmap(bitmap1, 0, 0, i / 2, j, matrix, false);
            bitmap3 = Bitmap.createBitmap(i, j, android.graphics.Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bitmap3);
            canvas.drawBitmap(bitmap1, 0.0F, 0.0F, null);
            canvas.drawBitmap(bitmap2, -4 + bitmap1.getWidth(), 0.0F, null);
            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));
            canvas.drawRect(0.0F, j, i, -4 + bitmap3.getHeight(), paint);
        }
        catch(OutOfMemoryError outofmemoryerror)
        {
            return bitmap;
        }
        if(bitmap2 != null)
        {
        	if(bitmap2.isRecycled())
            {
                bitmap2.recycle();
            }
        }
        if(bitmap1 != null)
        {
        	if(bitmap1.isRecycled())
            {
                bitmap1.recycle();
            }
        }
        if(bitmap != null)
        {
        	if(bitmap.isRecycled())
            {
                bitmap.recycle();
            }
        }
        return bitmap3;
    }

    public static Bitmap getLeftMirrored(Bitmap bitmap)
    {
        Bitmap bitmap1;
        Bitmap bitmap2;
        try
        {
            int i = bitmap.getWidth();
            int j = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.preScale(-1F, 1.0F);
            bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, i, j, matrix, false);
            bitmap2 = Bitmap.createBitmap(i * 2, j, android.graphics.Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bitmap2);
            canvas.drawBitmap(bitmap, i - 4, 0.0F, null);
            canvas.drawBitmap(bitmap1, 0.0F, 0.0F, null);
            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));
            canvas.drawRect(0.0F, j, i, -4 + bitmap2.getHeight(), paint);
        }
        catch(OutOfMemoryError outofmemoryerror)
        {
            return bitmap;
        }
        if(bitmap1 != null)
        {
        	if(bitmap1.isRecycled())
            {
                bitmap1.recycle();
            }
        }
        if(bitmap != null)
        {
        	if(bitmap.isRecycled())
            {
                bitmap.recycle();
            }
        }
        return bitmap2;
    }

    public static Bitmap getRightHalfMirrored(Bitmap bitmap)
    {
        Bitmap bitmap1;
        Bitmap bitmap2;
        Bitmap bitmap3;
        try
        {
            int i = bitmap.getWidth();
            int j = bitmap.getHeight();
            bitmap1 = Bitmap.createBitmap(bitmap, i / 2, 0, i / 2, j, null, false);
            Matrix matrix = new Matrix();
            matrix.preScale(-1F, 1.0F);
            bitmap2 = Bitmap.createBitmap(bitmap1, 0, 0, i / 2, j, matrix, false);
            bitmap3 = Bitmap.createBitmap(i, j, android.graphics.Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bitmap3);
            canvas.drawBitmap(bitmap1, -4 + bitmap1.getWidth(), 0.0F, null);
            canvas.drawBitmap(bitmap2, 0.0F, 0.0F, null);
            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));
            canvas.drawRect(0.0F, j, i, -4 + bitmap3.getHeight(), paint);
        }
        catch(OutOfMemoryError outofmemoryerror)
        {
            return bitmap;
        }
        if(bitmap2 != null)
        {
        	if(bitmap2.isRecycled())
            {
                bitmap2.recycle();
            }
        }
        if(bitmap1 != null)
        {
        	if(bitmap1.isRecycled())
            {
                bitmap1.recycle();
            }
        }
        if(bitmap != null)
        {
        	if(bitmap.isRecycled())
            {
                bitmap.recycle();
            }
        }
        return bitmap3;
    }

    public static Bitmap getRightMirrored(Bitmap bitmap)
    {
        Bitmap bitmap1;
        Bitmap bitmap2;
        try
        {
            int i = bitmap.getWidth();
            int j = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.preScale(-1F, 1.0F);
            bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, i, j, matrix, false);
            bitmap2 = Bitmap.createBitmap(i * 2, j, android.graphics.Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bitmap2);
            canvas.drawBitmap(bitmap, 0.0F, 0.0F, null);
            canvas.drawBitmap(bitmap1, i - 4, 0.0F, null);
            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));
            canvas.drawRect(0.0F, j, i, -4 + bitmap2.getHeight(), paint);
        }
        catch(OutOfMemoryError outofmemoryerror)
        {
            return bitmap;
        }
        if(bitmap1 != null)
        {
        	if(bitmap1.isRecycled())
            {
                bitmap1.recycle();
            }
        }
        if(bitmap != null)
        {
        	if(bitmap.isRecycled())
            {
                bitmap.recycle();
            }
        }
        return bitmap2;
    }

    public static Bitmap getTopMirrored(Bitmap bitmap)
    {
        Bitmap bitmap1;
        Bitmap bitmap2;
        try
        {
            int i = bitmap.getWidth();
            int j = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.preScale(1.0F, -1F);
            bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, i, j, matrix, false);
            bitmap2 = Bitmap.createBitmap(i, j * 2, android.graphics.Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bitmap2);
            canvas.drawBitmap(bitmap, 0.0F, j - 4, null);
            canvas.drawBitmap(bitmap1, 0.0F, 0.0F, null);
            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));
            canvas.drawRect(0.0F, j, i, -4 + bitmap2.getHeight(), paint);
        }
        catch(OutOfMemoryError outofmemoryerror)
        {
            return bitmap;
        }
        if(bitmap1 != null)
        {
        	if(bitmap1.isRecycled())
            {
                bitmap1.recycle();
            }
        }
        if(bitmap != null)
        {
        	if(bitmap.isRecycled())
            {
                bitmap.recycle();
            }
        }
        return bitmap2;
    }
}
