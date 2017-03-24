package com.km.photogridbuilder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.*;
import android.widget.Toast;
import com.dexati.adclient.*;
import com.google.android.gms.analytics.Tracker;
import java.io.File;

// Referenced classes of package com.km.photogridbuilder:
//            StickerActivity, ApplicationController, StyleChooserActivity, SettingActivity, 
//            GalleryActivity

public class MainActivity extends Activity
    implements AdMobListener
{

    private static final String TAG = "KM";
    public static int height = 0;
    public static int width = 0;
    long startAdTime;

    public MainActivity()
    {
        startAdTime = 0L;
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        super.onActivityResult(i, j, intent);
        if(j == -1 && intent != null && i == 0)
        {
            Intent intent1 = new Intent(intent);
            intent1.setClass(this, com.km.photogridbuilder.StickerActivity.class);
            startActivity(intent1);
        }
    }

    public void onAd1(View view)
    {
        Tracker tracker = ((ApplicationController)getApplication()).getTracker();
        tracker.send((new com.google.android.gms.analytics.HitBuilders.EventBuilder()).setCategory("CrossPromotion").setAction("App1").setLabel("App1").build());
        tracker.setScreenName("CrossPromote");
        tracker.send((new com.google.android.gms.analytics.HitBuilders.AppViewBuilder()).build());
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.km.funnyfacedecorator&referrer=utm_source%3Dcrosspromoti" +"on%26utm_medium%3Dstartpage%26utm_campaign%3Dcom.km.photogridbuilder"));
        intent.setFlags(0x10000000);
        startActivity(intent);
    }

    public void onAd2(View view)
    {
        Tracker tracker = ((ApplicationController)getApplication()).getTracker();
        tracker.send((new com.google.android.gms.analytics.HitBuilders.EventBuilder()).setCategory("CrossPromotion").setAction("App2").setLabel("App2").build());
        tracker.setScreenName("CrossPromote");
        tracker.send((new com.google.android.gms.analytics.HitBuilders.AppViewBuilder()).build());
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.km.natureframes&referrer=utm_source%3Dcrosspromotion%26u" +"tm_medium%3Dstartpage%26utm_campaign%3Dcom.km.photogridbuilder"));
        intent.setFlags(0x10000000);
        startActivity(intent);
    }

    public void onAd3(View view)
    {
        Tracker tracker = ((ApplicationController)getApplication()).getTracker();
        tracker.send((new com.google.android.gms.analytics.HitBuilders.EventBuilder()).setCategory("CrossPromotion").setAction("App3").setLabel("App3").build());
        tracker.setScreenName("CrossPromote");
        tracker.send((new com.google.android.gms.analytics.HitBuilders.AppViewBuilder()).build());
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.km.makemedragon&referrer=utm_source%3Dcrosspromotion%26u" +"tm_medium%3Dstartpage%26utm_campaign%3Dcom.km.photogridbuilder"));
        intent.setFlags(0x10000000);
        startActivity(intent);
    }

    public void onAd4(View view)
    {
        Tracker tracker = ((ApplicationController)getApplication()).getTracker();
        tracker.send((new com.google.android.gms.analytics.HitBuilders.EventBuilder()).setCategory("CrossPromotion").setAction("App4").setLabel("App4").build());
        tracker.setScreenName("CrossPromote");
        tracker.send((new com.google.android.gms.analytics.HitBuilders.AppViewBuilder()).build());
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.km.voicecamera&referrer=utm_source%3Dcrosspromotion%26ut" +"m_medium%3Dstartpage%26utm_campaign%3Dcom.km.photogridbuilder"));
        intent.setFlags(0x10000000);
        startActivity(intent);
    }

    public void onAd5(View view)
    {
        Tracker tracker = ((ApplicationController)getApplication()).getTracker();
        tracker.send((new com.google.android.gms.analytics.HitBuilders.EventBuilder()).setCategory("CrossPromotion").setAction("App5").setLabel("App5").build());
        tracker.setScreenName("CrossPromote");
        tracker.send((new com.google.android.gms.analytics.HitBuilders.AppViewBuilder()).build());
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.km.waterfallframes&referrer=utm_source%3Dcrosspromotion%" +"26utm_medium%3Dstartpage%26utm_campaign%3Dcom.km.voicecamera"));
        intent.setFlags(0x10000000);
        startActivity(intent);
    }

    public void onAd6(View view)
    {
        Tracker tracker = ((ApplicationController)getApplication()).getTracker();
        tracker.send((new com.google.android.gms.analytics.HitBuilders.EventBuilder()).setCategory("CrossPromotion").setAction("App6").setLabel("App6").build());
        tracker.setScreenName("CrossPromote");
        tracker.send((new com.google.android.gms.analytics.HitBuilders.AppViewBuilder()).build());
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.km.beachframes&referrer=utm_source%3Dcrosspromotion%26ut" +"m_medium%3Dstartpage%26utm_campaign%3Dcom.km.photogridbuilder"));
        intent.setFlags(0x10000000);
        startActivity(intent);
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(0x7f030035);
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        startAdTime = System.currentTimeMillis();
        Dexati.initialize(this, this);
        Tracker tracker = ((ApplicationController)getApplication()).getTracker();
        tracker.setScreenName("MainActivity");
        tracker.send((new com.google.android.gms.analytics.HitBuilders.AppViewBuilder()).build());
    }

    public void onFree(View view)
    {
        startActivity((new Intent()).setClass(this, com.km.photogridbuilder.StickerActivity.class));
    }

    public void onGrid(View view)
    {
        startActivity((new Intent()).setClass(this, com.km.photogridbuilder.StyleChooserActivity.class));
    }

    public boolean onKeyDown(int i, KeyEvent keyevent)
    {
        if(i == 4)
        {
            startActivity(new Intent(this, com.dexati.adclient.EndWall.class));
            finish();
            return true;
        } else
        {
            return super.onKeyDown(i, keyevent);
        }
    }

    public void onSettigClicked(View view)
    {
        startActivity((new Intent()).setClass(this, com.km.photogridbuilder.SettingActivity.class));
    }

    public void onViewCreations(View view)
    {
        String s = (new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().toString()))).append(getString(0x7f060061)).toString();
        String s1 = s.substring(0, -6 + s.length());
        Log.v("KM", (new StringBuilder("filePath :")).append(s1).toString());
        if((new File(s1)).exists())
        {
            startActivity(new Intent(this, com.km.photogridbuilder.GalleryActivity.class));
            return;
        } else
        {
            Toast.makeText(this, "You have not created any Collages yet. Create new Collages using options above.", 1).show();
            return;
        }
    }

    public void showAdmob()
    {
        ((ApplicationController)getApplication()).getTracker().send((new com.google.android.gms.analytics.HitBuilders.TimingBuilder()).setCategory("Dexati").setValue(System.currentTimeMillis() - startAdTime).setVariable("DexatiServer").setLabel("DexatiServer").build());
        AdMobManager.initialize(getApplication());
    }

}
