package com.km.photogridbuilder.path;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.*;
import android.util.DisplayMetrics;
import com.km.photogridbuilder.Objects.FontManager;

public class TextObject
{

    private static final float SCREEN_MARGIN = 100F;
    private static final int UI_MODE_ANISOTROPIC_SCALE = 2;
    private static final int UI_MODE_ROTATE = 1;
    private int alpha;
    private float angle;
    private float centerX;
    private float centerY;
    public int color;
    private Context context;
    private int displayHeight;
    private int displayWidth;
    private boolean firstLoad;
    private String font;
    private float fontSize;
    private int height;
    public String hexColor;
    private boolean isText;
    private int mUIMode;
    private float maxX;
    private float maxY;
    private float minX;
    private float minY;
    private Paint paint;
    private float scaleX;
    private float scaleY;
    private boolean sticker;
    public String text;
    private int width;

    public TextObject(String s, String s1, int i, int j, String s2, Resources resources, Context context1)
    {
        mUIMode = 1;
        font = null;
        paint = new Paint();
        fontSize = 30F;
        alpha = 150;
        setFont(s1);
        color = j;
        text = s;
        firstLoad = true;
        context = context1;
        hexColor = s2;
        fontSize = i;
        getMetrics(resources);
    }

    private void getMetrics(Resources resources)
    {
        DisplayMetrics displaymetrics = resources.getDisplayMetrics();
        int i;
        int j;
        if(resources.getConfiguration().orientation == 2)
        {
            i = Math.max(displaymetrics.widthPixels, displaymetrics.heightPixels);
        } else
        {
            i = Math.min(displaymetrics.widthPixels, displaymetrics.heightPixels);
        }
        displayWidth = i;
        if(resources.getConfiguration().orientation == 2)
        {
            j = Math.min(displaymetrics.widthPixels, displaymetrics.heightPixels);
        } else
        {
            j = Math.max(displaymetrics.widthPixels, displaymetrics.heightPixels);
        }
        displayHeight = j;
    }

    private boolean setPos(float f, float f1, float f2, float f3, float f4)
    {
        float f5 = f2 * (float)(width / 2);
        float f6 = f3 * (float)(height / 2);
        float f7 = f - f5;
        float f8 = f1 - f6;
        float f9 = f + f5;
        float f10 = f1 + f6;
        if(f7 > (float)displayWidth - 100F || f9 < 100F || f8 > (float)displayHeight - 100F || f10 < 100F)
        {
            return false;
        }
        centerX = f;
        centerY = f1;
        scaleX = f2;
        scaleY = f3;
        angle = f4;
        minX = f7;
        minY = f8;
        Paint paint1 = getPaint();
        float f11 = fontSize;
        if(f2 == 0.0F)
        {
            if(f3 == 0.0F)
            {
                f3 = 1.0F;
            }
        } else
        {
            f3 = f2;
        }
        paint1.setTextSize(f11 * f3);
        maxX = minX + getPaint().measureText(text);
        maxY = f10;
        return true;
    }

    private boolean setPos(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7, float f8)
    {
        float f9 = f2 * (float)(width / 2);
        float f10 = f3 * (float)(height / 2);
        if(f5 == 0.0F && f6 == 0.0F && f7 == 0.0F && f8 == 0.0F)
        {
            f5 = f - f9;
            f7 = f1 - f10;
            f6 = f + f9;
            f8 = f1 + f10;
        }
        if(f5 > (float)displayWidth - 100F || f6 < 100F || f7 > (float)displayHeight - 100F || f8 < 100F)
        {
            return false;
        }
        centerX = f;
        centerY = f1;
        scaleX = f2;
        scaleY = f3;
        angle = f4;
        minX = f5;
        minY = f7;
        Paint paint1 = getPaint();
        float f11 = fontSize;
        if(f2 == 0.0F)
        {
            if(f3 == 0.0F)
            {
                f3 = 1.0F;
            }
        } else
        {
            f3 = f2;
        }
        paint1.setTextSize(f11 * f3);
        maxX = minX + getPaint().measureText(text);
        maxY = f8;
        getPaint().setColor(color);
        getPaint().setTypeface(FontManager.getTypeFace(context, getFont()));
        return true;
    }

    public boolean containsPoint(float f, float f1)
    {
        return f >= minX && f <= maxX && f1 >= minY && f1 <= maxY;
    }

    public void draw(Canvas canvas)
    {
        canvas.save();
        float f = (maxX + minX) / 2.0F;
        float f1 = (maxY + minY) / 2.0F;
        getPaint().setColor(color);
        maxX = minX + getPaint().measureText(text);
        canvas.translate(f, f1);
        canvas.rotate((180F * angle) / 3.141593F);
        canvas.translate(-f, -f1);
        canvas.drawText(text, minX, maxY, getPaint());
        canvas.restore();
    }

    public int getAlpha()
    {
        return alpha;
    }

    public float getAngle()
    {
        return angle;
    }

    public float getCenterX()
    {
        return centerX;
    }

    public float getCenterY()
    {
        return centerY;
    }

    public String getFont()
    {
        return font;
    }

    public float getFontSize()
    {
        return fontSize;
    }

    public int getHeight()
    {
        return height;
    }

    public float getMaxX()
    {
        return maxX;
    }

    public float getMaxY()
    {
        return maxY;
    }

    public float getMinX()
    {
        return minX;
    }

    public float getMinY()
    {
        return minY;
    }

    public Paint getPaint()
    {
        return paint;
    }

    public float getScaleX()
    {
        return scaleX;
    }

    public float getScaleY()
    {
        return scaleY;
    }

    public int getWidth()
    {
        return width;
    }

    public boolean isSticker()
    {
        return sticker;
    }

    public boolean isText()
    {
        return isText;
    }

    public void load(Resources resources, RectF rectf)
    {
        float f;
        float f1;
        getMetrics(resources);
        f = rectf.centerX();
        f1 = rectf.centerY();
        width = (int)rectf.width();
        height = (int)rectf.height();
        if(!firstLoad){
        	if(maxX >= 100F){
            	if(minX > (float)displayWidth - 100F)
                {
                    f = (float)displayWidth - 100F;
                }
            }else
            	f = 100F;
            if(maxY <= 100F)
            {
            	if(minY > (float)displayHeight - 100F)
                {
                    f1 = (float)displayHeight - 100F;
                }
            }else
            	f1 = 100F;
        }else
        	firstLoad = false;
        setPos(f, f1, 0.0F, 0.0F, 0.0F, rectf.left, rectf.right, rectf.top, rectf.bottom);
    }

    public void setAlpha(int i)
    {
        alpha = i;
    }

    public void setFont(String s)
    {
        font = s;
    }

    public void setFontSize(float f)
    {
        fontSize = f;
    }

    public void setPaint(Paint paint1)
    {
        paint = paint1;
    }

    public boolean setPos(ImageObjectPath.PositionAndScale positionandscale)
    {
        float f = positionandscale.getXOff();
        float f1 = positionandscale.getYOff();
        float f2;
        float f3;
        if((2 & mUIMode) != 0)
        {
            f2 = positionandscale.getScaleX();
        } else
        {
            f2 = positionandscale.getScale();
        }
        if((2 & mUIMode) != 0)
        {
            f3 = positionandscale.getScaleY();
        } else
        {
            f3 = positionandscale.getScale();
        }
        return setPos(f, f1, f2, f3, positionandscale.getAngle());
    }

    public void setSticker(boolean flag)
    {
        sticker = flag;
    }

    public void setText(boolean flag)
    {
        isText = flag;
    }

    public void unload()
    {
        text = null;
    }
}
