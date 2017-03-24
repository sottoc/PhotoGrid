package com.km.photogridbuilder.cut;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;

// Referenced classes of package com.km.photogridbuilder.cut:
//            CustomTouch, Image

public class AdvanceStickerView extends View
    implements CustomTouch.CommonListener
{
    public static interface AdvanceActionListener
    {

        public abstract void onDeleteActionListener(RectF rectf, Path path, ArrayList arraylist);
    }


    private static final float TOUCH_TOLERANCE = 4F;
    private static final int UI_MODE_ANISOTROPIC_SCALE = 2;
    private static final int UI_MODE_ROTATE = 1;
    private RectF bounds;
    private Context context;
    private CustomTouch.PointInfo currTouchPoint;
    public RectF gapRect;
    private boolean iscut;
    PointF last;
    private Bitmap mBmpCloseNormal;
    private Bitmap mBmpCloseSelected;
    private Bitmap mBmpCutNormal;
    private Bitmap mBmpCutSelected;
    private Paint mDrawPaint;
    private ArrayList mImages;
    private ArrayList mListRedo;
    private ArrayList mListUndo;
    private AdvanceActionListener mListener;
    private Paint mPaint;
    private Path mPath;
    private ArrayList mPoints;
    private boolean mShowDebugInfo;
    private int mUIMode;
    private float mX;
    private float mY;
    private Bitmap mbitmap;
    private CustomTouch multiTouchController;
    PointF start;

    public AdvanceStickerView(Context context1)
    {
        this(context1, null);
        context = context1;
        init();
    }

    public AdvanceStickerView(Context context1, AttributeSet attributeset)
    {
        this(context1, attributeset, 0);
        context = context1;
        init();
    }

    public AdvanceStickerView(Context context1, AttributeSet attributeset, int i)
    {
        super(context1, attributeset, i);
        mImages = new ArrayList();
        multiTouchController = new CustomTouch(this);
        currTouchPoint = new CustomTouch.PointInfo();
        mShowDebugInfo = true;
        mUIMode = 1;
        mPaint = new Paint();
        gapRect = new RectF();
        mPoints = new ArrayList();
        mDrawPaint = new Paint();
        last = new PointF();
        start = new PointF();
        bounds = new RectF();
        mListUndo = new ArrayList();
        mListRedo = new ArrayList();
        context = context1;
        init();
    }

    private Bitmap getBitmap(int i, boolean flag)
    {
        android.graphics.BitmapFactory.Options options;
        try
        {
            options = new android.graphics.BitmapFactory.Options();
            options.inDither = true;
            options.inPreferredConfig = android.graphics.Bitmap.Config.RGB_565;
        }
        catch(Exception exception)
        {
            Log.v("ERROR", exception.toString());
            return null;
        }
        if(!flag)
        	options.inScaled = false;
        else
        	options.inScaled = true;
        return BitmapFactory.decodeResource(getResources(), i, options);
    }

    private void printOnScreen(Canvas canvas)
    {
    	float af[];
        float af1[];
        float af2[];
        int i;
        int j;
        if(!currTouchPoint.isDown())
        	return;
        mPaint.setColor(0xff00ff00);
        mPaint.setStrokeWidth(1.0F);
        mPaint.setStyle(android.graphics.Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        af = currTouchPoint.getXs();
        af1 = currTouchPoint.getYs();
        af2 = currTouchPoint.getPressures();
        i = Math.min(currTouchPoint.getNumTouchPoints(), 2);
        j = 0;
        while(j < i){
        	canvas.drawCircle(af[j], af1[j], 2.0F * (20F * af2[j]), mPaint);
            j++;
        }
        if(i == 2)
        {
            mPaint.setStrokeWidth(2.0F);
            canvas.drawLine(af[0], af1[0], af[1], af1[1], mPaint);
        }
    }

    private void touch_move(float f, float f1)
    {
        float f2 = Math.abs(f - mX);
        float f3 = Math.abs(f1 - mY);
        if(f2 >= 4F || f3 >= 4F)
        {
            mPath.quadTo(mX, mY, (f + mX) / 2.0F, (f1 + mY) / 2.0F);
            mX = f;
            mY = f1;
            mPoints.add(new PointF(f, f1));
        }
    }

    private void touch_start(float f, float f1)
    {
        mPoints.clear();
        mPoints.add(new PointF(f, f1));
        mPath.reset();
        mPath.moveTo(f, f1);
        mX = f;
        mY = f1;
        start.x = mX;
        start.y = mY;
    }

    private void touch_up(float f, float f1)
    {
        last.x = mX;
        last.y = mY;
        if(start.equals(f, f1))
        {
            bounds.setEmpty();
            mPath.reset();
            mPoints.clear();
        } else
        {
            mPath.lineTo(mX, mY);
        }
        mPath.computeBounds(bounds, false);
    }

    public void addToRedoList(Image image)
    {
        mListRedo.add(image);
    }

    public void addToUndoList(Image image)
    {
        mListUndo.add(image);
    }

    public void delete(Object obj)
    {
        mImages.remove(obj);
        invalidate();
    }

    public Bitmap getBitmap()
    {
        return mbitmap;
    }

    public Image getDraggableObjectAtPoint(CustomTouch.PointInfo pointinfo)
    {
        float f;
        float f1;
        int i;
        Image image = null;
        f = pointinfo.getX();
        f1 = pointinfo.getY();
        i = -1 + mImages.size();
        while(i >= 0){
        	image = (Image)mImages.get(i);
            if(!image.containsPoint(f, f1))
            {
            	i--;
            	continue;
            }
            if(image.isFixed())
            {
                return null;
            }
        }
        return image;
    }

    public ArrayList getImages()
    {
        return mImages;
    }

    public void getPositionAndScale(Image image, Image.PositionAndScale positionandscale)
    {
        float f = image.getCenterX();
        float f1 = image.getCenterY();
        boolean flag;
        float f2;
        boolean flag1;
        float f3;
        float f4;
        int i;
        boolean flag2;
        if((2 & mUIMode) == 0)
        {
            flag = true;
        } else
        {
            flag = false;
        }
        f2 = (image.getScaleX() + image.getScaleY()) / 2.0F;
        if((2 & mUIMode) != 0)
        {
            flag1 = true;
        } else
        {
            flag1 = false;
        }
        f3 = image.getScaleX();
        f4 = image.getScaleY();
        i = 1 & mUIMode;
        flag2 = false;
        if(i != 0)
        {
            flag2 = true;
        }
        positionandscale.set(f, f1, flag, f2, flag1, f3, f4, flag2, image.getAngle());
    }

    public ArrayList getmListRedo()
    {
        return mListRedo;
    }

    public ArrayList getmListUndo()
    {
        return mListUndo;
    }

    void init()
    {
        setBackgroundResource(0x7f02000f);
        CustomTouch.notRequired = false;
        mPath = new Path();
        mDrawPaint.setStrokeWidth(8F);
        mDrawPaint.setColor(-1);
        mDrawPaint.setStyle(android.graphics.Paint.Style.STROKE);
        mDrawPaint.setPathEffect(new DashPathEffect(new float[] {
            20F, 35F
        }, 0.0F));
    }

    public void init(Image image)
    {
        mImages.add(image);
    }

    public boolean isCutMode()
    {
        return iscut;
    }

    public void loadImages(Context context1, Rect rect)
    {
        android.content.res.Resources resources = context1.getResources();
        int i = mImages.size();
        if(rect == null)
        {
            ((Image)mImages.get(i - 1)).load(resources);
            return;
        } else
        {
            ((Image)mImages.get(i - 1)).load(resources, rect);
            return;
        }
    }

    public void onDoubleTap(Image image, CustomTouch.PointInfo pointinfo)
    {
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if(mbitmap == null)
        {
            mbitmap = Bitmap.createBitmap(getWidth(), getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        }
        if(mbitmap != null)
        {
            if(isCutMode())
            {
                Image image = (Image)mImages.get(0);
                mbitmap = image.getBitmap();
                gapRect.left = image.getMinX();
                gapRect.right = image.getMaxX();
                gapRect.top = image.getMinY();
                gapRect.bottom = image.getMaxY();
            }
            Rect rect = new Rect((int)gapRect.left, (int)gapRect.top, (int)gapRect.right, (int)gapRect.bottom);
            if(false)
            {
                canvas.drawBitmap(mbitmap, null, rect, null);
            }
            mPaint.setColor(0xff0000ff);
        }
        int i = mImages.size();
        int j = 0;
        do
        {
            if(j >= i)
            {
                if(isCutMode() && mPoints.size() > 0)
                {
                    canvas.drawPath(mPath, mDrawPaint);
                    if(bounds != null && mBmpCutNormal != null && !bounds.isEmpty())
                    {
                        canvas.drawBitmap(mBmpCutNormal, bounds.left, bounds.top, null);
                    }
                    if(bounds != null && mBmpCloseNormal != null && !bounds.isEmpty())
                    {
                        canvas.drawBitmap(mBmpCloseNormal, bounds.right, bounds.top, null);
                    }
                }
                if(mShowDebugInfo)
                {
                    printOnScreen(canvas);
                }
                return;
            }
            ((Image)mImages.get(j)).draw(canvas);
            j++;
        } while(true);
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        float f;
        float f1;
        f = motionevent.getX();
        f1 = motionevent.getY();
        if(isCutMode()){
	        switch(motionevent.getAction()){
	        default:
	        	break;
	        case 0:
	        	if(f - bounds.left > 0.0F && f - bounds.left < (float)mBmpCutNormal.getWidth() && f1 - bounds.top > 0.0F && f1 - bounds.top < (float)mBmpCutNormal.getHeight())
	            {
	                mListener.onDeleteActionListener(bounds, mPath, mPoints);
	                bounds.setEmpty();
	                mPath.reset();
	                mPoints.clear();
	                invalidate();
	                return false;
	            }
	            if(f - bounds.right > 0.0F && f - bounds.right < (float)mBmpCloseNormal.getWidth() && f1 - bounds.top > 0.0F && f1 - bounds.top < (float)mBmpCloseNormal.getHeight())
	            {
	                bounds.setEmpty();
	                mPath.reset();
	                mPoints.clear();
	                invalidate();
	                return false;
	            }
	            touch_start(f, f1);
	            invalidate();
	            break;
	        case 1:
	        	touch_up(f, f1);
	            invalidate();
	            break;
	        case 2:
	        	touch_move(f, f1);
	            invalidate();
	            break;
	        }
        }
        if(!isCutMode())
            return multiTouchController.onTouchEvent(motionevent);
        else
            return true;
    }

    public Image putImages(Image image)
    {
        Image image1 = (Image)mImages.remove(0);
        mImages.add(image);
        invalidate();
        return image1;
    }

    public void selectObject(Image image, CustomTouch.PointInfo pointinfo)
    {
        currTouchPoint.set(pointinfo);
        if(image != null)
        {
            mImages.remove(image);
            mImages.add(image);
        }
        invalidate();
    }

    public int setBitmap(Bitmap bitmap)
    {
        if(bitmap.getWidth() > bitmap.getHeight())
        {
            mbitmap = bitmap;
            return 0;
        } else
        {
            mbitmap = bitmap;
            return 1;
        }
    }

    public void setMode(boolean flag)
    {
        iscut = flag;
    }

    public void setOnAdvanceActionListener(AdvanceActionListener advanceactionlistener)
    {
        mListener = advanceactionlistener;
    }

    public boolean setPositionAndScale(Image image, Image.PositionAndScale positionandscale, CustomTouch.PointInfo pointinfo)
    {
        currTouchPoint.set(pointinfo);
        boolean flag = image.setPos(positionandscale);
        if(flag)
        {
            invalidate();
        }
        return flag;
    }

    public void setmListRedo(ArrayList arraylist)
    {
        mListRedo = arraylist;
    }

    public void setmListUndo(ArrayList arraylist)
    {
        mListUndo = arraylist;
    }

    public void trackballClicked()
    {
        mUIMode = (1 + mUIMode) % 3;
        invalidate();
    }

    public void unloadImage()
    {
        int i = mImages.size();
        if(i > 0)
        {
            mImages.remove(i - 1);
        }
    }

    public void unloadImages()
    {
        int i = mImages.size();
        int j = 0;
        do
        {
            if(j >= i)
            {
                return;
            }
            ((Image)mImages.get(j)).unload();
            j++;
        } while(true);
    }
}
