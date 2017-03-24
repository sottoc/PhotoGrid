package com.km.photogridbuilder.util;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.*;
import android.util.DisplayMetrics;

public class ImageObject
{
    public static class PositionAndScale
    {

        float angle;
        float scale;
        float scaleX;
        float scaleY;
        boolean updateAngle;
        boolean updateScale;
        boolean updateScaleXY;
        float xOff;
        float yOff;

        public float getAngle()
        {
            if(!updateAngle)
            {
                return 0.0F;
            } else
            {
                return angle;
            }
        }

        public float getScale()
        {
            if(!updateScale)
            {
                return 1.0F;
            } else
            {
                return scale;
            }
        }

        public float getScaleX()
        {
            if(!updateScaleXY)
            {
                return 1.0F;
            } else
            {
                return scaleX;
            }
        }

        public float getScaleY()
        {
            if(!updateScaleXY)
            {
                return 1.0F;
            } else
            {
                return scaleY;
            }
        }

        public float getXOff()
        {
            return xOff;
        }

        public float getYOff()
        {
            return yOff;
        }

        protected void set(float f, float f1, float f2, float f3, float f4, float f5)
        {
            float f6 = 1.0F;
            xOff = f;
            yOff = f1;
            if(f2 == 0.0F)
            {
                f2 = f6;
            }
            scale = f2;
            if(f3 == 0.0F)
            {
                f3 = f6;
            }
            scaleX = f3;
            if(f4 != 0.0F)
            {
                f6 = f4;
            }
            scaleY = f6;
            angle = f5;
        }

        public void set(float f, float f1, boolean flag, float f2, boolean flag1, float f3, float f4, 
                boolean flag2, float f5)
        {
            float f6 = 1.0F;
            xOff = f;
            yOff = f1;
            updateScale = flag;
            if(f2 == 0.0F)
            {
                f2 = f6;
            }
            scale = f2;
            updateScaleXY = flag1;
            if(f3 == 0.0F)
            {
                f3 = f6;
            }
            scaleX = f3;
            if(f4 != 0.0F)
            {
                f6 = f4;
            }
            scaleY = f6;
            updateAngle = flag2;
            angle = f5;
        }

        public PositionAndScale()
        {
        }
    }


    private static final float SCREEN_MARGIN = 100F;
    private static final int UI_MODE_ANISOTROPIC_SCALE = 2;
    private static final int UI_MODE_ROTATE = 1;
    private int alpha;
    private float angle;
    private Bitmap bitmap;
    private int borderColor;
    private int borderSize;
    private float centerX;
    private float centerY;
    private int displayHeight;
    private int displayWidth;
    private boolean firstLoad;
    private boolean hasBorder;
    private int height;
    int mPosition[];
    private int mUIMode;
    private float maxX;
    private float maxY;
    private float minX;
    private float minY;
    private Paint paint;
    private int position;
    private float scaleX;
    private float scaleY;
    private int shape;
    private boolean sticker;
    private int width;

    public ImageObject(Bitmap bitmap1, Resources resources)
    {
        mUIMode = 1;
        borderSize = 12;
        alpha = 255;
        borderColor = -1;
        paint = new Paint();
        shape = 0;
        bitmap = bitmap1;
        firstLoad = true;
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
        maxX = f9;
        maxY = f10;
        if(firstLoad)
        {
            maxX = bitmap.getWidth();
            maxY = bitmap.getHeight();
            minX = mPosition[0];
            minY = mPosition[1];
            maxX = minX + maxX;
            maxY = minY + maxY;
            centerX = minX + (maxX - minX) / 2.0F;
            centerY = minY + (maxY - minY) / 2.0F;
            scaleX = 1.0F;
            scaleY = 1.0F;
        }
        firstLoad = false;
        return true;
    }

    public void addBorder()
    {
        setBorder(true);
    }

