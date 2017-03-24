package com.km.photogridbuilder.cut;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;

// Referenced classes of package com.km.photogridbuilder.cut:
//            CustomTouch, Image

public class CutView extends View
    implements CustomTouch.CommonListener
{
    public static interface ActionListener
    {

        public abstract void onCutActionListener(RectF rectf, Path path, ArrayList arraylist);

        public abstract void onDoubleTapListener(Image image, CustomTouch.PointInfo pointinfo);
    }

    public enum ModifyMode
    {
    	None("None", 0),
        Move("Move", 1),
        Grow("Grow", 2);
        static 
        {
            ModifyMode amodifymode[] = new ModifyMode[3];
            amodifymode[0] = None;
            amodifymode[1] = Move;
            amodifymode[2] = Grow;
        }

        private ModifyMode(String s, int i)
        {
        }
    }


    public static final int GROW_BOTTOM_EDGE = 16;
    public static final int GROW_LEFT_EDGE = 2;
    public static final int GROW_NONE = 1;
    public static final int GROW_RIGHT_EDGE = 4;
    public static final int GROW_TOP_EDGE = 8;
    public static final int MOVE = 32;
    private static final int STAR_ANGLE_HALF = 18;
    private static final int STAR_OPP_ANGLE = 72;
    private static final float TOUCH_TOLERANCE = 4F;
    private static final int UI_MODE_ANISOTROPIC_SCALE = 2;
    private static final int UI_MODE_ROTATE = 1;
    private RectF bounds;
    private Context context;
    private CustomTouch.PointInfo currTouchPoint;
    private CutActivity.CUTMODE current_cut_mode;
    private BitmapDrawable drawable;
    public RectF gapRect;
    private boolean isCollageMode;
    private boolean iscut;
    PointF last;
    private Bitmap mBmpCloseNormal;
    private Bitmap mBmpCloseSelected;
    private Bitmap mBmpCutNormal;
    private Bitmap mBmpCutSelected;
    private Paint mDrawPaint;
    private ArrayList mImages;
    private float mInitialAspectRatio;
    private float mLastX;
    private float mLastY;
    private ActionListener mListener;
    private boolean mMaintainAspectRatio;
    private int mMotionEdge;
    private Drawable mMoveDrawableCenter;
    private Paint mPaint;
    private Path mPath;
    private ArrayList mPoints;
    private Drawable mResizeDrawableHeight;
    private Drawable mResizeDrawableWidth;
    private boolean mShowDebugInfo;
    private int mUIMode;
    private float mX;
    private float mY;
    private Bitmap mbitmap;
    private Object mode;
    private CustomTouch multiTouchController;
    private boolean noCropping;
    private int paddingBottom;
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    PointF start;

    public CutView(Context context1)
    {
        this(context1, null);
        setBackgroundColor(0xff000000);
        context = context1;
        init();
    }

    public CutView(Context context1, AttributeSet attributeset)
    {
        this(context1, attributeset, 0);
        context = context1;
        init();
    }

    public CutView(Context context1, AttributeSet attributeset, int i)
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
        paddingLeft = 0;
        paddingRight = 0;
        paddingTop = 0;
        paddingBottom = 0;
        noCropping = false;
        context = context1;
        init();
    }

    private void drawShapes()
    {
        if(current_cut_mode != CutActivity.CUTMODE.FREE_FORM)
        {
            mPath.reset();
        }
        Rect rect = new Rect((int)bounds.left, (int)bounds.top, (int)bounds.right, (int)bounds.bottom);
        if(current_cut_mode == CutActivity.CUTMODE.HEART)
        {
            int j1 = rect.width();
            int k1 = rect.height();
            float f2 = 1.0F * ((1.0F * (float)j1) / 130F);
            float f3 = 1.0F * ((1.0F * (float)k1) / 120F);
            float f4 = (float)rect.left + 65F * f2;
            float f5 = (float)rect.top + 20F * f3;
            mPath.moveTo(f4, f5);
            float f6 = (float)rect.left + 65F * f2;
            float f7 = (float)rect.top + 17F * f3;
            float f8 = (float)rect.left + 60F * f2;
            float f9 = (float)rect.top + 5F * f3;
            float f10 = (float)rect.left + 45F * f2;
            float f11 = (float)rect.top + 5F * f3;
            mPath.cubicTo(f6, f7, f8, f9, f10, f11);
            float f12 = (float)rect.left + 0.0F * f2;
            float f13 = (float)rect.top + 5F * f3;
            float f14 = (float)rect.left + 0.0F * f2;
            float f15 = (float)rect.top + 42.5F * f3;
            float f16 = (float)rect.left + 0.0F * f2;
            float f17 = (float)rect.top + 42.5F * f3;
            mPath.cubicTo(f12, f13, f14, f15, f16, f17);
            float f18 = (float)rect.left + 0.0F * f2;
            float f19 = (float)rect.top + 80F * f3;
            float f20 = (float)rect.left + 20F * f2;
            float f21 = (float)rect.top + 102F * f3;
            float f22 = (float)rect.left + 65F * f2;
            float f23 = (float)rect.top + 120F * f3;
            mPath.cubicTo(f18, f19, f20, f21, f22, f23);
            float f24 = (float)rect.left + 110F * f2;
            float f25 = (float)rect.top + 102F * f3;
            float f26 = (float)rect.left + 130F * f2;
            float f27 = (float)rect.top + 80F * f3;
            float f28 = (float)rect.left + 130F * f2;
            float f29 = (float)rect.top + 42.5F * f3;
            mPath.cubicTo(f24, f25, f26, f27, f28, f29);
            float f30 = (float)rect.left + 130F * f2;
            float f31 = (float)rect.top + 42.5F * f3;
            float f32 = (float)rect.left + 130F * f2;
            float f33 = (float)rect.top + 5F * f3;
            float f34 = (float)rect.left + 90F * f2;
            float f35 = (float)rect.top + 5F * f3;
            mPath.cubicTo(f30, f31, f32, f33, f34, f35);
            float f36 = (float)rect.left + 75F * f2;
            float f37 = (float)rect.top + 5F * f3;
            float f38 = (float)rect.left + 65F * f2;
            float f39 = (float)rect.top + 17F * f3;
            float f40 = (float)rect.left + 65F * f2;
            float f41 = (float)rect.top + 20F * f3;
            mPath.cubicTo(f36, f37, f38, f39, f40, f41);
            mPath.close();
            return;
        }
        if(current_cut_mode == CutActivity.CUTMODE.STAR)
        {
            int i = rect.width();
            int j = rect.height();
            int k = Math.min(i - paddingLeft - paddingRight, j - paddingTop - paddingBottom);
            double d = (double)k / Math.cos(Math.toRadians(18D));
            double d1 = k;
            double d2 = d1 * Math.tan(Math.toRadians(18D));
            double d3 = d / (2D + Math.cos(Math.toRadians(72D)) + Math.cos(Math.toRadians(72D)));
            double d4 = d3 * Math.cos(Math.toRadians(72D));
            double d5 = d3 * Math.sin(Math.toRadians(72D));
            int l = rect.left + i / 2;
            int i1 = rect.top;
            mPath.moveTo(l, i1);
            mPath.lineTo((int)(d2 + (double)l), (int)(d1 + (double)i1));
            mPath.lineTo((int)((double)l - d4 - d5), (int)(d5 + (double)i1));
            mPath.lineTo((int)(d5 + (d4 + (double)l)), (int)(d5 + (double)i1));
            mPath.lineTo((int)((double)l - d2), (int)(d1 + (double)i1));
            mPath.lineTo(l, i1);
            mPath.close();
            return;
        }
        if(current_cut_mode == CutActivity.CUTMODE.CIRCLE)
        {
            float f = rect.width();
            float f1 = rect.height();
            mPath.addCircle((float)rect.left + f / 2.0F, (float)rect.top + f1 / 2.0F, f / 2.0F, android.graphics.Path.Direction.CW);
            return;
        }
        if(current_cut_mode == CutActivity.CUTMODE.SQUARE)
        {
            Path path = mPath;
            RectF rectf = new RectF(rect);
            path.addRect(rectf, android.graphics.Path.Direction.CW);
            return;
        } else
        {
            CutActivity.CUTMODE _tmp = CutActivity.CUTMODE.FREE_FORM;
            return;
        }
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
        if(!currTouchPoint.isDown())
        	return;
        float af[];
        float af1[];
        float af2[];
        int i;
        int j;
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

    public void clearPath()
    {
        bounds.setEmpty();
        mPath.reset();
        mPoints.clear();
        invalidate();
    }

    public void delete(Image image)
    {
        mImages.remove(image);
        invalidate();
    }

    public Bitmap getBitmap()
    {
        return mbitmap;
    }

    public CutActivity.CUTMODE getCutMode()
    {
        return current_cut_mode;
    }

    public Image getDraggableObjectAtPoint(CustomTouch.PointInfo pointinfo)
    {
        float f;
        float f1;
        int i;
        f = pointinfo.getX();
        f1 = pointinfo.getY();
        i = -1 + mImages.size();
        Image image;
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
            return image;
        }
        return null;
    }

    public int getHit(float f, float f1)
    {
        RectF rectf = bounds;
        int i = 1;
        boolean flag;
        boolean flag1;
        if(f1 >= rectf.top - 20F && f1 < 20F + rectf.bottom)
        {
            flag = true;
        } else
        {
            flag = false;
        }
        if(f >= rectf.left - 20F && f < 20F + rectf.right)
        {
            flag1 = true;
        } else
        {
            flag1 = false;
        }
        if(Math.abs(rectf.left - f) < 20F && flag)
        {
            i |= 2;
        }
        if(Math.abs(rectf.right - f) < 20F && flag)
        {
            i |= 4;
        }
        if(Math.abs(rectf.top - f1) < 20F && flag1)
        {
            i |= 8;
        }
        if(Math.abs(rectf.bottom - f1) < 20F && flag1)
        {
            i |= 0x10;
        }
        if(i == 1 && rectf.contains((int)f, (int)f1))
        {
            i = 32;
        }
        return i;
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

    public Bitmap getTextureBitmap()
    {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        draw(new Canvas(bitmap));
        return bitmap;
    }

    void growBy(float f, float f1)
    {
        float f2 = 25F;
        if(mMaintainAspectRatio){
	        if(f == 0.0F){
	        	if(f1 != 0.0F)
	            {
	                f = f1 * mInitialAspectRatio;
	            }
	        }else
	        	f1 = f / mInitialAspectRatio;
        }
        RectF rectf = bounds;
        rectf.inset(-f, -f1);
        if(rectf.width() < f2)
        {
            rectf.inset(-(f2 - rectf.width()) / 2.0F, 0.0F);
        }
        if(mMaintainAspectRatio)
        {
            f2 /= mInitialAspectRatio;
        }
        if(rectf.height() < f2)
        {
            rectf.inset(0.0F, -(f2 - rectf.height()) / 2.0F);
        }
        invalidate();
    }

    public void handleMotion(int i, float f, float f1)
    {
        int j = -1;
        RectF _tmp = bounds;
        if(i == 1)
        {
            return;
        }
        if(i == 32)
        {
            moveBy(f, f1);
            return;
        }
        if((i & 6) == 0)
        {
            f = 0.0F;
        }
        if((i & 0x18) == 0)
        {
            f1 = 0.0F;
        }
        float f2 = f;
        float f3 = f1;
        int k;
        float f4;
        if((i & 2) != 0)
        {
            k = j;
        } else
        {
            k = 1;
        }
        f4 = f2 * (float)k;
        if((i & 8) == 0)
        {
            j = 1;
        }
        growBy(f4, f3 * (float)j);
    }

    void init()
    {
        Resources resources = context.getResources();
        mResizeDrawableWidth = resources.getDrawable(0x7f0200a6);
        mResizeDrawableHeight = resources.getDrawable(0x7f0200a5);
        mMoveDrawableCenter = resources.getDrawable(0x7f02018f);
        mInitialAspectRatio = 1.0F;
        mMaintainAspectRatio = false;
        current_cut_mode = CutActivity.CUTMODE.FREE_FORM;
        CustomTouch.notRequired = false;
        mPath = new Path();
        mDrawPaint.setStrokeWidth(8F);
        int i = PreferenceManager.getDefaultSharedPreferences(context).getInt("color_cutimage", -1);
        mDrawPaint.setColor(i);
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
        Resources resources = context1.getResources();
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

    void moveBy(float f, float f1)
    {
        bounds.offset(f, f1);
        invalidate();
    }

    public void onDoubleTap(Image image, CustomTouch.PointInfo pointinfo)
    {
        mListener.onDoubleTapListener(image, pointinfo);
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        int k2;
        int l2;
        if(isCollageMode)
        {
            if(drawable != null)
            {
                drawable.setBounds(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()));
                drawable.draw(canvas);
            }
        } else
        {
            if(mbitmap != null)
            {
                float f = 1.0F * ((1.0F * (float)mbitmap.getWidth()) / (float)mbitmap.getHeight());
                float f1 = (1.0F * (float)getWidth()) / f;
                float f2 = getWidth();
                gapRect.top = ((float)getHeight() - f1) / 2.0F;
                gapRect.bottom = ((float)getHeight() - f1) / 2.0F;
                if(f1 > 1.0F * (float)getHeight())
                {
                    f1 = getHeight();
                    f2 = f * (1.0F * (float)getHeight());
                    gapRect.left = ((float)getWidth() - f2) / 2.0F;
                    gapRect.right = ((float)getWidth() - f2) / 2.0F;
                    gapRect.top = 0.0F;
                    gapRect.bottom = 0.0F;
                    Log.e("View", (new StringBuilder(String.valueOf(getHeight()))).append(" height : newHeight").append(f1).toString());
                }
                Rect rect = new Rect((int)gapRect.left, (int)gapRect.top, (int)(f2 + gapRect.left), (int)(f1 + gapRect.top));
                canvas.drawBitmap(mbitmap, null, rect, null);
                mPaint.setColor(0xff0000ff);
            }
            if(isCutMode())
            {
                drawShapes();
                canvas.drawPath(mPath, mDrawPaint);
                if(bounds != null && mBmpCutNormal != null)
                {
                    bounds.isEmpty();
                }
                if(bounds != null && mBmpCloseNormal != null)
                {
                    bounds.isEmpty();
                }
                if(mode == ModifyMode.Grow || mode == ModifyMode.Move)
                {
                    int i = (int)(1.0F + bounds.left);
                    int j = (int)(1.0F + bounds.right);
                    int k = (int)(4F + bounds.top);
                    int l = (int)(3F + bounds.bottom);
                    int i1 = mResizeDrawableWidth.getIntrinsicWidth() / 2;
                    int j1 = mResizeDrawableWidth.getIntrinsicHeight() / 2;
                    int k1 = mResizeDrawableHeight.getIntrinsicHeight() / 2;
                    int l1 = mResizeDrawableHeight.getIntrinsicWidth() / 2;
                    int i2 = (int)(bounds.left + (bounds.right - bounds.left) / 2.0F);
                    int j2 = (int)(bounds.top + (bounds.bottom - bounds.top) / 2.0F);
                    if(mode == ModifyMode.Move)
                    {
                        mMoveDrawableCenter.setBounds(i2 - i1, j2 - j1, i2 + i1, j2 + j1);
                        mMoveDrawableCenter.draw(canvas);
                    }
                    mResizeDrawableWidth.setBounds(i - i1, j2 - j1, i + i1, j2 + j1);
                    mResizeDrawableWidth.draw(canvas);
                    mResizeDrawableWidth.setBounds(j - i1, j2 - j1, j + i1, j2 + j1);
                    mResizeDrawableWidth.draw(canvas);
                    mResizeDrawableHeight.setBounds(i2 - l1, k - k1, i2 + l1, k + k1);
                    mResizeDrawableHeight.draw(canvas);
                    mResizeDrawableHeight.setBounds(i2 - l1, l - k1, i2 + l1, l + k1);
                    mResizeDrawableHeight.draw(canvas);
                }
            }
        }
        k2 = mImages.size();
        l2 = 0;
        do
        {
            if(l2 >= k2)
            {
                if(mShowDebugInfo)
                {
                    printOnScreen(canvas);
                }
                return;
            }
            ((Image)mImages.get(l2)).draw(canvas);
            l2++;
        } while(true);
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        float f;
        float f1;
        int i;
        ModifyMode modifymode;
        f = motionevent.getX();
        f1 = motionevent.getY();
        if(isCutMode())
        {
            motionevent.getAction();
        }
        if(!(!isCutMode() || current_cut_mode != CutActivity.CUTMODE.FREE_FORM)){
	        switch(motionevent.getAction()){
	        default:
	        	break;
	        case 0:
	        	touch_start(f, f1);
	            invalidate();
	            break;
	        case 1:
	        	touch_up(f, f1);
	            mListener.onCutActionListener(bounds, mPath, mPoints);
	            invalidate();
	            break;
	        case 2:
	        	touch_move(f, f1);
	            invalidate();
	            break;
	        }
        }
        if(!(current_cut_mode != CutActivity.CUTMODE.CIRCLE && current_cut_mode != CutActivity.CUTMODE.HEART && current_cut_mode != CutActivity.CUTMODE.SQUARE && current_cut_mode != CutActivity.CUTMODE.STAR)){
	        switch(motionevent.getAction()){
	        default:
	        	break;
	        case 0:
	        	i = getHit(motionevent.getX(), motionevent.getY());
	            if(i != 1)
	            {
	                mMotionEdge = i;
	                mLastX = motionevent.getX();
	                mLastY = motionevent.getY();
	                if(i == 32)
	                {
	                    modifymode = ModifyMode.Move;
	                } else
	                {
	                    modifymode = ModifyMode.Grow;
	                }
	                mode = modifymode;
	            }
	            break;
	        case 1:
	        	mode = ModifyMode.None;
	        	break;
	        case 2:
	        	if(mode != ModifyMode.None)
	            {
	                handleMotion(mMotionEdge, motionevent.getX() - mLastX, motionevent.getY() - mLastY);
	                mLastX = motionevent.getX();
	                mLastY = motionevent.getY();
	            }
	        	break;
	        }
        }
        invalidate();
        if(!isCutMode())
            return multiTouchController.onTouchEvent(motionevent);
        else
            return true;
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

    public void setBackground()
    {
    }

    public void setBackgroundTexture(int i)
    {
        isCollageMode = true;
        drawable = new BitmapDrawable(context.getResources(), BitmapFactory.decodeResource(getResources(), i));
        drawable.setTileModeXY(android.graphics.Shader.TileMode.REPEAT, android.graphics.Shader.TileMode.REPEAT);
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

    public void setCutMode(CutActivity.CUTMODE cutmode)
    {
        current_cut_mode = cutmode;
        mPath.reset();
        if(cutmode == CutActivity.CUTMODE.FREE_FORM)
        {
            bounds.setEmpty();
        }
        if(cutmode == CutActivity.CUTMODE.CIRCLE || cutmode == CutActivity.CUTMODE.HEART || cutmode == CutActivity.CUTMODE.STAR)
        {
            mMaintainAspectRatio = true;
        } else
        {
            mMaintainAspectRatio = false;
        }
        if(cutmode != CutActivity.CUTMODE.FREE_FORM)
        {
            bounds = new RectF(100F, 300F, 400F, 600F);
        }
        invalidate();
    }

    public void setMode(boolean flag)
    {
        iscut = flag;
    }

    public void setOnActionListener(ActionListener actionlistener)
    {
        mListener = actionlistener;
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
