package com.km.photogridbuilder.cut;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;

// Referenced classes of package com.km.photogridbuilder.cut:
//            BitmapUndoRedoManager, Drawing, OnViewLoadListener, BitmapUtil

public class AdvancedView extends View
{

    int centerX;
    int centerY;
    int count;
    private float dx;
    private float dy;
    int height;
    public boolean isAutoMode;
    private boolean isDragging;
    public boolean isFirstTime;
    public boolean isZoom;
    private int mColor;
    private Context mContext;
    private int mLineWidth;
    private OnViewLoadListener mLoadListener;
    private Paint mPaintLine;
    private Path mPath;
    private Bitmap mPickedBitmap;
    private float mScaleFactor;
    private BitmapUndoRedoManager mUndoRedoManager;
    private float mX;
    private float mY;
    private float oldTouchX;
    private float oldTouchY;
    private ArrayList pathsList;
    private float rotate;
    private float rotateTemp;
    private float scale;
    private float tx;
    private float ty;
    int width;
    private float xTranslation;
    private float yTranslation;

    public AdvancedView(Context context)
    {
        super(context);
        mLineWidth = 50;
        pathsList = new ArrayList();
        mColor = 0;
        isZoom = false;
        isFirstTime = false;
        mScaleFactor = 1.0F;
        rotate = 0.0F;
        rotateTemp = 0.0F;
        tx = 0.0F;
        ty = 0.0F;
        count = 0;
        oldTouchX = 0.0F;
        oldTouchY = 0.0F;
        isAutoMode = false;
        dx = 0.0F;
        dy = 0.0F;
        isDragging = false;
        mContext = context;
        init(context);
    }

