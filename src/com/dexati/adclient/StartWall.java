package com.dexati.adclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.*;
import android.widget.Toast;
import com.google.android.gms.analytics.Tracker;
import com.km.photogridbuilder.ApplicationController;
import java.net.URLEncoder;
import java.util.Random;

// Referenced classes of package com.dexati.adclient:
//            Dexati

public class StartWall extends Activity
{

    protected static final String TAG = "KM";
    public static String URL_SERVER = "http://apps.dexati.com/adserver/api/1/pages/startup";
    private static boolean initialLoadFinished = false;
    private ProgressDialog progress;

    public StartWall()
    {
    }

    private void hideProgress()
    {
        if(progress != null && progress.isShowing())
        {
            progress.dismiss();
        }
    }

    private void showProgress()
    {
        if(progress == null)
        {
            progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
        }
        progress.show();
    }

    public void onClose(View view)
    {
        finish();
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(0x7f030036);
        WebView webview = (WebView)findViewById(0x7f0900a7);
        webview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView webview1, String s1)
            {
                Log.d("KM", "WebView onPageFinished");
                hideProgress();
                StartWall.initialLoadFinished = true;
            }

            public void onReceivedError(WebView webview1, int i, String s1, String s2)
            {
                Log.i("KM", (new StringBuilder("WebView failed to load. Error code:")).append(i).toString());
                if(!StartWall.initialLoadFinished)
                {
                    webview1.setVisibility(4);
                    hideProgress();
                    super.onReceivedError(webview1, i, s1, s2);
                    finish();
                }
            }

            public boolean shouldOverrideUrlLoading(WebView webview1, String s1)
            {
                Log.d("KM", (new StringBuilder("URL: ")).append(s1).toString());
                if(s1.startsWith("http") || s1.startsWith("https"))
                {
                    return false;
                }
                try
                {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(s1));
                    startActivity(intent);
                    finish();
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                    Toast.makeText(StartWall.this, "Not supported on your device.", 1000).show();
                    finish();
                    return true;
                }
                return true;
            }
        });
        String s = (new StringBuilder(String.valueOf(URL_SERVER))).append("?random=").append((new Random()).nextInt(10000)).append("&country=").append(Dexati.country).append("&package=").append(Dexati.packageName).append("&devid=").append(Dexati.devId).append("&model=").append(URLEncoder.encode(Build.MODEL)).append("&product=").append(URLEncoder.encode(Build.PRODUCT)).append("&manufacturer=").append(URLEncoder.encode(Build.MANUFACTURER)).append("&appversion=").append(Dexati.versionCode).append("&osversion=").append(android.os.Build.VERSION.SDK_INT).toString();
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setInitialScale(1);
        webview.setBackgroundColor(0);
        Log.v("KM", (new StringBuilder("URL=")).append(s).toString());
        webview.loadUrl(s);
        showProgress();
        Tracker tracker = ((ApplicationController)getApplication()).getTracker();
        tracker.setScreenName("StartWall");
        tracker.send((new com.google.android.gms.analytics.HitBuilders.AppViewBuilder()).build());
    }

    public void onPause()
    {
        super.onPause();
        hideProgress();
    }




}
