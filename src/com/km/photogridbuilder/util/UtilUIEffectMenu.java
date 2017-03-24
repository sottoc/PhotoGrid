package com.km.photogridbuilder.util;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.km.photogridbuilder.listener.EffectSelectListener;
import java.util.ArrayList;

public class UtilUIEffectMenu
{

    public static ArrayList shapeViews = new ArrayList();

    public UtilUIEffectMenu()
    {
    }

    public static void loadEffects(Context context, LinearLayout linearlayout, final EffectSelectListener listener, int ai[])
    {
        int i = (int)context.getResources().getDimension(0x7f0a001f);
        if(linearlayout != null)
        {
            linearlayout.removeAllViewsInLayout();
        }
        int j = 0;
        do
        {
            if(j >= ai.length)
            {
                return;
            }
            android.widget.LinearLayout.LayoutParams layoutparams = new android.widget.LinearLayout.LayoutParams(i, i);
            layoutparams.setMargins(5, 5, 5, 5);
            final ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(layoutparams);
            imageView.setImageResource(ai[j]);
            imageView.setId(ai[j]);
            imageView.setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(View view)
                {
                    listener.onStickerSelect(imageView.getId());
                }
            });
            linearlayout.addView(imageView);
            j++;
        } while(true);
    }

}