    public boolean containsPoint(float f, float f1)
    {
        return f >= minX && f <= maxX && f1 >= minY && f1 <= maxY;
    }

    public void draw(Canvas canvas)
    {
        Rect rect;
        paint.setAlpha(alpha);
        canvas.save();
        float f = (maxX + minX) / 2.0F;
        float f1 = (maxY + minY) / 2.0F;
        if(bitmap == null)
        {
            canvas.setBitmap(Bitmap.createBitmap(240, 320, android.graphics.Bitmap.Config.ARGB_8888));
            return;
        }
        rect = new Rect((int)minX, (int)minY, (int)maxX, (int)maxY);
        canvas.translate(f, f1);
        canvas.rotate((180F * angle) / 3.141593F);
        canvas.translate(-f, -f1);
        if(!isBorder()){
        	canvas.drawBitmap(bitmap, null, rect, paint);
            canvas.restore();
            return;
        }
        Paint paint1;
        Path path;
        paint1 = new Paint();
        paint1.setColor(borderColor);
        paint1.setAlpha(alpha);
        path = new Path();
        switch(shape){
        default:
        	break;
        case 0:
        	canvas.drawRect(new Rect((int)minX - borderSize, (int)minY - borderSize, (int)maxX + borderSize, (int)maxY + borderSize), paint1);
        	break;
        }
        if(shape != 0)
        {
            canvas.drawPath(path, paint1);
        }
        canvas.drawBitmap(bitmap, null, rect, paint);
        canvas.restore();
        return;
    }

    public int getAlpha()
    {
        return alpha;
    }

    public float getAngle()
    {
        return angle;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public int getBorderColor()
    {
        return borderColor;
    }

    public int getBorderSize()
    {
        return borderSize;
    }

    public float getCenterX()
    {
        return centerX;
    }

    public float getCenterY()
    {
        return centerY;
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

    public int getPosition()
    {
        return position;
    }

    public float getScaleX()
    {
        return scaleX;
    }

    public float getScaleY()
    {
        return scaleY;
    }

    public int getShape()
    {
        return shape;
    }

    public int getWidth()
    {
        return width;
    }

    public boolean isBorder()
    {
        return hasBorder;
    }

    public boolean isSticker()
    {
        return sticker;
    }

    public void load(Resources resources, int ai[])
    {
    	float f;
        float f1;
        float f2;
        float f3;
        getMetrics(resources);
        mPosition = ai;
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        if(!firstLoad){
        	f = centerX;
            f1 = centerY;
            f2 = scaleX;
            f3 = scaleY;
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
        }else{
	        f = 100F + (float)(Math.random() * (double)((float)displayWidth - 200F));
	        f1 = 100F + (float)(Math.random() * (double)((float)displayHeight - 200F));
	        float f4 = (float)(0.20000000000000001D + 0.29999999999999999D * ((double)((float)Math.max(displayWidth, displayHeight) / (float)Math.max(width, height)) * Math.random()));
	        f3 = f4;
	        f2 = f4;
        }
        setPos(f, f1, f2, f3, 0.0F);
    }

    public void setAlpha(int i)
    {
        alpha = i;
    }

    public void setBitmap(Bitmap bitmap1)
    {
        bitmap = bitmap1;
    }

    public void setBorder(boolean flag)
    {
        hasBorder = flag;
    }

    public void setBorderColor(int i)
    {
        borderColor = i;
    }

    public void setBorderSize(int i)
    {
        borderSize = i;
    }

    public void setPaint(Paint paint1)
    {
        paint = paint1;
    }

    public boolean setPos(com.km.photogridbuilder.Objects.Image.PositionAndScale positionandscale)
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

    public void setPosition(int i)
    {
        position = i;
    }

    public void setShape(int i)
    {
        shape = i;
    }

    public void setSticker(boolean flag)
    {
        sticker = flag;
    }

    public void unload()
    {
        bitmap = null;
    }
}
