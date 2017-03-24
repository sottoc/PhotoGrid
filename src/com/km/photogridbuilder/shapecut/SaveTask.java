package com.km.photogridbuilder.shapecut;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;
import java.util.Calendar;

// Referenced classes of package com.km.photogridbuilder.shapecut:
//            Utils

public class SaveTask extends AsyncTask
{

    private Bitmap bitmap;
    private boolean isSaved;
    private int mBitmapHeight;
    private int mBitmapWidth;
    private Context mContext;

    public SaveTask(Context context, Bitmap bitmap1, int i, int j)
    {
        mContext = context;
        mBitmapWidth = i;
        mBitmapHeight = j;
        bitmap = bitmap1;
    }

    private Bitmap cropTransparentArea(Bitmap bitmap1)
    {
        int i;
        int j;
        int k;
        int l;
        int i1;
        i = bitmap1.getWidth();
        j = 0;
        k = bitmap1.getHeight();
        l = 0;
        i1 = 0;
        while(true){
	        if(i1 >= bitmap1.getWidth())
	        {
	            int k1 = j - i;
	            int l1 = l - k;
	            if(k1 > 0 && l1 > 0)
	            {
	                bitmap1 = Bitmap.createBitmap(bitmap1, i, k, k1, l1);
	            }
	            return bitmap1;
	        }
	        int j1 = 0;
	        while(j1 < bitmap1.getHeight())
	        {
	        	if(bitmap1.getPixel(i1, j1) != 0)
	            {
	                if(k > j1)
	                {
	                    k = j1;
	                }
	                if(l < j1)
	                {
	                    l = j1;
	                }
	                if(i > i1)
	                {
	                    i = i1;
	                }
	                if(j < i1)
	                {
	                    j = i1;
	                }
	            }
	            j1++;
	        }
	        i1++;
        }
    }

    protected Object doInBackground(Object aobj[])
    {
        return doInBackground((Void[])aobj);
    }

    protected Void doInBackground(Void avoid[])
    {
        if(mBitmapWidth != 0 && mBitmapHeight != 0)
        {
            String s = (new StringBuilder(String.valueOf(mContext.getString(0x7f060023)))).append("_").append(Calendar.getInstance().getTimeInMillis()).toString();
            bitmap = cropTransparentArea(bitmap);
            isSaved = Utils.saveImage(bitmap, mBitmapWidth, mBitmapHeight, s);
        }
        return null;
    }

    protected void onPostExecute(Object obj)
    {
        onPostExecute((Void)obj);
    }

    protected void onPostExecute(Void void1)
    {
        if(isSaved)
        {
            Toast.makeText(mContext, "Saving Success", 0).show();
        } else
        {
            Toast.makeText(mContext, "Saving Failed", 0).show();
        }
        super.onPostExecute(void1);
    }
}
