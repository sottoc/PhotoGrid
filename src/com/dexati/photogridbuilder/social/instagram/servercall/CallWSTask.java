package com.dexati.photogridbuilder.social.instagram.servercall;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

// Referenced classes of package com.dexati.photogridbuilder.social.instagram.servercall:
//            HttpUtils, GetJSONListener

public class CallWSTask extends AsyncTask
{
    private GetJSONListener getJSONListener;
    private Context mContext;
    private String mType;
    ProgressDialog progressDialog;
    private String responseStr;

    public CallWSTask(Context context, GetJSONListener getjsonlistener, String s)
    {
        getJSONListener = getjsonlistener;
        mContext = context;
        mType = s;
    }

    protected String doInBackground(Object aobj[])
    {
        DefaultHttpClient defaulthttpclient = HttpUtils.getNewHttpClient();
        int i = aobj.length;
        int j = 0;
        do
        {
            if(j >= i)
            {
                return responseStr;
            }
            Object obj = aobj[j];
            Log.e("Url:", obj.toString());
            HttpGet httpget = new HttpGet(obj.toString());
            try
            {
                responseStr = EntityUtils.toString(defaulthttpclient.execute(httpget).getEntity());
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
            j++;
        } while(true);
    }

    protected void onPostExecute(Object obj)
    {
        onPostExecute((String)obj);
    }

    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        progressDialog.dismiss();
        getJSONListener.onRemoteCallComplete(responseStr, mType);
    }

    protected void onPreExecute()
    {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(mContext, mContext.getString(0x7f060053), mContext.getString(0x7f060052));
    }
}
