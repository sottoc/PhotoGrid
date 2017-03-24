package com.dexati.adclient;

import android.app.Application;
import android.util.Log;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.Tracker;
import com.km.photogridbuilder.ApplicationController;

public class AdMobManager
{

    private static final int AD_TIME = 62000;
    private static final String TAG = "KM";
    private static Application application;
    private static InterstitialAd interstitial;
    private static long previousTime = 0L;

    public AdMobManager()
    {
    }

    public static void initialize(Application application1)
    {
        application = application1;
        interstitial = new InterstitialAd(application);
        interstitial.setAdUnitId("ca-app-pub-4094465090325270/7880708744");
        com.google.android.gms.ads.AdRequest adrequest = (new com.google.android.gms.ads.AdRequest.Builder()).build();
        final long startAdTime = System.currentTimeMillis();
        Log.v("KM", "Request to Load First Admob");
        interstitial.setAdListener(new AdListener() {
            public void onAdClosed()
            {
            }

            public void onAdLoaded()
            {
                ((ApplicationController)AdMobManager.application).getTracker().send((new com.google.android.gms.analytics.HitBuilders.TimingBuilder()).setCategory("admob").setValue(System.currentTimeMillis() - startAdTime).setVariable("AdmobLoadTimeFirst").setLabel("AdmobLoadTimeFirst").build());
                Log.v("KM", "Loaded First Admob");
            }
        });
        interstitial.loadAd(adrequest);
    }

    public static boolean isReady(Application application1)
    {
        StringBuilder stringbuilder = new StringBuilder("IsReady - interstitial=");
        String s;
        if(interstitial != null)
        {
            s = (new StringBuilder("Not null. loaded = ")).append(interstitial.isLoaded()).toString();
        } else
        {
            s = "null";
        }
        Log.v("KM", stringbuilder.append(s).append(", Time Diff=").append(System.currentTimeMillis() - previousTime).toString());
        if(interstitial == null)
        {
            initialize(application1);
            return false;
        }
        return interstitial.isLoaded() && System.currentTimeMillis() - previousTime > 62000L;
    }

    public static void show()
    {
        Log.v("KM", "Show Request");
        if(interstitial == null)
        {
            return;
        }
        if(interstitial.isLoaded())
        {
            interstitial.show();
        }
        previousTime = System.currentTimeMillis();
        Log.v("KM", "Request to Load After First Admob");
        interstitial = new InterstitialAd(application);
        interstitial.setAdUnitId("ca-app-pub-4094465090325270/9357441941");
        com.google.android.gms.ads.AdRequest adrequest = (new com.google.android.gms.ads.AdRequest.Builder()).build();
        final long startAdTime = System.currentTimeMillis();
        interstitial.setAdListener(new AdListener() {
            public void onAdClosed()
            {
            }

            public void onAdLoaded()
            {
                ((ApplicationController)AdMobManager.application).getTracker().send((new com.google.android.gms.analytics.HitBuilders.TimingBuilder()).setCategory("admob").setValue(System.currentTimeMillis() - startAdTime).setVariable("AdmobLoadTimeAfterFirst").setLabel("AdmobLoadTimeAfterFirst").build());
                Log.v("KM", "Loaded After First Admob");
            }
        });
        interstitial.loadAd(adrequest);
    }


}
