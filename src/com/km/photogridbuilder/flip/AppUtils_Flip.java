package com.km.photogridbuilder.flip;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.*;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;
import java.io.*;

public class AppUtils_Flip
{

    private static final String APP_FOLDER = "MirrorImage";
    private static final String FILE_EXTENSION = ".jpg";
    private static final String FILE_PREFIX = "Mirror_";
    public static Bitmap mBmpImage;

    public AppUtils_Flip()
    {
    }

    public static Bitmap getBitmap(Uri uri, Context context)
    {
        InputStream inputstream;
        Bitmap bitmap;
        inputstream = null;
        bitmap = null;
        try
        {
        	InputStream inputstream1 = context.getContentResolver().openInputStream(uri);
            inputstream = inputstream1;
            int j = (new ExifInterface(getRealPathFromURI(context, uri))).getAttributeInt("Orientation", 1);
            int i;
            if(j != 6){
            	if(j == 3)
                {
                    i = 180;
                } else
                {
                    i = 0;
                    if(j == 8)
                    {
                        i = 270;
                    }
                }
            }else
            	i = 90;
            android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeStream(inputstream, null, options);
            android.graphics.BitmapFactory.Options options1 = new android.graphics.BitmapFactory.Options();
            if(options.outHeight > 500 || options.outWidth > 500)
            {
                options1.inSampleSize = 8;
            }
            inputstream = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(inputstream, null, options1);
            if(i != 0)
            {
            	Bitmap bitmap1;
                Matrix matrix = new Matrix();
                matrix.postRotate(i);
                bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                bitmap = bitmap1;
            }
            inputstream.close();
        }
        catch(Exception exception4) { }
        return bitmap;
    }

    public static String getRealPathFromURI(Context context, Uri uri)
    {
        Cursor cursor = null;
        String as[] = {
            "_data"
        };
        try{
	        cursor = context.getContentResolver().query(uri, as, null, null, null);
	        String s1;
	        if(cursor != null){
	        	String s2;
	            int i = cursor.getColumnIndexOrThrow("_data");
	            cursor.moveToFirst();
	            s2 = cursor.getString(i);
	            s1 = s2;
	        }else{
		        String s = uri.getPath();
		        s1 = s;
	        }
	        if(cursor != null)
	        {
	            cursor.close();
	        }
	        return s1;
        }catch(Exception e){
        	return null;
        }
    }

    public static boolean saveImage(Bitmap bitmap, Context context)
    {
        File file1;
        try{
	        File file = new File((new StringBuilder()).append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)).append("/").append("MirrorImage").append("/").toString());
	        if(!file.exists())
	        {
	            file.mkdirs();
	        }
	        file1 = new File(file, (new StringBuilder("Mirror_")).append(System.currentTimeMillis()).append(".jpg").toString());
	        if(!file1.exists())
	        {
	            file1.createNewFile();
	        }
	        Toast.makeText(context, 0x7f060050, 0).show();
	        FileOutputStream fileoutputstream = new FileOutputStream(file1);
	        bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, fileoutputstream);
	        fileoutputstream.flush();
	        fileoutputstream.close();
	        return true;
        }catch(Exception e){
        	return false;
        }
    }
}
