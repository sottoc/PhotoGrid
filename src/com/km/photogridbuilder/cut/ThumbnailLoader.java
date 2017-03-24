package com.km.photogridbuilder.cut;

import android.content.Context;
import android.graphics.*;
import android.os.AsyncTask;
import android.os.Environment;
import java.io.*;
import java.util.HashMap;

public class ThumbnailLoader
{
    public static interface ImageLoaderListener
    {

        public abstract void onComplete(Bitmap bitmap);

        public abstract void onError();
    }

    public class clearCache extends AsyncTask
    {
        protected Object doInBackground(Object aobj[])
        {
            return doInBackground((Void[])aobj);
        }

        protected Void doInBackground(Void avoid[])
        {
            File afile[] = cacheDir.listFiles();
            int i = afile.length;
            int j = 0;
            do
            {
                if(j >= i)
                {
                    return null;
                }
                afile[j].delete();
                j++;
            } while(true);
        }

        public clearCache()
        {
            super();
        }
    }


    public static HashMap cache = new HashMap();
    private File cacheDir;
    private Context mContext;
    private int reqHeight;
    private int reqWidth;

    public ThumbnailLoader(Context context, int i, int j)
    {
        reqWidth = i;
        reqHeight = j;
        mContext = context;
        if(Environment.getExternalStorageState().equals("mounted"))
        {
            cacheDir = new File(Environment.getExternalStorageDirectory(), "dexati");
        } else
        {
            cacheDir = context.getCacheDir();
        }
        if(!cacheDir.exists())
        {
            cacheDir.mkdirs();
        }
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

    private Bitmap decodeFile(File file)
        throws IOException
    {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        FileInputStream fileinputstream = new FileInputStream(file);
        BitmapFactory.decodeStream(fileinputstream, null, options);
        fileinputstream.close();
        int i = 1;
        if(options.outHeight > 200 || options.outWidth > 200)
        {
            i = (int)Math.pow(2D, (int)Math.round(Math.log((double)200 / (double)Math.max(options.outHeight, options.outWidth)) / Math.log(0.5D)));
        }
        android.graphics.BitmapFactory.Options options1 = new android.graphics.BitmapFactory.Options();
        options1.inSampleSize = i;
        FileInputStream fileinputstream1 = new FileInputStream(file);
        Bitmap bitmap = BitmapFactory.decodeStream(fileinputstream1, null, options1);
        fileinputstream1.close();
        return bitmap;
    }

    public static Bitmap decodeSampledBitmapFromStream(File file, int i, int j)
    {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap;
        try
        {
            BitmapFactory.decodeStream(new FileInputStream(file), null, options);
        }
        catch(FileNotFoundException filenotfoundexception) { }
        options.inSampleSize = calculateInSampleSize(options, i, j);
        options.inTempStorage = new byte[16384];
        options.inJustDecodeBounds = false;
        try
        {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
        }
        catch(FileNotFoundException filenotfoundexception1)
        {
            return null;
        }
        return bitmap;
    }

    public static Bitmap decodeSampledBitmapFromStream(InputStream inputstream, int i, int j)
    {
    	try{
	        synchronized(ThumbnailLoader.class){
	            android.graphics.BitmapFactory.Options options;
	            options = new android.graphics.BitmapFactory.Options();
	            options.inJustDecodeBounds = true;
	            BufferedOutputStream bufferedoutputstream = null;
	            byte abyte0[];
	            BufferedOutputStream bufferedoutputstream1;
	            abyte0 = new byte[inputstream.available()];
	            bufferedoutputstream1 = new BufferedOutputStream(new ByteArrayOutputStream(inputstream.available()));
	            Bitmap bitmap1;
	            bufferedoutputstream1.write(abyte0);
	            bufferedoutputstream1.flush();
	            BitmapFactory.decodeByteArray(abyte0, 0, abyte0.length, options);
	            options.inSampleSize = calculateInSampleSize(options, i, j);
	            options.inJustDecodeBounds = false;
	            bitmap1 = BitmapFactory.decodeByteArray(abyte0, 0, abyte0.length, options);
	            Bitmap bitmap;
	            bitmap = bitmap1;
	            if(bufferedoutputstream1 != null)
	            	bufferedoutputstream1.close();
	            return bitmap;
	        }
    	}catch(Exception e){
    		return null;
    	}
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int i, int j)
    {
        Bitmap bitmap1 = Bitmap.createBitmap(i, j, android.graphics.Bitmap.Config.ARGB_8888);
        float f = i / bitmap.getWidth();
        float f1 = j / bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(f, f1, 0.0F, 0.0F);
        Canvas canvas = new Canvas(bitmap1);
        canvas.setMatrix(matrix);
        canvas.drawBitmap(bitmap, 0.0F, 0.0F, new Paint(2));
        return bitmap1;
    }

    public static Bitmap scaleDown(Bitmap bitmap, int i, int j, boolean flag)
    {
        Bitmap bitmap1;
        try
        {
            bitmap1 = Bitmap.createScaledBitmap(bitmap, i, j, flag);
        }
        catch(OutOfMemoryError outofmemoryerror)
        {
            outofmemoryerror.printStackTrace();
            return bitmap;
        }
        return bitmap1;
    }

    public static Bitmap toGrayscale(Bitmap bitmap)
    {
        int i = bitmap.getHeight();
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), i, android.graphics.Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap1);
        Paint paint = new Paint();
        ColorMatrix colormatrix = new ColorMatrix();
        colormatrix.setSaturation(0.0F);
        paint.setColorFilter(new ColorMatrixColorFilter(colormatrix));
        canvas.drawBitmap(bitmap, 0.0F, 0.0F, paint);
        return bitmap1;
    }

    public void CopyStream(InputStream inputstream, OutputStream outputstream)
    {
        byte abyte0[] = new byte[1024];
        try{
	        while(true){
	        	int i = inputstream.read(abyte0, 0, 1024);
	        	if(i == -1)
	        		return;
	            outputstream.write(abyte0, 0, i);
	        }
        }catch(Exception exception){}
    }

    public void clearCache()
    {
        (new clearCache()).execute(new Void[0]);
    }

    public Bitmap getBitmap(String s, boolean flag)
    {
        File file;
        Bitmap bitmap;
        try{
	        file = new File(s);
	        boolean flag1 = file.exists();
	        bitmap = null;
	        if(!flag1)
	        {
	            return null;
	        }
	        bitmap = decodeSampledBitmapFromStream(file, reqWidth, reqHeight);
	        if(flag)
	        {
	            return bitmap;
	        }
	        Bitmap bitmap1 = scaleDown(bitmap, reqWidth, reqHeight, true);
	        bitmap = bitmap1;
	        return bitmap;
        }catch(Exception e){
        	return null;
        }
    }
}
