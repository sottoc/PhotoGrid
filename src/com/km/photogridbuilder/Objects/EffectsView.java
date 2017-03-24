package com.km.photogridbuilder.Objects;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class EffectsView extends View
{

    public RectF gapRect;
    private Bitmap mbitmap;

    public EffectsView(Context context)
    {
        this(context, null);
        setBackgroundResource(0x7f020010);
    }

    public EffectsView(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
        setBackgroundResource(0x7f020010);
    }

    public EffectsView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        gapRect = new RectF();
        setBackgroundResource(0x7f020010);
    }

    public Bitmap getBitmap()
    {
        return mbitmap;
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if(mbitmap != null)
        {
            float f = 1.0F * ((1.0F * (float)mbitmap.getWidth()) / (float)mbitmap.getHeight());
            float f1 = (1.0F * (float)getWidth()) / f;
            float f2 = getWidth();
            gapRect.top = ((float)getHeight() - f1) / 2.0F;
            gapRect.bottom = ((float)getHeight() - f1) / 2.0F;
            Rect rect;
            if(f1 > 1.0F * (float)getHeight())
            {
                f1 = getHeight();
                f2 = f * (1.0F * (float)getHeight());
                gapRect.left = ((float)getWidth() - f2) / 2.0F;
                gapRect.right = ((float)getWidth() - f2) / 2.0F;
                gapRect.top = 0.0F;
                gapRect.bottom = 0.0F;
                Log.e("View", (new StringBuilder(String.valueOf(getHeight()))).append(" height : newHeight").append(f1).toString());
            } else
            {
                gapRect.left = 0.0F;
            }
            Log.v("value", (new StringBuilder(String.valueOf(gapRect.left))).toString());
            rect = new Rect((int)gapRect.left, (int)gapRect.top, (int)(f2 + gapRect.left), (int)(f1 + gapRect.top));
            canvas.drawBitmap(mbitmap, null, rect, null);
        }
    }

    public int setBitmap(Bitmap bitmap)
    {
        mbitmap = bitmap;
        invalidate();
        return 0;
    }
}
