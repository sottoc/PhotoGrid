package com.km.photogridbuilder;

import android.app.Application;
import com.google.android.gms.analytics.*;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class ApplicationController extends Application
{

    public static final String LOG_TAG = "KM";
    private static final String PROPERTY_ID = "UA-40333083-40";
    private static ApplicationController applicationController = null;
    private Tracker tracker;

    public ApplicationController()
    {
    }

    private static void checkInstance()
    {
        if(applicationController == null)
        {
            throw new IllegalStateException("Application not yet created !");
        } else
        {
            return;
        }
    }

    public static ApplicationController getInstance()
    {
        checkInstance();
        return applicationController;
    }

    public Tracker getTracker()
    {
    	try{
	        synchronized(this){
	        	Tracker tracker1;
	            if(tracker == null)
	            {
	                GoogleAnalytics googleanalytics = GoogleAnalytics.getInstance(this);
	                googleanalytics.getLogger().setLogLevel(0);
	                tracker = googleanalytics.newTracker("UA-40333083-40");
	                tracker.enableAdvertisingIdCollection(true);
	            }
	            tracker1 = tracker;
	            return tracker1;
	        }
    	}catch(Exception e){
    		return null;
    	}
    }

    public void onCreate()
    {
        super.onCreate();
        applicationController = this;
        StorageUtils.getOwnCacheDirectory(getApplicationContext(), "UniversalImageLoader/Cache");
        com.nostra13.universalimageloader.core.DisplayImageOptions displayimageoptions = (new com.nostra13.universalimageloader.core.DisplayImageOptions.Builder()).showStubImage(0x7f0200f9).cacheInMemory().cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY).build();
        ImageLoader.getInstance().init((new com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder(getApplicationContext())).threadPoolSize(5).threadPriority(4).denyCacheImageMultipleSizesInMemory().memoryCache(new WeakMemoryCache()).memoryCache(new LRULimitedMemoryCache(0xa00000)).discCacheFileNameGenerator(new HashCodeFileNameGenerator()).defaultDisplayImageOptions(displayimageoptions).build());
    }

    public void onLowMemory()
    {
        super.onLowMemory();
    }

    public void onTerminate()
    {
        super.onTerminate();
    }

}
