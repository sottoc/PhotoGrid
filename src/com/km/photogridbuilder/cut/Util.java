package com.km.photogridbuilder.cut;

import android.content.ContentResolver;
import android.graphics.*;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import java.io.Closeable;
import java.io.IOException;

public class Util
{

    private static final String TAG = "db.Util";
    private static android.view.View.OnClickListener sNullOnClickListener;

    private Util()
    {
    }

    public static void Assert(boolean flag)
    {
        if(!flag)
        {
            throw new AssertionError();
        } else
        {
            return;
        }
    }

    public static void closeSilently(ParcelFileDescriptor parcelfiledescriptor)
    {
        if(parcelfiledescriptor == null)
        {
            return;
        }
        try
        {
            parcelfiledescriptor.close();
            return;
        }
        catch(Throwable throwable)
        {
            return;
        }
    }

    public static void closeSilently(Closeable closeable)
    {
        if(closeable == null)
        {
            return;
        }
        try
        {
            closeable.close();
            return;
        }
        catch(Throwable throwable)
        {
            return;
        }
    }

    public static android.graphics.BitmapFactory.Options createNativeAllocOptions()
    {
        return new android.graphics.BitmapFactory.Options();
    }

    public static Bitmap createVideoThumbnail(String s)
    {
        return null;
    }

    public static void debugWhere(String s, String s1)
    {
        Log.d(s, (new StringBuilder(String.valueOf(s1))).append(" --- stack trace begins: ").toString());
        StackTraceElement astacktraceelement[] = Thread.currentThread().getStackTrace();
        int i = 3;
        int j = astacktraceelement.length;
        do
        {
            if(i >= j)
            {
                Log.d(s, (new StringBuilder(String.valueOf(s1))).append(" --- stack trace ends.").toString());
                return;
            }
            StackTraceElement stacktraceelement = astacktraceelement[i];
            Object aobj[] = new Object[4];
            aobj[0] = stacktraceelement.getClassName();
            aobj[1] = stacktraceelement.getMethodName();
            aobj[2] = stacktraceelement.getFileName();
            aobj[3] = Integer.valueOf(stacktraceelement.getLineNumber());
            Log.d(s, String.format("    at %s.%s(%s:%s)", aobj));
            i++;
        } while(true);
    }

    public static boolean equals(String s, String s1)
    {
        return s == s1 || s.equals(s1);
    }

    public static Bitmap extractMiniThumb(Bitmap bitmap, int i, int j)
    {
        return extractMiniThumb(bitmap, i, j, true);
    }

    public static Bitmap extractMiniThumb(Bitmap bitmap, int i, int j, boolean flag)
    {
        Bitmap bitmap1;
        if(bitmap == null)
        {
            bitmap1 = null;
        } else
        {
            float f;
            Matrix matrix;
            if(bitmap.getWidth() < bitmap.getHeight())
            {
                f = (float)i / (float)bitmap.getWidth();
            } else
            {
                f = (float)j / (float)bitmap.getHeight();
            }
            matrix = new Matrix();
            matrix.setScale(f, f);
            bitmap1 = transform(matrix, bitmap, i, j, false);
            if(flag && bitmap1 != bitmap)
            {
                bitmap.recycle();
                return bitmap1;
            }
        }
        return bitmap1;
    }

    public static android.view.View.OnClickListener getNullOnClickListener()
    {
    	try{
	        synchronized(com.km.photogridbuilder.cut.Util.class){
	        	android.view.View.OnClickListener onclicklistener;
	            if(sNullOnClickListener == null)
	            {
	                sNullOnClickListener = new android.view.View.OnClickListener() {
	
	                    public void onClick(View view)
	                    {
	                    }
	
	                };
	            }
	            onclicklistener = sNullOnClickListener;
	            return onclicklistener;
	        }
    	}catch(Exception e){
    		return null;
    	}
    }

    public static int indexOf(Object aobj[], Object obj)
    {
        int i = 0;
        while(i < aobj.length){
        	if(aobj[i].equals(obj))
            	return i;
            i++;
        }
        i = -1;
        return i;
    }

    private static ParcelFileDescriptor makeInputStream(Uri uri, ContentResolver contentresolver)
    {
        ParcelFileDescriptor parcelfiledescriptor;
        try
        {
            parcelfiledescriptor = contentresolver.openFileDescriptor(uri, "r");
        }
        catch(IOException ioexception)
        {
            return null;
        }
        return parcelfiledescriptor;
    }

    public static Bitmap rotate(Bitmap bitmap, int i)
    {
        if(i == 0 || bitmap == null)
        {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(i, (float)bitmap.getWidth() / 2.0F, (float)bitmap.getHeight() / 2.0F);
        Bitmap bitmap1;
        try
        {
            int j = bitmap.getWidth();
            int k = bitmap.getHeight();
            bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, j, k, matrix, true);
        }
        catch(OutOfMemoryError outofmemoryerror)
        {
            return bitmap;
        }
        if(bitmap == bitmap1)
        {
            return bitmap;
        }
        bitmap.recycle();
        bitmap = bitmap1;
        return bitmap;
    }

    public static Bitmap transform(Matrix matrix, Bitmap bitmap, int i, int j, boolean flag)
    {
    	int i1;
        int j1;
        int k = bitmap.getWidth() - i;
        int l = bitmap.getHeight() - j;
        Bitmap bitmap2;
        if(!flag && (k < 0 || l < 0))
        {
            bitmap2 = Bitmap.createBitmap(i, j, android.graphics.Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap2);
            int k1 = Math.max(0, k / 2);
            int l1 = Math.max(0, l / 2);
            Rect rect = new Rect(k1, l1, k1 + Math.min(i, bitmap.getWidth()), l1 + Math.min(j, bitmap.getHeight()));
            int i2 = (i - rect.width()) / 2;
            int j2 = (j - rect.height()) / 2;
            Rect rect1 = new Rect(i2, j2, i - i2, j - j2);
            canvas.drawBitmap(bitmap, rect, rect1, null);
        } else
        {
            float f = bitmap.getWidth();
            float f1 = bitmap.getHeight();
            Bitmap bitmap1;
            if(f / f1 > (float)i / (float)j)
            {
                float f3 = (float)j / f1;
                if(f3 < 0.9F || f3 > 1.0F)
                {
                    matrix.setScale(f3, f3);
                } else
                {
                    matrix = null;
                }
            } else
            {
                float f2 = (float)i / f;
                if(f2 < 0.9F || f2 > 1.0F)
                {
                    matrix.setScale(f2, f2);
                } else
                {
                    matrix = null;
                }
            }
            if(matrix != null)
            {
                bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            } else
            {
                bitmap1 = bitmap;
            }
            i1 = Math.max(0, bitmap1.getWidth() - i);
            j1 = Math.max(0, bitmap1.getHeight() - j);
            bitmap2 = Bitmap.createBitmap(bitmap1, i1 / 2, j1 / 2, i, j);
            if(bitmap1 != bitmap)
            {
                bitmap1.recycle();
                return bitmap2;
            }
        }
        return bitmap2;
    }
}
