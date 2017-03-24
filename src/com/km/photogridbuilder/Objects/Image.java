package com.km.photogridbuilder.Objects;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.*;
import android.util.DisplayMetrics;

public class Image
{
    public static class PositionAndScale
    {
        public float angle;
        public float scale;
        public float scaleX;
        public float scaleY;
        public boolean updateAngle;
        public boolean updateScale;
        public boolean updateScaleXY;
        public float xOff;
        public float yOff;

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

        public void set(float f, float f1, float f2, float f3, float f4, float f5)
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
    private float angle;
    private Bitmap bitmap;
    public int borderSize;
    private float centerX;
    private float centerY;
    private boolean clipping;
    private int displayHeight;
    private int displayWidth;
    private boolean firstLoad;
    private boolean fixed;
    private RectF frameRect;
    private int height;
    private boolean isBorder;
    private int mUIMode;
    private float maxX;
    private float maxY;
    private float minX;
    private float minY;
    private Paint paint;
    private RectF rect;
    private float scaleX;
    private float scaleY;
    private String url;
    private int width;

    public Image(Bitmap bitmap1, Resources resources)
    {
        mUIMode = 1;
        fixed = false;
        paint = new Paint();
        clipping = true;
        borderSize = 5;
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
        } else
        {
            centerX = f;
            centerY = f1;
            scaleX = f2;
            scaleY = f3;
            angle = f4;
            minX = f7;
            minY = f8;
            maxX = f9;
            maxY = f10;
            return true;
        }
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
        } else
        {
            centerX = f;
            centerY = f1;
            scaleX = f2;
            scaleY = f3;
            angle = f4;
            minX = f5;
            minY = f7;
            maxX = f6;
            maxY = f8;
            return true;
        }
    }

    public boolean containsPoint(float f, float f1)
    {
        if(clipping)
        {
            return rect.contains((int)f, (int)f1);
        }
        return f >= minX && f <= maxX && f1 >= minY && f1 <= maxY;
    }

    public void draw(Canvas canvas)
    {
        canvas.save();
        float f = (maxX + minX) / 2.0F;
        float f1 = (maxY + minY) / 2.0F;
        if(bitmap == null)
        {
            canvas.setBitmap(Bitmap.createBitmap(240, 320, android.graphics.Bitmap.Config.ARGB_8888));
            return;
        }
        Rect rect1 = new Rect((int)minX, (int)minY, (int)maxX, (int)maxY);
        if(clipping)
        {
            canvas.clipRect(rect);
        }
        canvas.translate(f, f1);
        canvas.rotate((180F * angle) / 3.141593F);
        canvas.translate(-f, -f1);
        if(isBorder())
        {
            Paint paint1 = new Paint();
            paint1.setColor(-1);
            new Path();
            float _tmp = (float)rect1.width();
            float _tmp1 = (float)rect1.height();
            canvas.drawRect(new Rect((int)minX - borderSize, (int)minY - borderSize, (int)maxX + borderSize, (int)maxY + borderSize), paint1);
        }
        canvas.drawBitmap(bitmap, null, rect1, paint);
        canvas.restore();
    }

    public float getAngle()
    {
        return angle;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
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

    public RectF getOriginalRect()
    {
        return frameRect;
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

    public String getUrl()
    {
        return url;
    }

    public int getWidth()
    {
        return width;
    }

    public boolean isBorder()
    {
        return isBorder;
    }

    public boolean isClipping()
    {
        return clipping;
    }

    public boolean isFirstLoad()
    {
        return firstLoad;
    }

    public boolean isFixed()
    {
        return fixed;
    }

    public void load(Resources resources)
    {
        getMetrics(resources);
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        float f;
        float f1;
        float f2;
        float f3;
        if(firstLoad)
        {
            f = 100F + (float)(Math.random() * (double)((float)displayWidth - 200F));
            f1 = 100F + (float)(Math.random() * (double)((float)displayHeight - 200F));
            float f4 = (float)(0.20000000000000001D + 0.29999999999999999D * ((double)((float)Math.max(displayWidth, displayHeight) / (float)Math.max(width, height)) * Math.random()));
            f3 = f4;
            f2 = f4;
            firstLoad = false;
        } else
        {
            f = centerX;
            f1 = centerY;
            f2 = scaleX;
            f3 = scaleY;
        }
        setPos(f, f1, f2, f3, 0.0F);
    }

    public void load(Resources resources, RectF rectf)
    {
        float f;
        float f1;
        float f2;
        float f3;
        float f4;
        float f5;
        rect = rectf;
        f = rectf.centerX() + (float)(bitmap.getWidth() / 2);
        f1 = rectf.centerY() + (float)(bitmap.getHeight() / 2);
        f2 = rectf.centerX() - (float)(bitmap.getWidth() / 2);
        f3 = rectf.centerY() - (float)(bitmap.getHeight() / 2);
        f4 = rectf.centerX();
        f5 = rectf.centerY();
        getMetrics(resources);
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        if(!firstLoad){
        	if(maxX >= 100F){
            	if(minX > (float)displayWidth - 100F)
                {
                    f4 = (float)displayWidth - 100F;
                }
            }else
            	f4 = 100F;
            if(maxY <= 100F)
            {
            	if(minY > (float)displayHeight - 100F)
                {
                    f5 = (float)displayHeight - 100F;
                }
            }else
            	f5 = 100F;
        }else
        	firstLoad = false;
        setPos(f4, f5, 0.0F, 0.0F, 0.0F, f2, f, f3, f1);
    }

    public void setBitmap(Bitmap bitmap1)
    {
        bitmap = bitmap1;
    }

    public void setBorder(boolean flag)
    {
        isBorder = flag;
    }

    public void setClipping(boolean flag)
    {
        clipping = flag;
    }

    public void setFirstLoad(boolean flag)
    {
        firstLoad = flag;
    }

    public void setFixed(boolean flag)
    {
        fixed = flag;
    }

    public void setOriginalRect(RectF rectf)
    {
        frameRect = rectf;
    }

    public boolean setPos(PositionAndScale positionandscale)
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

    public void setUrl(String s)
    {
        url = s;
    }

    public void unload()
    {
        bitmap = null;
    }
}
