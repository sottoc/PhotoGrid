package com.km.photogridbuilder;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.dexati.adclient.AdMobManager;
import com.google.android.gms.analytics.Tracker;
import com.km.photogridbuilder.Objects.ImageItem;
import com.km.photogridbuilder.adapter.GridViewAdapter;
import java.io.File;
import java.io.FilenameFilter;
import java.text.*;
import java.util.ArrayList;

// Referenced classes of package com.km.photogridbuilder:
//            ApplicationController, ImageDisplayScreen

public class GalleryActivity extends Activity
{

    private static final String TAG = "KM";
    private GridViewAdapter customGridAdapter;
    private GridView gridView;
    private ArrayList imageItems;

    public GalleryActivity()
    {
        imageItems = new ArrayList();
    }

    private void getData()
    {
        File file;
        android.graphics.BitmapFactory.Options options;
        imageItems = new ArrayList();
        String s = (new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().toString()))).append(getString(0x7f060061)).toString();
        String s1 = s.substring(0, -6 + s.length());
        Log.v("KM", (new StringBuilder("filePath :")).append(s1).toString());
        file = new File(s1);
        Log.v("KM", (new StringBuilder("exist :")).append(file.exists()).toString());
        if(!file.exists())
        	return;
        File afile[];
        int i;
        afile = file.listFiles(new FilenameFilter() {
            public boolean accept(File file1, String s4)
            {
                return s4.toLowerCase().endsWith(".png");
            }
        });
        i = 0;
        while(i < afile.length){
        	options = new android.graphics.BitmapFactory.Options();
            options.inSampleSize = 2;
            android.graphics.Bitmap bitmap;
            try{
    	        android.graphics.Bitmap bitmap1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(afile[i])), null, options);
    	        bitmap = bitmap1;
            }catch(Exception e){
            	bitmap = null;
            }
            if(bitmap != null){
            	SimpleDateFormat simpledateformat;
                SimpleDateFormat simpledateformat1;
                String s2;
                simpledateformat = new SimpleDateFormat("MMM dd hh:mm aaa");
                simpledateformat1 = new SimpleDateFormat("yyyyMMddHHmmss");
                s2 = afile[i].getName().substring(6, afile[i].getName().lastIndexOf("."));
                try{
    	            String s3 = simpledateformat.format(simpledateformat1.parse(s2));
    	            s2 = s3;
                }catch(Exception e1){}
                imageItems.add(new ImageItem(bitmap, s2, afile[i].getAbsolutePath()));
            }
            i++;
        }
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(0x7f03002f);
        Tracker tracker = ((ApplicationController)getApplication()).getTracker();
        tracker.setScreenName("GalleryActivity");
        tracker.send((new com.google.android.gms.analytics.HitBuilders.AppViewBuilder()).build());
        if(AdMobManager.isReady(getApplication()))
        {
            AdMobManager.show();
        }
    }

    public void onResume()
    {
        super.onResume();
        getData();
        gridView = (GridView)findViewById(0x7f0900d3);
        TextView textview = (TextView)findViewById(0x7f0900d2);
        if(imageItems.size() > 0)
        {
            textview.setVisibility(8);
            customGridAdapter = new GridViewAdapter(this, 0x7f030032, imageItems);
            gridView.setAdapter(customGridAdapter);
            gridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView adapterview, View view, int i, long l)
                {
                    Log.v("KM", (new StringBuilder("selected :")).append(i).toString());
                    Intent intent = new Intent(GalleryActivity.this, com.km.photogridbuilder.ImageDisplayScreen.class);
                    intent.putExtra("imgPath", ((ImageItem)imageItems.get(i)).getImagePath());
                    startActivity(intent);
                }
            });
            return;
        } else
        {
            gridView.setVisibility(8);
            textview.setVisibility(0);
            return;
        }
    }
}
