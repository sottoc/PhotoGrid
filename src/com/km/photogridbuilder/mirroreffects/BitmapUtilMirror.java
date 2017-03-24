package com.km.photogridbuilder.mirroreffects;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.*;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class BitmapUtilMirror
{

    public BitmapUtilMirror()
    {
    }

    public static int calculateInSampleSize(android.graphics.BitmapFactory.Options options, int i, int j, int k)
    {
        int l;
        int i1;
        int j1;
        int k1;
        l = 8;
        i1 = options.outHeight;
        j1 = options.outWidth;
        k1 = 1;
        if(i1 <= i && j1 <= j || k != l && k != 6){
        	if(i1 > j || j1 > i)
            {
                int j2 = Math.round((float)i1 / (float)j);
                int k2 = Math.round((float)j1 / (float)i);
                if(j2 < k2)
                {
                    k1 = j2;
                } else
                {
                    k1 = k2;
                }
            }
        }else{
	        int l1 = Math.round((float)i1 / (float)i);
	        int i2 = Math.round((float)j1 / (float)j);
	        if(l1 < i2)
	        {
	            k1 = l1;
	        } else
	        {
	            k1 = i2;
	        }
        }
        if(k1 <= 16){
        	if(k1 > l)
            	return l;
            if(k1 > 4)
            {
                return 4;
            }
            if(k1 <= 2)
            	return k1;
            return 2;
        }
        k1 = 16;
        l = k1;
        return l;
    }

    public static Bitmap convertToMutable(Bitmap bitmap)
    {
        try
        {
            File file = new File((new StringBuilder()).append(Environment.getExternalStorageDirectory()).append(File.separator).append("temp.tmp").toString());
            RandomAccessFile randomaccessfile = new RandomAccessFile(file, "rw");
            int i = bitmap.getWidth();
            int j = bitmap.getHeight();
            android.graphics.Bitmap.Config config = bitmap.getConfig();
            FileChannel filechannel = randomaccessfile.getChannel();
            MappedByteBuffer mappedbytebuffer = filechannel.map(java.nio.channels.FileChannel.MapMode.READ_WRITE, 0L, j * bitmap.getRowBytes());
            bitmap.copyPixelsToBuffer(mappedbytebuffer);
            bitmap.recycle();
            System.gc();
            bitmap = Bitmap.createBitmap(i, j, config);
            mappedbytebuffer.position(0);
            bitmap.copyPixelsFromBuffer(mappedbytebuffer);
            filechannel.close();
            randomaccessfile.close();
            file.delete();
        }
        catch(FileNotFoundException filenotfoundexception)
        {
            filenotfoundexception.printStackTrace();
            return bitmap;
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
            return bitmap;
        }
        return bitmap;
    }

    public static Bitmap fitToViewByRect(Bitmap bitmap, int i, int j)
    {
        RectF rectf = new RectF(0.0F, 0.0F, bitmap.getWidth(), bitmap.getHeight());
        RectF rectf1 = new RectF(0.0F, 0.0F, i, j);
        Matrix matrix = new Matrix();
        matrix.setRectToRect(rectf, rectf1, android.graphics.Matrix.ScaleToFit.CENTER);
        Bitmap bitmap1 = Bitmap.createBitmap(i, j, android.graphics.Bitmap.Config.ARGB_8888);
        (new Canvas(bitmap1)).drawBitmap(bitmap, matrix, null);
        if(bitmap != null)
        {
            bitmap.recycle();
        }
        return bitmap1;
    }

    public static Bitmap fitToViewByScale(Bitmap bitmap, int i, int j)
    {
        Bitmap bitmap1 = Bitmap.createBitmap(i, j, android.graphics.Bitmap.Config.ARGB_8888);
        float f = bitmap.getWidth();
        float f1 = bitmap.getHeight();
        Canvas canvas = new Canvas(bitmap1);
        float f2 = (float)i / f;
        float f3 = ((float)j - f1 * f2) / 2.0F;
        Matrix matrix = new Matrix();
        matrix.postTranslate(0.0F, f3);
        matrix.preScale(f2, f2);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, matrix, paint);
        return bitmap1;
    }

    public static Bitmap getBitmap(Uri uri, Context context)
    {
        InputStream inputstream;
        Bitmap bitmap;
        inputstream = null;
        bitmap = null;
        
        try
        {
        	File file;
            FileOutputStream fileoutputstream;
            byte abyte0[];
            inputstream = context.getContentResolver().openInputStream(uri);
            file = new File((new StringBuilder()).append(Environment.getExternalStorageDirectory()).append(File.separator).append("tempfile.tmp").toString());
            fileoutputstream = new FileOutputStream(file);
            abyte0 = new byte[1024];
            while(true){
    	        int i = inputstream.read(abyte0);
    	        if(i == -1)
    	        	break;
    	        fileoutputstream.write(abyte0, 0, i);
            }
            int j = (new ExifInterface(file.getAbsolutePath())).getAttributeInt("Orientation", 1);
            android.graphics.BitmapFactory.Options options;
            android.graphics.BitmapFactory.Options options1;
            Bitmap bitmap1;
            if(j == 6 || j == 3 || j != 8);
            if(fileoutputstream != null)
            {
            	fileoutputstream.close();
            }
            options = new android.graphics.BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeStream(inputstream, null, options);
            options1 = new android.graphics.BitmapFactory.Options();
            if(options.outHeight > 1000 || options.outWidth > 1000)
            {
                options1.inSampleSize = 4;
            }
            inputstream = context.getContentResolver().openInputStream(uri);
            bitmap1 = BitmapFactory.decodeStream(inputstream, null, options1);
            inputstream.close();
            return bitmap1;
        }
        catch(Exception exception5) { 
        	return null;
        }
    }

    public static Bitmap getBitmapFromPath(String s, DisplayMetrics displaymetrics)
    {
        FileInputStream fileinputstream;
        android.graphics.BitmapFactory.Options options1;
        FileInputStream fileinputstream1;
        Bitmap bitmap;
        try
        {
            fileinputstream = new FileInputStream(s);
        }
        catch(FileNotFoundException filenotfoundexception1)
        {
            return null;
        }
        try
        {
            android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(fileinputstream, null, options);
            options1 = new android.graphics.BitmapFactory.Options();
            if(options.outHeight > displaymetrics.heightPixels || options.outWidth > displaymetrics.widthPixels)
            {
                options1.inSampleSize = 2;
            }
            fileinputstream1 = new FileInputStream(s);
        }
        catch(FileNotFoundException filenotfoundexception)
        {
            return null;
        }
        bitmap = BitmapFactory.decodeStream(fileinputstream1, null, options1);
        return bitmap;
    }

    public static Bitmap getBitmapFromUri(Context context, int i, int j, boolean flag, Uri uri, String s)
    {
        int k;
        String s1 = s;
        android.graphics.BitmapFactory.Options options;
        int l;
        if(s1 == null)
        {
            if(flag)
            {
                String as[] = {
                    "_data"
                };
                Cursor cursor = context.getContentResolver().query(uri, as, null, null, null);
                if(cursor.moveToFirst())
                {
                    s1 = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                }
                cursor.close();
            } else
            {
                s1 = uri.getPath();
            }
        }
        options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try{
	        BitmapFactory.decodeFile(s1, options);
	        l = (new ExifInterface(s1)).getAttributeInt("Orientation", 1);
	        k = l;
        }catch(IOException ioexception){
        	k = 0;
        }
        options.inSampleSize = calculateInSampleSize(options, i, j, k);
        options.inJustDecodeBounds = false;
        return rotateImage(BitmapFactory.decodeFile(s1, options), s1);
    }

    public static Bitmap getBitmapFromUriWithoutScale(Context context, boolean flag, Uri uri, String s)
    {
        String s1 = s;
        if(s1 == null)
        {
            if(flag)
            {
                String as[] = {
                    "_data"
                };
                Cursor cursor = context.getContentResolver().query(uri, as, null, null, null);
                if(cursor.moveToFirst())
                {
                    s1 = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                }
                cursor.close();
            } else
            {
                s1 = uri.getPath();
            }
        }
        return rotateImage(BitmapFactory.decodeFile(s1), s1);
    }

    public static Bitmap getImageFromSDCard(String s)
    {
        try
        {
        	FileInputStream fileinputstream = null;
            FileInputStream fileinputstream1 = new FileInputStream(s);
            android.graphics.BitmapFactory.Options options1;
            android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(fileinputstream1, null, options);
            options1 = new android.graphics.BitmapFactory.Options();
            fileinputstream = new FileInputStream(s);
            Bitmap bitmap;
            bitmap = BitmapFactory.decodeStream(fileinputstream, null, options1);
            fileinputstream.close();
            fileinputstream.close();
            return bitmap;
        }
        catch(IOException ioexception4)
        {
            ioexception4.printStackTrace();
            return null;
        }
    }

    public static String getPath(Context context, Uri uri)
    {
        String as[] = {
            "_data"
        };
        Cursor cursor = context.getContentResolver().query(uri, as, null, null, null);
        if(cursor == null)
        {
            return null;
        } else
        {
            int i = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            return cursor.getString(i);
        }
    }

    public static String getRealPathFromURI(Context context, Uri uri)
    {
        Cursor cursor = null;
        String s1;
        String as[] = {
            "_data"
        };
        try{
	        cursor = context.getContentResolver().query(uri, as, null, null, null);
	        if(cursor != null){
	        	String s2;
	            int i = cursor.getColumnIndexOrThrow("_data");
	            cursor.moveToFirst();
	            s2 = cursor.getString(i);
	            s1 = s2;
	            if(cursor != null)
	            	cursor.close();
	        }else{
		        String s = uri.getPath();
		        s1 = s;
		        if(cursor != null)
		        {
		            cursor.close();
		        }
	        }
	        return s1;
        }catch(Exception e){
        	return null;
        }
    }

    public static Bitmap rotateImage(Bitmap bitmap, String s)
    {
        int i;
        Matrix matrix;
        float f;
        Bitmap bitmap1;
        try
        {
            i = (new ExifInterface(s)).getAttributeInt("Orientation", 1);
            matrix = new Matrix();
        }
        catch(Exception exception)
        {
            return bitmap;
        }
        if(i != 6){
        	if(i == 3)
            {
                f = 180F;
            } else
            {
                f = 0.0F;
                if(i == 8)
                {
                    f = 270F;
                }
            }
        }else
        	f = 90F;
        if(f == 0.0F)
        {
            return bitmap;
        }
        matrix.postRotate(f);
        int j = bitmap.getWidth();
        int k = bitmap.getHeight();
        bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, j, k, matrix, true);
        bitmap.recycle();
        bitmap = bitmap1;
        return bitmap;
    }

    public static Bitmap scaleToFill(Bitmap bitmap, int i, int j)
    {
        float f = (float)j / (float)bitmap.getWidth();
        float f1 = (float)i / (float)bitmap.getWidth();
        float f2;
        if(f > f1)
        {
            f2 = f1;
        } else
        {
            f2 = f;
        }
        return Bitmap.createScaledBitmap(bitmap, (int)(f2 * (float)bitmap.getWidth()), (int)(f2 * (float)bitmap.getHeight()), false);
    }

    public static Bitmap scaleToFitHeight(Bitmap bitmap, int i)
    {
        return Bitmap.createScaledBitmap(bitmap, (int)(((float)i / (float)bitmap.getHeight()) * (float)bitmap.getWidth()), i, false);
    }

    public static Bitmap scaleToFitWidth(Bitmap bitmap, int i)
    {
        return Bitmap.createScaledBitmap(bitmap, i, (int)(((float)i / (float)bitmap.getWidth()) * (float)bitmap.getHeight()), false);
    }
}
