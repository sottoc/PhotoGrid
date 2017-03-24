package com.km.photogridbuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.dexati.adclient.AdMobManager;
import com.google.android.gms.analytics.Tracker;
import com.km.photogridbuilder.adapter.SelectAdapter;
import com.km.photogridbuilder.bean.Constant;
import com.km.photogridbuilder.path.StickerActivityPath;

// Referenced classes of package com.km.photogridbuilder:
//            ApplicationController, StickerActivity

public class StyleChooserActivity extends Activity
    implements android.widget.AdapterView.OnItemClickListener
{

    int displayImage;
    private boolean isIrregularClicked;
    private boolean isRegularClicked;
    private boolean isShapeGridClicked;
    GridView mGrid;
    ImageView mIrregular;
    ImageView mRegular;
    ImageView mShapes;
    SelectAdapter selectAdapter;

    public StyleChooserActivity()
    {
        displayImage = 0x7f020144;
        isRegularClicked = true;
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(0x7f03003b);
        mRegular = (ImageView)findViewById(0x7f09012a);
        mIrregular = (ImageView)findViewById(0x7f09012c);
        mShapes = (ImageView)findViewById(0x7f09012e);
        selectAdapter = new SelectAdapter(this, Constant.array_regular_icons);
        mGrid = (GridView)findViewById(0x7f090130);
        mGrid.setAdapter(selectAdapter);
        mGrid.setOnItemClickListener(this);
        Tracker tracker = ((ApplicationController)getApplication()).getTracker();
        tracker.setScreenName("StyleChooser");
        tracker.send((new com.google.android.gms.analytics.HitBuilders.AppViewBuilder()).build());
        if(AdMobManager.isReady(getApplication()))
        {
            AdMobManager.show();
        }
    }

    public void onIrregularClick(View view)
    {
        mRegular.setImageResource(0x7f020071);
        mIrregular.setImageResource(0x7f020068);
        mShapes.setImageResource(0x7f020079);
        isRegularClicked = false;
        isIrregularClicked = true;
        isShapeGridClicked = false;
        selectAdapter = new SelectAdapter(this, Constant.array_irregular_icon);
        mGrid.setAdapter(selectAdapter);
    }

    public void onItemClick(AdapterView adapterview, View view, int i, long l)
    {
        Tracker tracker;
        Intent intent;
        tracker = ((ApplicationController)getApplication()).getTracker();
        intent = new Intent();
        if(!isRegularClicked){
        	if(isIrregularClicked)
            {
                tracker.send((new com.google.android.gms.analytics.HitBuilders.EventBuilder()).setCategory("Style").setAction("IRRegular").setLabel((new StringBuilder()).append(i).toString()).build());
                intent.putExtra("frame", Constant.array_irregular_shapes[i]);
                intent.putExtra("type", 103);
                intent.setClass(this, com.km.photogridbuilder.path.StickerActivityPath.class);
            } else
            if(isShapeGridClicked)
            {
                tracker.send((new com.google.android.gms.analytics.HitBuilders.EventBuilder()).setCategory("Style").setAction("Shape").setLabel((new StringBuilder()).append(i).toString()).build());
                intent.putExtra("frame", Constant.array_gride_shapes[i]);
                intent.putExtra("type", 102);
                intent.setClass(this, com.km.photogridbuilder.path.StickerActivityPath.class);
            }
        }else{
	        tracker.send((new com.google.android.gms.analytics.HitBuilders.EventBuilder()).setCategory("Style").setAction("Regular").setLabel((new StringBuilder()).append(i).toString()).build());
	        intent.putExtra("frame", Constant.array_regular_template[i]);
	        intent.setClass(this, com.km.photogridbuilder.StickerActivity.class);
        }
        startActivity(intent);
    }

    public void onRegularClick(View view)
    {
        mRegular.setImageResource(0x7f020072);
        mIrregular.setImageResource(0x7f020067);
        mShapes.setImageResource(0x7f020079);
        isRegularClicked = true;
        isIrregularClicked = false;
        isShapeGridClicked = false;
        selectAdapter = new SelectAdapter(this, Constant.array_regular_icons);
        mGrid.setAdapter(selectAdapter);
    }

    public void onShapeGridClick(View view)
    {
        mRegular.setImageResource(0x7f020071);
        mIrregular.setImageResource(0x7f020067);
        mShapes.setImageResource(0x7f02007a);
        isRegularClicked = false;
        isIrregularClicked = false;
        isShapeGridClicked = true;
        selectAdapter = new SelectAdapter(this, Constant.array_grid_icon);
        mGrid.setAdapter(selectAdapter);
    }
}
