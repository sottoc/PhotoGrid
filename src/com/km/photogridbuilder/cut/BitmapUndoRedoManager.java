package com.km.photogridbuilder.cut;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BitmapUndoRedoManager
{
    private class SaveAsync extends AsyncTask
    {
        protected File doInBackground(Bitmap abitmap[])
        {
            if(abitmap[0] != null)
            {
                Log.e("undo", "step1");
                return saveBitmap(abitmap[0], (new StringBuilder("temp")).append(System.currentTimeMillis()).append(".png").toString());
            } else
            {
                Log.e("undo", "step5");
                return null;
            }
        }

        protected Object doInBackground(Object aobj[])
        {
            return doInBackground((Bitmap[])aobj);
        }

        protected void onPostExecute(File file)
        {
            if(file != null)
            {
                if(bitmapsUndo.size() > 2)
                {
                    ((File)bitmapsUndo.get(0)).delete();
                }
                bitmapsUndo.add(file);
                Log.e("undo", "saved");
            } else
            {
                Log.e("undo", "not saved");
            }
            super.onPostExecute(file);
        }

        protected void onPostExecute(Object obj)
        {
            onPostExecute((File)obj);
        }

        private SaveAsync()
        {
            super();
        }

        SaveAsync(SaveAsync saveasync)
        {
            this();
        }
    }


    private List bitmapsRedo;
    private List bitmapsUndo;

    public BitmapUndoRedoManager()
    {
        bitmapsRedo = new ArrayList();
        bitmapsUndo = new ArrayList();
    }

    private File saveBitmap(Bitmap bitmap, String s)
    {
        File file;
        FileOutputStream fileoutputstream;
        file = new File(Environment.getExternalStorageDirectory(), s);
        Log.e("undo", "step2");
        try{
	        fileoutputstream = null;
	        FileOutputStream fileoutputstream1 = new FileOutputStream(file);
	        if(fileoutputstream1 == null)
	        {
	            return file;
	        }
	        Log.e("undo", "step3");
	        bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, fileoutputstream1);
	        fileoutputstream1.flush();
	        try
	        {
	            fileoutputstream1.close();
	        }
	        catch(Exception exception) { }
	        return file;
        }catch(Exception e){
        	return null;
        }
    }

    public void destroyTemporaryFolder(Context context)
    {
        int i = 0;
        while(i < bitmapsRedo.size()){
        	((File)bitmapsRedo.get(i)).delete();
            i++;
        }
        int j = 0;
        if(j < bitmapsUndo.size())
        {
        	((File)bitmapsUndo.get(j)).delete();
            j++;
        }
    }

    public Bitmap getRedo()
    {
        if(bitmapsRedo.size() > 0)
        {
            bitmapsUndo.add((File)bitmapsRedo.get(-1 + bitmapsRedo.size()));
            bitmapsRedo.remove(-1 + bitmapsRedo.size());
            return BitmapFactory.decodeFile(((File)bitmapsUndo.get(-1 + bitmapsUndo.size())).getPath());
        } else
        {
            return BitmapFactory.decodeFile(((File)bitmapsUndo.get(-1 + bitmapsUndo.size())).getPath());
        }
    }

    public Bitmap getUndo()
    {
        Bitmap bitmap;
        try
        {
            if(bitmapsUndo.size() > 1)
            {
                bitmapsRedo.add((File)bitmapsUndo.get(-1 + bitmapsUndo.size()));
                bitmapsUndo.remove(-1 + bitmapsUndo.size());
                return BitmapFactory.decodeFile(((File)bitmapsUndo.get(-1 + bitmapsUndo.size())).getPath());
            }
            bitmap = BitmapFactory.decodeFile(((File)bitmapsUndo.get(0)).getPath());
        }
        catch(OutOfMemoryError outofmemoryerror)
        {
            return null;
        }
        return bitmap;
    }

    public boolean isRedo()
    {
        return bitmapsRedo.size() > 0;
    }

    public boolean isUndo()
    {
        return bitmapsUndo.size() > 0;
    }

    public void saveState(Bitmap bitmap)
    {
        (new SaveAsync(null)).execute(new Bitmap[] {
            bitmap
        });
        bitmapsRedo.clear();
    }


}
