package com.km.photogridbuilder.util;

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

public class BitmapUtil2
{

    public BitmapUtil2()
    {
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
        File file;
        FileOutputStream fileoutputstream;
        byte abyte0[];
        try
        {
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
            	fileoutputstream.close();
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
        catch(Exception ioexception4)
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
    	try{
	        Cursor cursor = null;
	        String as[] = {
	            "_data"
	        };
	        cursor = context.getContentResolver().query(uri, as, null, null, null);
	        String s1;
	        if(cursor != null){
	        	String s2;
	            int i = cursor.getColumnIndexOrThrow("_data");
	            cursor.moveToFirst();
	            s2 = cursor.getString(i);
	            s1 = s2;
	            if(cursor == null)
	            	return s1;
	            cursor.close();
	            return s1;
	        }
	        String s = uri.getPath();
	        s1 = s;
	        if(cursor != null)
	        {
	            cursor.close();
	        }
	        return s1;
    	}catch(Exception e){
    		return null;
    	}
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
