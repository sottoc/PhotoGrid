package com.km.photogridbuilder.path;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.km.photogridbuilder.Objects.AddButton;
import com.km.photogridbuilder.bean.Constant;
import com.km.photogridbuilder.bean.Drawing;
import com.km.photogridbuilder.util.ImageObject;
import java.util.ArrayList;
import java.util.Iterator;

// Referenced classes of package com.km.photogridbuilder.path:
//            CustomTouchPath, TextObject, ImageObjectPath

public class StickerViewPath extends View
    implements CustomTouchPath.CommonListener
{
    public static interface ClickListener
    {

        public abstract void onClickListener(int i, int j);
    }

    public static interface TapListener
    {

        public abstract void onDoubleTapListener(Object obj, CustomTouchPath.PointInfo pointinfo, int i);
    }


    private static final int UI_MODE_ANISOTROPIC_SCALE = 2;
    private static final int UI_MODE_ROTATE = 1;
    private Bitmap bg_btn;
    private CustomTouchPath.PointInfo currTouchPoint;
    private int drawColor;
    public Paint drawPaint;
    public RectF gapRect;
    private boolean isButtonSet;
    private boolean isFreHandDrawMode;
    public boolean isSaveClicked;
    private int layout;
    public ArrayList mAddButtons;
    private ClickListener mCLickListener;
    Context mContext;
    public ArrayList mDrawingList;
    private Bitmap mFrameBitmap;
    private ArrayList mImages;
    private TapListener mListener;
    private float mOldX;
    private float mOldY;
    private Paint mPaint;
    private boolean mShowDebugInfo;
    private int mType;
    private int mUIMode;
    private Bitmap mbitmap;
    private CustomTouchPath multiTouchController;
    public Path path;
    private Point point;
    private int strokeWidth;
    private Bitmap textureBitmap;
    private ArrayList undonePaths;

    public StickerViewPath(Context context)
    {
        this(context, null);
        mContext = context;
    }

    public StickerViewPath(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
        mContext = context;
    }

    public StickerViewPath(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        mImages = new ArrayList();
        multiTouchController = new CustomTouchPath(this);
        currTouchPoint = new CustomTouchPath.PointInfo();
        mShowDebugInfo = true;
        mUIMode = 1;
        mPaint = new Paint();
        gapRect = new RectF();
        mAddButtons = new ArrayList();
        isButtonSet = false;
        drawColor = -1;
        strokeWidth = 10;
        isSaveClicked = false;
        mContext = context;
        drawPaint = new Paint();
        drawPaint.setStrokeWidth(strokeWidth);
        drawPaint.setAntiAlias(true);
        drawPaint.setDither(true);
        drawPaint.setStyle(android.graphics.Paint.Style.STROKE);
        drawPaint.setStrokeJoin(android.graphics.Paint.Join.ROUND);
        drawPaint.setStrokeCap(android.graphics.Paint.Cap.ROUND);
        drawPaint.setColor(drawColor);
        path = new Path();
        mDrawingList = new ArrayList();
        undonePaths = new ArrayList();
    }

    private void onTouchDown(MotionEvent motionevent)
    {
        path.reset();
        path.moveTo(motionevent.getX(), motionevent.getY());
        mOldX = motionevent.getX();
        mOldY = motionevent.getY();
    }

    private void onTouchMove(MotionEvent motionevent)
    {
        path.quadTo(mOldX, mOldY, (motionevent.getX() + mOldX) / 2.0F, (motionevent.getY() + mOldY) / 2.0F);
        mOldX = motionevent.getX();
        mOldY = motionevent.getY();
    }

    private void onTouchUp(MotionEvent motionevent)
    {
        path.lineTo(mOldX, mOldY);
        mDrawingList.add(new Drawing(path, strokeWidth, drawColor, new Paint(drawPaint)));
        path = new Path();
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

    public void addButton(int i)
    {
        ((AddButton)mAddButtons.get(i)).done = false;
    }

    public void addButtons(int i, Bitmap bitmap, Point point1)
    {
        layout = i;
        bg_btn = bitmap;
        point = point1;
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

    public Object getDraggableObjectAtPoint(CustomTouchPath.PointInfo pointinfo)
    {
        float f;
        float f1;
        int i;
        int j;
        Object obj = null;
        f = pointinfo.getX();
        f1 = pointinfo.getY();
        i = mImages.size();
        j = i - 1;
        while(j >= 0){
        	obj = mImages.get(j);
            if((obj instanceof TextObject) && ((TextObject)obj).containsPoint(f, f1))
            	return obj;
            j--;
        }
        int k = i - 1;
        while(k >= 0){
        	obj = mImages.get(k);
            if((obj instanceof ImageObjectPath) && ((ImageObjectPath)obj).isSticker() && ((ImageObjectPath)obj).containsPoint(f, f1))
            	return obj;
            k--;
        }
        int l = i - 1;
        while(l >= 0){
        	obj = mImages.get(l);
            if((obj instanceof ImageObjectPath) && !((ImageObjectPath)obj).isSticker() && ((ImageObjectPath)obj).containsPoint(f, f1))
            	return obj;
            l--;
        }
        return obj;
    }

    public Bitmap getFrame()
    {
        return mFrameBitmap;
    }

    public ArrayList getImages()
    {
        return mImages;
    }

    public void getPositionAndScale(Object obj, ImageObjectPath.PositionAndScale positionandscale)
    {
        if(obj instanceof TextObject)
        {
            TextObject textobject = (TextObject)obj;
            float f10 = textobject.getCenterX();
            float f11 = textobject.getCenterY();
            boolean flag6;
            float f12;
            boolean flag7;
            float f13;
            float f14;
            int k;
            boolean flag8;
            if((2 & mUIMode) == 0)
            {
                flag6 = true;
            } else
            {
                flag6 = false;
            }
            f12 = (textobject.getScaleX() + textobject.getScaleY()) / 2.0F;
            if((2 & mUIMode) != 0)
            {
                flag7 = true;
            } else
            {
                flag7 = false;
            }
            f13 = textobject.getScaleX();
            f14 = textobject.getScaleY();
            k = 1 & mUIMode;
            flag8 = false;
            if(k != 0)
            {
                flag8 = true;
            }
            positionandscale.set(f10, f11, flag6, f12, flag7, f13, f14, flag8, textobject.getAngle());
            return;
        }
        if(obj instanceof ImageObjectPath)
        {
            ImageObjectPath imageobjectpath1 = (ImageObjectPath)obj;
            float f5 = imageobjectpath1.getCenterX();
            float f6 = imageobjectpath1.getCenterY();
            boolean flag3;
            float f7;
            boolean flag4;
            float f8;
            float f9;
            int j;
            boolean flag5;
            if((2 & mUIMode) == 0)
            {
                flag3 = true;
            } else
            {
                flag3 = false;
            }
            f7 = (imageobjectpath1.getScaleX() + imageobjectpath1.getScaleY()) / 2.0F;
            if((2 & mUIMode) != 0)
            {
                flag4 = true;
            } else
            {
                flag4 = false;
            }
            f8 = imageobjectpath1.getScaleX();
            f9 = imageobjectpath1.getScaleY();
            j = 1 & mUIMode;
            flag5 = false;
            if(j != 0)
            {
                flag5 = true;
            }
            positionandscale.set(f5, f6, flag3, f7, flag4, f8, f9, flag5, imageobjectpath1.getAngle());
            return;
        }
        ImageObjectPath imageobjectpath = (ImageObjectPath)obj;
        float f = imageobjectpath.getCenterX();
        float f1 = imageobjectpath.getCenterY();
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
        f2 = (imageobjectpath.getScaleX() + imageobjectpath.getScaleY()) / 2.0F;
        if((2 & mUIMode) != 0)
        {
            flag1 = true;
        } else
        {
            flag1 = false;
        }
        f3 = imageobjectpath.getScaleX();
        f4 = imageobjectpath.getScaleY();
        i = 1 & mUIMode;
        flag2 = false;
        if(i != 0)
        {
            flag2 = true;
        }
        positionandscale.set(f, f1, flag, f2, flag1, f3, f4, flag2, imageobjectpath.getAngle());
    }

    public Bitmap getTexture()
    {
        return textureBitmap;
    }

    public void init(Object obj)
    {
        mImages.add(obj);
    }

    public void loadImages(Context context, RectF rectf, Path path1)
    {
        android.content.res.Resources resources = context.getResources();
        int i = mImages.size();
        if(rectf == null)
        {
            if(mImages.get(i - 1) instanceof ImageObjectPath)
            {
                ((ImageObjectPath)mImages.get(i - 1)).load(resources);
            }
            return;
        }
        if(path1 != null)
        {
            if(mImages.get(i - 1) instanceof ImageObjectPath)
            {
                ((ImageObjectPath)mImages.get(i - 1)).load(resources, rectf, path1);
                return;
            } else
            {
                ((TextObject)mImages.get(i - 1)).load(resources, rectf);
                return;
            }
        }
        if(mImages.get(i - 1) instanceof ImageObjectPath)
        {
            ((ImageObjectPath)mImages.get(i - 1)).load(resources, rectf);
            return;
        } else
        {
            ((TextObject)mImages.get(i - 1)).load(resources, rectf);
            return;
        }
    }

    public void loadImages(Context context, boolean flag, int ai[])
    {
        android.content.res.Resources resources = context.getResources();
        int i = mImages.size();
        if(flag)
        {
            if(mImages.get(i - 1) instanceof ImageObjectPath)
            {
                ((ImageObjectPath)mImages.get(i - 1)).load(resources);
            }
        } else
        {
            int j = 0;
            while(j < mImages.size()) 
            {
                if(mImages.get(j) instanceof ImageObjectPath)
                {
                    if(((ImageObjectPath)mImages.get(j)).isSticker() || mImages.size() == j + 1)
                    {
                        ArrayList arraylist1 = mImages;
                        if(j != 0)
                        {
                            j--;
                        }
                        ((ImageObject)arraylist1.get(j)).load(resources, ai);
                        return;
                    }
                } else
                if(((TextObject)mImages.get(j)).isSticker() || mImages.size() == j + 1)
                {
                    ArrayList arraylist = mImages;
                    if(j != 0)
                    {
                        j--;
                    }
                    ((ImageObject)arraylist.get(j)).load(resources, ai);
                    return;
                }
                j++;
            }
        }
    }

    public PointF mapPoints(Point point1, float f, float f1)
    {
        PointF pointf = new PointF();
        Bitmap bitmap = getBitmap();
        float f2 = point1.x;
        float f3 = point1.y;
        int i = bitmap.getHeight();
        int j = bitmap.getWidth();
        int k = (int)(f3 - 2.0F * gapRect.top);
        int l = (int)(f2 - 2.0F * gapRect.left);
        float f4 = 1.0F * ((1.0F * (float)k) / (float)i);
        float f5 = f * (1.0F * ((1.0F * (float)l) / (float)j)) + gapRect.left;
        float f6 = f1 * f4 + gapRect.top;
        pointf.x = f5;
        pointf.y = f6;
        return pointf;
    }

    public void onClickRedo()
    {
        if(undonePaths.size() > 0)
        {
            mDrawingList.add((Drawing)undonePaths.remove(-1 + undonePaths.size()));
            invalidate();
        }
    }

    public void onClickUndo()
    {
        if(mDrawingList.size() > 0)
        {
            undonePaths.add((Drawing)mDrawingList.remove(-1 + mDrawingList.size()));
            invalidate();
        }
    }

    public void onDoubleTap(Object obj, CustomTouchPath.PointInfo pointinfo)
    {
        mListener.onDoubleTapListener(obj, pointinfo, -1);
    }

    protected void onDraw(Canvas canvas)
    {
        int i;
        int j;
        int k;
        Iterator iterator;
        int l;
        float af[][];
        int i1;
        super.onDraw(canvas);
        if(mbitmap == null && textureBitmap != null)
        {
            float f5 = 1.0F * ((1.0F * (float)textureBitmap.getWidth()) / (float)textureBitmap.getHeight());
            float f6 = (1.0F * (float)getWidth()) / f5;
            float f7 = getWidth();
            gapRect.top = ((float)getHeight() - f6) / 2.0F;
            gapRect.bottom = ((float)getHeight() - f6) / 2.0F;
            if(f6 > 1.0F * (float)getHeight())
            {
                f6 = getHeight();
                f7 = f5 * (1.0F * (float)getHeight());
                gapRect.left = ((float)getWidth() - f7) / 2.0F;
                gapRect.right = ((float)getWidth() - f7) / 2.0F;
                gapRect.top = 0.0F;
                gapRect.bottom = 0.0F;
            }
            Rect rect1 = new Rect((int)gapRect.left, (int)gapRect.top, (int)(f7 + gapRect.left), (int)(f6 + gapRect.top));
            canvas.drawBitmap(textureBitmap, null, rect1, null);
        }
        i = mImages.size();
        j = 0;
        while(j < i){
        	if(mImages.get(j) instanceof TextObject)
            {
                ((TextObject)mImages.get(j)).draw(canvas);
            } else
            if((mImages.get(j) instanceof ImageObjectPath) && !((ImageObjectPath)mImages.get(j)).isSticker())
            {
                ((ImageObjectPath)mImages.get(j)).draw(canvas);
            }
            j++;
        }
        if(mbitmap != null){
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
	        canvas.clipRect(rect);
	        canvas.drawBitmap(mbitmap, null, rect, null);
	        if(textureBitmap != null)
	        {
	            canvas.drawBitmap(textureBitmap, null, rect, null);
	        }
	        if(mFrameBitmap != null)
	        {
	            canvas.drawBitmap(mFrameBitmap, null, rect, null);
	        }
	        if(!isSaveClicked){
		        if(!isButtonSet){
			        if(mType == 102)
			        {
			            af = Constant.getShapeAddForLayout(layout);
			        } else
			        {
			            af = Constant.getIrrgularAddForLayout(layout);
			        }
			        if(af != null){
				        i1 = 0;
				        while(i1 < af.length){
				        	float f3 = af[i1][0];
				            float f4 = af[i1][1];
				            PointF pointf = mapPoints(point, f3, f4);
				            AddButton addbutton = new AddButton(bg_btn, pointf.x, pointf.y);
				            addbutton.buttonNo = i1 + 1;
				            addbutton.totalButton = af.length;
				            mAddButtons.add(addbutton);
				            i1++;
				        }
				        isButtonSet = true;
			        }
		        }
		        if(!isSaveClicked){
			        l = 0;
			        while(l < mAddButtons.size()){
			        	if(!((AddButton)mAddButtons.get(l)).done)
			            {
			                ((AddButton)mAddButtons.get(l)).draw(canvas);
			            }
			            l++;
			        }
		        }
	        }
        }
        k = 0;
        while(k < i){
        	if(mImages.get(k) instanceof TextObject)
            {
                ((TextObject)mImages.get(k)).draw(canvas);
            } else
            if((mImages.get(k) instanceof ImageObjectPath) && ((ImageObjectPath)mImages.get(k)).isSticker())
            {
                ((ImageObjectPath)mImages.get(k)).draw(canvas);
            }
            k++;
        }
        iterator = mDrawingList.iterator();
        while(true){
	        if(!iterator.hasNext())
	        {
	            canvas.drawPath(path, drawPaint);
	            if(mShowDebugInfo)
	            {
	                printOnScreen(canvas);
	            }
	            return;
	        }
	        Drawing drawing = (Drawing)iterator.next();
	        canvas.drawPath(drawing.getPath(), drawing.getPaint());
        }
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        if(isFreHandDrawMode){
	        switch(motionevent.getAction()){
	        default:
	        	break;
	        case 0:
	        	onTouchDown(motionevent);
	        	break;
	        case 1:
	        	onTouchUp(motionevent);
	        	break;
	        case 2:
	        	onTouchMove(motionevent);
	        	break;
	        }
	        invalidate();
	        return true;
        }
        if(motionevent.getAction() == 0){
	        float f;
	        float f1;
	        int i;
	        f = motionevent.getRawX();
	        f1 = motionevent.getRawY();
	        i = 0;
	        while(i < mAddButtons.size()){
	        	AddButton addbutton = (AddButton)mAddButtons.get(i);
	            if(addbutton != null && addbutton.containsPoint(f, f1).booleanValue() && !addbutton.done)
	            {
	                mCLickListener.onClickListener(layout, addbutton.buttonNo);
	                return true;
	            }
	            i++;
	        }
        }
        return multiTouchController.onTouchEvent(motionevent);
    }

    public void removeButton(int i)
    {
        ((AddButton)mAddButtons.get(i)).done = true;
    }

    public void selectObject(Object obj, CustomTouchPath.PointInfo pointinfo)
    {
        currTouchPoint.set(pointinfo);
        if(obj != null)
        {
            mImages.remove(obj);
            mImages.add(obj);
        }
        invalidate();
    }

    public int setBitmap(Bitmap bitmap)
    {
        mbitmap = bitmap;
        invalidate();
        return 0;
    }

    public void setBrushSize(int i)
    {
        strokeWidth = i;
        drawPaint.setStrokeWidth(strokeWidth);
        invalidate();
    }

    public void setDrawColor(int i)
    {
        drawColor = i;
        drawPaint.setColor(drawColor);
        invalidate();
    }

    public void setFrame(Bitmap bitmap)
    {
        mFrameBitmap = bitmap;
    }

    public void setFreHandDrawMode(boolean flag)
    {
        isFreHandDrawMode = flag;
    }

    public void setOnButtonClickListener(ClickListener clicklistener)
    {
        mCLickListener = clicklistener;
    }

    public void setOnTapListener(TapListener taplistener)
    {
        mListener = taplistener;
    }

    public boolean setPositionAndScale(Object obj, ImageObjectPath.PositionAndScale positionandscale, CustomTouchPath.PointInfo pointinfo)
    {
        currTouchPoint.set(pointinfo);
        boolean flag;
        if(obj instanceof ImageObjectPath)
        {
            flag = ((ImageObjectPath)obj).setPos(positionandscale);
        } else
        {
            flag = ((TextObject)obj).setPos(positionandscale);
        }
        if(flag)
        {
            invalidate();
        }
        return flag;
    }

    public void setTexture(Bitmap bitmap)
    {
        textureBitmap = bitmap;
    }

    public void setType(int i)
    {
        mType = i;
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
            if(mImages.get(j) instanceof ImageObjectPath)
            {
                ((ImageObjectPath)mImages.get(j)).unload();
            } else
            {
                ((TextObject)mImages.get(j)).unload();
            }
            j++;
        } while(true);
    }
}
