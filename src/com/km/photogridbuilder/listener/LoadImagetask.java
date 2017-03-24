package com.km.photogridbuilder.listener;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import java.io.IOException;
import java.net.*;

// Referenced classes of package com.km.photogridbuilder.listener:
//            ImageLoadListener

public class LoadImagetask extends AsyncTask
{

    private Bitmap image;
    private ImageLoadListener mListener;

    public LoadImagetask(ImageLoadListener imageloadlistener)
    {
        mListener = imageloadlistener;
    }

    protected Object doInBackground(Object aobj[])
    {
        return doInBackground((String[])aobj);
    }

    protected Void doInBackground(String as[])
    {
        try
        {
            image = BitmapFactory.decodeStream((new URL(as[0])).openConnection().getInputStream());
        }
        catch(MalformedURLException malformedurlexception)
        {
            malformedurlexception.printStackTrace();
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Object obj)
    {
        onPostExecute((Void)obj);
    }

    protected void onPostExecute(Void void1)
    {
        mListener.onImageLoad(image);
        super.onPostExecute(void1);
    }
}
