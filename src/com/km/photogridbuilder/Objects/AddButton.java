package com.km.photogridbuilder.Objects;

import android.graphics.*;
import android.util.Log;

public class AddButton
{

    public int angle;
    public Bitmap bmp;
    public RectF bounds;
    public int buttonNo;
    public boolean done;
    public Boolean mOnCounter;
    public Boolean mServed;
    public Matrix matrix;
    public Matrix matrixPart;
    public Path newPath;
    private Paint paint;
    public float ratio;
    public float rx;
    public float ry;
    public int totalButton;
    public float x;
    public float xCenter;
    public float xOriginal;
    public float y;
    public float yCenter;
    public float yOriginal;

    public AddButton(Bitmap bitmap, float f, float f1)
    {
        bounds = new RectF();
        matrix = new Matrix();
        matrixPart = new Matrix();
        ratio = 1.0F;
        mServed = Boolean.valueOf(false);
        paint = new Paint();
        buttonNo = 0;
        totalButton = 0;
        done = false;
        bmp = bitmap;
        xOriginal = f;
        yOriginal = f1;
        x = f - (float)bitmap.getWidth();
        y = f1 - (float)bitmap.getHeight();
        rx = f + (float)bitmap.getWidth();
        ry = f1 + (float)bitmap.getHeight();
        bounds.set(x, y, rx, ry);
        xCenter = f - (float)bitmap.getWidth() / 2.0F;
        yCenter = f1 - (float)bitmap.getHeight() / 2.0F;
        applyMove();
    }

    public AddButton(RectF rectf)
    {
        bounds = new RectF();
        matrix = new Matrix();
        matrixPart = new Matrix();
        ratio = 1.0F;
        mServed = Boolean.valueOf(false);
        paint = new Paint();
        buttonNo = 0;
        totalButton = 0;
        done = false;
        bounds.set(rectf);
    }

    public void applyMove()
    {
        matrix.reset();
        matrix.postTranslate(x, y);
    }

    public Boolean containsPoint(float f, float f1)
    {
        Log.e("touch", (new StringBuilder("x : ")).append(f).append(" Y : ").append(f1).append(" contains:").append(bounds.contains(f, f1)).toString());
        return Boolean.valueOf(bounds.contains(f, f1));
    }

    public void draw(Canvas canvas)
    {
        paint.setAntiAlias(true);
        canvas.save();
        canvas.drawBitmap(bmp, xCenter, yCenter, paint);
        canvas.restore();
    }

    public void rotate(int i)
    {
        if(i >= 360 || i <= -360)
        {
            i = 0;
        }
        angle = i;
        applyMove();
    }

    public void setBitmap(Bitmap bitmap)
    {
        bmp = bitmap;
    }

    public void translate(float f, float f1)
    {
        x = f;
        y = f1;
        applyMove();
    }
}
