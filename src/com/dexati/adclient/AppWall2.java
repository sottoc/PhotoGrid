package com.dexati.adclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.*;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.google.android.gms.analytics.Tracker;
import com.km.photogridbuilder.ApplicationController;

// Referenced classes of package com.dexati.adclient:
//            Dexati

public class AppWall2 extends Activity
{

    protected static final String TAG = "KM";
    private static boolean accepted = false;
    public static Bitmap imageURL;
    public static String url;
    public static WebView webView;
    ImageView adCloser;
    ImageView fullPage;
    RelativeLayout linearLayout;
    private ProgressDialog progress;

    public AppWall2()
    {
        linearLayout = null;
    }

    private void hideProgress()
    {
        if(progress != null && progress.isShowing())
        {
            progress.dismiss();
        }
    }

    public void onAdClick(View view)
    {
        accepted = true;
        if(Dexati.preCache)
        {
            if(Dexati.urlStringFound != null)
            {
                Intent intent1 = new Intent("android.intent.action.VIEW", Uri.parse(Dexati.urlStringFound));
                intent1.setFlags(0x10000000);
                startActivity(intent1);
                finish();
                return;
            } else
            {
                progress = ProgressDialog.show(this, "Your Free App", "Getting the Top App ..", true);
                return;
            }
        } else
        {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
            intent.setFlags(0x10000000);
            startActivity(intent);
            finish();
            return;
        }
    }

    public void onClose(View view)
    {
        finish();
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(0x7f030010);
        accepted = false;
        fullPage = (ImageView)findViewById(0x7f09004d);
        adCloser = (ImageView)findViewById(0x7f09004e);
        fullPage.setImageBitmap(imageURL);
        if(Dexati.preCache)
        {
            webView = new WebView(this);
            webView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView webview, String s)
                {
                    Log.i("KM", "onPageFinished:");
                }

                public void onReceivedError(WebView webview, int i, String s, String s1)
                {
                    Log.i("KM", (new StringBuilder("WebView failed to load. Error code:")).append(i).toString());
                }

                public boolean shouldOverrideUrlLoading(WebView webview, String s)
                {
                    boolean flag;
                    flag = true;
                    try{
	                    Log.d("KM", (new StringBuilder("URL: ")).append(s).toString());
	                    if(!s.startsWith("http") && !s.startsWith("https")){
	                    	Dexati.urlStringFound = s;
	                        if(!AppWall2.accepted)
	                        	return flag;
	                        if(progress != null)
	                        {
	                            runOnUiThread(new Runnable() {
	                                public void run()
	                                {
	                                    progress.dismiss();
	                                }
	                            });
	                        }
	                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(s));
	                        intent.setFlags(0x10000000);
	                        startActivity(intent);
	                        return flag;
	                    }
	                    flag = false;
	                    return flag;
                    }catch(Exception e){
                    	return flag;
                    }
                }
            });
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setInitialScale(1);
            webView.setBackgroundColor(0);
            webView.loadUrl(url);
        }
        Tracker tracker = ((ApplicationController)getApplication()).getTracker();
        tracker.setScreenName("AppWall2");
        tracker.send((new com.google.android.gms.analytics.HitBuilders.AppViewBuilder()).build());
    }

    public void onPause()
    {
        super.onPause();
        hideProgress();
    }



}
