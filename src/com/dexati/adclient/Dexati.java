package com.dexati.adclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.webkit.*;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;
import org.json.*;

// Referenced classes of package com.dexati.adclient:
//            JSONUtil, AppWall2, AdMobListener, StartWall

public class Dexati
{
    private static class AdLoaderTask extends AsyncTask
    {

        protected Object doInBackground(Object aobj[])
        {
            return doInBackground((String[])aobj);
        }

        protected String doInBackground(String as[])
        {
            Dexati.contactServer();
            return null;
        }

        protected void onPostExecute(Object obj)
        {
            onPostExecute((String)obj);
        }

        protected void onPostExecute(String s)
        {
        }

        private AdLoaderTask()
        {
        }

        AdLoaderTask(AdLoaderTask adloadertask)
        {
            this();
        }
    }


    public static final long MINIMUM_INITIAL = 30000L;
    protected static final String TAG = "KM";
    private static boolean accepted = false;
    private static AdMobListener adListener = null;
    public static boolean admob = false;
    public static boolean appDirect = false;
    public static String backURL = null;
    public static Context context;
    public static String country;
    public static String devId = "NA";
    public static boolean endAd = false;
    public static boolean haveResponse = false;
    public static long lastRequestStart = 0L;
    public static String packageName;
    private static ProgressDialog pd = null;
    public static boolean preCache = true;
    private static boolean rejected = false;
    public static String text;
    public static String type;
    public static String urlStringFound = null;
    public static int versionCode = -1;
    public static WebView webView;

    public Dexati()
    {
    }