    public AdvancedView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        mLineWidth = 50;
        pathsList = new ArrayList();
        mColor = 0;
        isZoom = false;
        isFirstTime = false;
        mScaleFactor = 1.0F;
        rotate = 0.0F;
        rotateTemp = 0.0F;
        tx = 0.0F;
        ty = 0.0F;
        count = 0;
        oldTouchX = 0.0F;
        oldTouchY = 0.0F;
        isAutoMode = false;
        dx = 0.0F;
        dy = 0.0F;
        isDragging = false;
        mContext = context;
        init(context);
    }

    public AdvancedView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        mLineWidth = 50;
        pathsList = new ArrayList();
        mColor = 0;
        isZoom = false;
        isFirstTime = false;
        mScaleFactor = 1.0F;
        rotate = 0.0F;
        rotateTemp = 0.0F;
        tx = 0.0F;
        ty = 0.0F;
        count = 0;
        oldTouchX = 0.0F;
        oldTouchY = 0.0F;
        isAutoMode = false;
        dx = 0.0F;
        dy = 0.0F;
        isDragging = false;
        mContext = context;
        init(context);
    }

    private void geCalculatedPath(Path path, float f, float f1, float f2, float f3, float f4, float f5)
    {
        path.offset(f1, f2, path);
        Matrix matrix = new Matrix();
        matrix.postRotate(f, f4, f5);
        matrix.postScale(f3, f3, f4, f5);
        path.transform(matrix);
    }

    private float[] getNewPoint(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7)
    {
        Matrix matrix = new Matrix();
        matrix.postTranslate(-f3, -f4);
        matrix.postScale(1.0F / f5, 1.0F / f5, f6, f7);
        matrix.postRotate(-f2, f6, f7);
        float af[] = {
            f, f1
        };
        matrix.mapPoints(af);
        return af;
    }

    private void handleDrag(MotionEvent motionevent)
    {
        switch(motionevent.getAction()){
        default:
        	break;
        case 0:
        	oldTouchX = motionevent.getX();
            oldTouchY = motionevent.getY();
            isDragging = true;
            return;
        case 1:
        	oldTouchX = 0.0F;
            oldTouchY = 0.0F;
            dx = 0.0F;
            dy = 0.0F;
            isDragging = false;
            return;
        case 2:
        	if(isDragging)
            {
                dx = motionevent.getX() - oldTouchX;
                dy = motionevent.getY() - oldTouchY;
                tx = tx + dx;
                ty = ty + dy;
                oldTouchX = motionevent.getX();
                oldTouchY = motionevent.getY();
                invalidate();
                return;
            }
        	break;
        }
    }

    private void handleScale(float f)
    {
        isDragging = false;
        mScaleFactor = f;
    }

    private void init(Context context)
    {
        mUndoRedoManager = new BitmapUndoRedoManager();
        mContext = context;
        mPath = new Path();
        mPaintLine = new Paint();
        mPaintLine.setAntiAlias(true);
        mPaintLine.setDither(true);
        mPaintLine.setStyle(android.graphics.Paint.Style.STROKE);
        mPaintLine.setStrokeJoin(android.graphics.Paint.Join.ROUND);
        mPaintLine.setStrokeCap(android.graphics.Paint.Cap.ROUND);
        mPaintLine.setStrokeWidth(mLineWidth);
        mPaintLine.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
        mPaintLine.setColor(0);
        if(android.os.Build.VERSION.SDK_INT >= 11)
        {
            setLayerType(1, null);
        }
    }

    private void touchMove(float f, float f1)
    {
        mPath.quadTo(mX, mY, (f + mX) / 2.0F, (f1 + mY) / 2.0F);
        mX = f;
        mY = f1;
    }

    private void touchStart(float f, float f1)
    {
        mPath.reset();
        mPath.moveTo(f, f1);
        mX = f;
        mY = f1;
    }

    private void touchUp()
    {
        mPath.lineTo(mX, mY);
        pathsList.add(new Drawing(mPath, mLineWidth, mColor));
        mPath = new Path();
        mPickedBitmap = getErasedBitmap();
        mUndoRedoManager.saveState(mPickedBitmap);
        pathsList.clear();
        invalidate();
    }

    public boolean checkPath()
    {
        return pathsList.size() != 0;
    }

    public void clearCanvas()
    {
        pathsList.clear();
        invalidate();
        init(mContext);
    }

    public Bitmap getErasedBitmap()
    {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(mPickedBitmap, 0.0F, 0.0F, null);
        Iterator iterator = pathsList.iterator();
        do
        {
            if(!iterator.hasNext())
            {
                mPaintLine.setStrokeWidth((float)mLineWidth * (1.0F / mScaleFactor));
                geCalculatedPath(mPath, 360F - rotate, -tx, -ty, 1.0F / mScaleFactor, canvas.getWidth() / 2, canvas.getHeight() / 2);
                canvas.drawPath(mPath, mPaintLine);
                return bitmap;
            }
            Drawing drawing = (Drawing)iterator.next();
            Path path = drawing.getPath();
            mPaintLine.setStrokeWidth((float)drawing.getStrokeWidth() * (1.0F / mScaleFactor));
            geCalculatedPath(path, 360F - rotate, -tx, -ty, 1.0F / mScaleFactor, canvas.getWidth() / 2, canvas.getHeight() / 2);
            canvas.drawPath(path, mPaintLine);
        } while(true);
    }

    public float getRotationAngle()
    {
        return rotate;
    }

    public float getScale()
    {
        return scale;
    }

    public float getXTranslate()
    {
        return xTranslation;
    }

    public float getYTranslate()
    {
        return yTranslation;
    }

    public boolean isAutoMode()
    {
        return isAutoMode;
    }

    public boolean isZoom()
    {
        return isZoom;
    }

    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
    }

    public void onClickRedo()
    {
        if(mUndoRedoManager.isRedo())
        {
            mPickedBitmap = mUndoRedoManager.getRedo();
            invalidate();
        }
    }

    public void onClickUndo()
    {
        if(mUndoRedoManager.isUndo())
        {
            mPickedBitmap = mUndoRedoManager.getUndo();
            invalidate();
        }
    }

    public void onDestroy()
    {
        mUndoRedoManager.destroyTemporaryFolder(mContext);
    }

    protected void onDetachedFromWindow()
    {
        if(mPickedBitmap != null)
        {
            mPickedBitmap.recycle();
        }
        System.gc();
        super.onDetachedFromWindow();
    }

    protected void onDraw(Canvas canvas)
    {
        if(isZoom || mPickedBitmap == null || mPickedBitmap.isRecycled()){
        	if(!(mPickedBitmap == null || mPickedBitmap.isRecycled())){
    	        Iterator iterator;
    	        canvas.save();
    	        canvas.translate(tx, ty);
    	        canvas.scale(mScaleFactor, mScaleFactor, canvas.getWidth() / 2, canvas.getHeight() / 2);
    	        canvas.rotate(rotate + rotateTemp, canvas.getWidth() / 2, canvas.getHeight() / 2);
    	        canvas.drawBitmap(mPickedBitmap, 0.0F, 0.0F, null);
    	        iterator = pathsList.iterator();
    			while(iterator.hasNext())
    		    {
    		    	Drawing drawing = (Drawing)iterator.next();
    		        Path path = drawing.getPath();
    		        mPaintLine.setStrokeWidth(drawing.getStrokeWidth());
    		        canvas.drawPath(path, mPaintLine);
    		    }
    		    mPaintLine.setStrokeWidth(mLineWidth);
    		    canvas.drawPath(mPath, mPaintLine);
    		    canvas.restore();
            }
        }else{
	        Iterator iterator1;
	        canvas.save();
	        canvas.translate(tx, ty);
	        canvas.scale(mScaleFactor, mScaleFactor, canvas.getWidth() / 2, canvas.getHeight() / 2);
	        canvas.rotate(rotate + rotateTemp, canvas.getWidth() / 2, canvas.getHeight() / 2);
	        canvas.drawBitmap(mPickedBitmap, 0.0F, 0.0F, null);
	        canvas.restore();
	        canvas.save();
	        canvas.translate(0.0F, 0.0F);
	        canvas.scale(1.0F, 1.0F, canvas.getWidth() / 2, canvas.getHeight() / 2);
	        canvas.rotate(0.0F, canvas.getWidth() / 2, canvas.getHeight() / 2);
	        iterator1 = pathsList.iterator();
	        while(iterator1.hasNext()){
	        	Drawing drawing1 = (Drawing)iterator1.next();
	            Path path1 = drawing1.getPath();
	            mPaintLine.setStrokeWidth(drawing1.getStrokeWidth());
	            canvas.drawPath(path1, mPaintLine);
	        }
	        mPaintLine.setStrokeWidth(mLineWidth);
	        canvas.drawPath(mPath, mPaintLine);
	        canvas.restore();
        }
        super.onDraw(canvas);
    }

    protected void onSizeChanged(int i, int j, int k, int l)
    {
        if(i > 0 && j > 0 && mLoadListener != null)
        {
            mLoadListener.onViewInflated();
            mLoadListener = null;
        }
        super.onSizeChanged(i, j, k, l);
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        count = motionevent.getPointerCount();
        if(!isZoom())
        {
            float f = motionevent.getX();
            float f1 = motionevent.getY();
            switch(motionevent.getAction())
            {
            case 0: // '\0'
                touchStart(f, f1);
                invalidate();
                return true;

            case 2: // '\002'
                touchMove(f, f1);
                invalidate();
                return true;

            case 1: // '\001'
                touchUp();
                return true;
            }
        } else
        {
            switch(motionevent.getAction())
            {
            default:
                return true;

            case 0: // '\0'
                rotateTemp = 0.0F;
                if(count <= 1)
                {
                    handleDrag(motionevent);
                    return true;
                }
                break;

            case 2: // '\002'
                count = motionevent.getPointerCount();
                if(count <= 1)
                {
                    handleDrag(motionevent);
                    return true;
                }
                break;

            case 1: // '\001'
                oldTouchX = 0.0F;
                oldTouchY = 0.0F;
                rotate = rotate + rotateTemp;
                rotateTemp = 0.0F;
                return true;
            }
        }
        while(true) 
        {
            return true;
        }
    }

    public void progressValue(int i)
    {
        mLineWidth = i;
    }

    public void setAutoMode(boolean flag)
    {
        isAutoMode = flag;
    }

    public void setLoadListener(OnViewLoadListener onviewloadlistener)
    {
        mLoadListener = onviewloadlistener;
    }

    public void setPickedBitmap(Bitmap bitmap)
    {
        mPickedBitmap = bitmap;
        mPickedBitmap = BitmapUtil.fitToViewByRect(mPickedBitmap, getWidth(), getHeight());
        mUndoRedoManager.saveState(mPickedBitmap);
        invalidate();
    }

    public void setZoom(boolean flag)
    {
        isZoom = flag;
    }

    public void updateStrokeWidth(int i)
    {
        mLineWidth = i;
    }
}