    public static void contactServer()
    {
        String s;
        JSONObject jsonobject;
        String s1;
        try
        {
            com.google.android.gms.ads.identifier.AdvertisingIdClient.Info info = AdvertisingIdClient.getAdvertisingIdInfo(context);
            if(!info.isLimitAdTrackingEnabled())
            {
                devId = info.getId();
            }
            s = (new StringBuilder("http://apps.dexati.com/adserver/api/1/adservice?country=")).append(country).append("&package=").append(packageName).append("&devid=").append(devId).append("&type=startup&clientver=2").append("&model=").append(URLEncoder.encode(Build.MODEL)).append("&product=").append(URLEncoder.encode(Build.PRODUCT)).append("&manufacturer=").append(URLEncoder.encode(Build.MANUFACTURER)).append("&appversion=").append(versionCode).append("&osversion=").append(android.os.Build.VERSION.SDK_INT).append("&arch=").append(System.getProperty("os.arch")).toString();
            Log.v("KM", s);
            jsonobject = JSONUtil.getJSONfromURL(s);
            if(jsonobject == null)
            {
                Log.v("KM", "Dexati app server down");
                return;
            }
            s1 = "Download Top Free App !!";
            boolean flag2;
            boolean flag3;
            if(jsonobject.has("admob") && jsonobject.getBoolean("admob"))
            {
                flag3 = true;
            } else
            {
                flag3 = false;
            }
            boolean flag;
            String s2 = null;
            String s3;
            boolean flag1;
            PackageInfo packageinfo;
            JSONArray jsonarray;
            int i;
            int j;
            JSONObject jsonobject1;
            String s4;
            String s5;
            admob = flag3;
            try
            {
                if(jsonobject.has("precache"))
                {
                    preCache = jsonobject.getBoolean("precache");
                }
            }
            catch(Exception exception2) { }
            if(jsonobject.has("endwall") && jsonobject.getBoolean("endwall"))
            {
                flag2 = true;
            } else
            {
                flag2 = false;
            }
            try
            {
                endAd = flag2;
                if(!jsonobject.has("url")){
                	((Activity)context).runOnUiThread(new Runnable() {

                        public void run()
                        {
                            Dexati.context.startActivity(new Intent(Dexati.context, com.dexati.adclient.StartWall.class));
                        }

                    });
                    return;
                }
                flag = false;
                s2 = null;
                s = jsonobject.getString("url");
                s1 = jsonobject.getString("text");
                s3 = jsonobject.getString("app");
                flag1 = jsonobject.has("image");
                s2 = null;
                if(flag1)
                {
        	        s2 = jsonobject.getString("image");
        	        if(jsonobject.has("appdirect"))
        	        {
        	            appDirect = jsonobject.getBoolean("appdirect");
        	        }
        	        if(jsonobject.has("appurl"))
        	        {
        	            backURL = jsonobject.getString("appurl");
        	        }
        	        packageinfo = context.getPackageManager().getPackageInfo(s3, 0);
        	        flag = false;
        	        if(packageinfo == null){
        	        	if(!flag)
        	            {
        	                ((Activity)context).runOnUiThread(new Runnable() {
        	
        	                    public void run()
        	                    {
        	                        Dexati.adListener.showAdmob();
        	                    }
        	
        	                });
        	                return;
        	            }
        	        }else{
        		        jsonarray = jsonobject.getJSONArray("more");
        		        i = 0;
        		        j = jsonarray.length();
                        while(i < j){
                        	try{
                	        	jsonobject1 = jsonarray.getJSONObject(i);
                	            s4 = jsonobject1.getString("app");
                	            context.getPackageManager().getPackageInfo(s4, 0);
                	            flag = true;
                                s = jsonobject1.getString("url");
                                s1 = jsonobject1.getString("text");
                                if(jsonobject1.has("image")){
                        	        s5 = jsonobject1.getString("image");
                        	        s2 = s5;
                                }
                        	}catch(Exception e){
                        	}
                        	i++;
                        }
        	        }
                }
            }catch(Exception exception3) { }
            if(admob)
            {
                ((Activity)context).runOnUiThread(new Runnable() {

                    public void run()
                    {
                        Dexati.adListener.showAdmob();
                    }

                });
                return;
            }
            
            text = s1;
            if(s2 != null)
            {
                AppWall2.url = s;
                try
                {
                    AppWall2.imageURL = BitmapFactory.decodeStream((InputStream)(new URL(s2)).getContent());
                    ((Activity)context).runOnUiThread(new Runnable() {
                        public void run()
                        {
                            Dexati.context.startActivity(new Intent(Dexati.context, com.dexati.adclient.AppWall2.class));
                        }

                    });
                    return;
                }
                // Misplaced declaration of an exception variable
                catch(Exception exception4)
                {
                    Log.v("KM", "Error downloading image", exception4);
                }
                return;
            } else
            {
                ((Activity)context).runOnUiThread(new Runnable() {

                    public void run()
                    {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Dexati.context);
                        builder.setTitle("Top Free App");
                        builder.setMessage(Dexati.text);
                        builder.setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                Dexati.accepted = true;
                                if(Dexati.urlStringFound != null)
                                {
                                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(Dexati.urlStringFound));
                                    intent.setFlags(0x10000000);
                                    Dexati.context.startActivity(intent);
                                    return;
                                } else
                                {
                                    Dexati.pd = ProgressDialog.show(Dexati.context, "Your Free App", "Getting the Top App ..", true);
                                    return;
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                Dexati.rejected = true;
                            }
                        });
                        try
                        {
                            builder.show();
                            return;
                        }
                        catch(Exception exception5)
                        {
                            return;
                        }
                    }

                });
                webView.setWebViewClient(new WebViewClient() {

                    public void onPageFinished(WebView webview, String s6)
                    {
                        Log.i("KM", "onPageFinished:");
                    }

                    public void onReceivedError(WebView webview, int k, String s6, String s7)
                    {
                        Log.i("KM", (new StringBuilder("WebView failed to load. Error code:")).append(k).toString());
                    }

                    public boolean shouldOverrideUrlLoading(WebView webview, String s6)
                    {
                        Log.d("KM", (new StringBuilder("URL: ")).append(s6).toString());
                        if(s6.startsWith("http") || s6.startsWith("https"))
                        {
                            return false;
                        }
                        try
                        {
                            Dexati.urlStringFound = s6;
                            if(Dexati.accepted && !Dexati.rejected)
                            {
                                if(Dexati.pd != null)
                                {
                                    ((Activity)Dexati.context).runOnUiThread(new Runnable() {
                                        public void run()
                                        {
                                            Dexati.pd.dismiss();
                                        }
                                    });
                                }
                                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(s6));
                                intent.setFlags(0x10000000);
                                Dexati.context.startActivity(intent);
                            }
                        }
                        catch(Exception exception5)
                        {
                            exception5.printStackTrace();
                            Log.v("KM", "Error", exception5);
                            return true;
                        }
                        return true;
                    }

                });
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setInitialScale(1);
                webView.setBackgroundColor(0);
                webView.loadUrl(s);
                return;
            }
        }
        catch(Exception exception)
        {
            Log.v("KM", "Error connecting to google Play Services");
        }
    }

    public static void initialize(Context context1, AdMobListener admoblistener)
    {
        context = context1;
        accepted = false;
        rejected = false;
        adListener = admoblistener;
        pd = null;
        webView = new WebView(context);
        try
        {
            CookieSyncManager.createInstance(context);
            CookieManager.getInstance().removeAllCookie();
        }
        catch(Throwable throwable) { }
        if(System.currentTimeMillis() - lastRequestStart > 30000L)
        {
            try
            {
                if(country == null || versionCode == -1)
                {
                    country = context.getResources().getConfiguration().locale.getISO3Country();
                    type = "dexati";
                    PackageInfo packageinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    packageName = packageinfo.packageName;
                    versionCode = packageinfo.versionCode;
                }
            }
            catch(Exception exception)
            {
                Log.v("KM", "Error with basic info. something wrong with phone", exception);
            }
            lastRequestStart = System.currentTimeMillis();
            urlStringFound = null;
            (new AdLoaderTask(null)).execute(new String[0]);
        } else
        if(urlStringFound != null)
        {
            ((Activity)context).runOnUiThread(new Runnable() {

                public void run()
                {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Dexati.context);
                    builder.setTitle("Top Free App");
                    builder.setMessage(Dexati.text);
                    builder.setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i)
                        {
                            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(Dexati.urlStringFound));
                            intent.setFlags(0x10000000);
                            Dexati.context.startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i)
                        {
                            Dexati.rejected = true;
                        }
                    });
                    try
                    {
                        builder.show();
                        return;
                    }
                    catch(Exception exception1)
                    {
                        return;
                    }
                }

            });
            return;
        }
    }
}
