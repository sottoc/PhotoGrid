package com.km.photogridbuilder.shapecut;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;

// Referenced classes of package com.km.photogridbuilder.shapecut:
//            Shape, RotationGestureDetector, ScaleGestureDetector, ScalingUtilities

public class CropperView extends View
    implements RotationGestureDetector.OnRotationGestureListener, ScaleGestureDetector.OnScaleGestureListener
{
    private class ScaleListener extends android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
    {
        public boolean onScale(android.view.ScaleGestureDetector scalegesturedetector)
        {
            CropperView cropperview = CropperView.this;
            cropperview.mScaleFactor = cropperview.mScaleFactor * scalegesturedetector.getScaleFactor();
            mScaleFactor = Math.max(CropperView.MIN_ZOOM, Math.min(mScaleFactor, CropperView.MAX_ZOOM));
            handleScale(mScaleFactor);
            Log.d("Cropper view", (new StringBuilder("scale ")).append(mScaleFactor).toString());
            invalidate();
            return true;
        }

        private ScaleListener()
        {
            super();
        }

        ScaleListener(ScaleListener scalelistener)
        {
            this();
        }
    }


    private static float MAX_ZOOM = 0F;
    private static float MIN_ZOOM = 0F;
    private static final String TAG = "Cropper view";
    private Bitmap bitmapPhoto;
    private Bitmap bitmapShape;
    private Bitmap bitmapTopLayer;
    private int borderColor;
    private int borderSize;
    private Canvas canvasTopLayer;
    int centerX;
    int centerY;
    int count;
    private android.view.ScaleGestureDetector detector;
    int height;
    private int initialTouchX;
    private int initialTouchY;
    private boolean isShapeTouched;
    private Context mContext;
    private float mScaleFactor;
    private Paint paintClearMode;
    private Paint paintRectangle;
    private Rect rectBorder;
    private Rect rectOld;
    private Rect rectOverlay;
    private Rect rectPhoto;
    private Rect rectShapeDest;
    private Rect rectShapeOriginal;
    float rotate;
    float rotateTemp;
    private RotationGestureDetector rotationDetector;
    private com.km.photogridbuilder.shapecut.ScaleGestureDetector scaleGestureDetector;
    int width;

    public CropperView(Context context)
    {
        super(context);
        mScaleFactor = 1.0F;
        borderColor = Shape.borderColor;
        rotate = 0.0F;
        count = 0;
        isShapeTouched = false;
        initialTouchX = 0;
        initialTouchY = 0;
        rectOld = new Rect();
        borderSize = Shape.borderWidth;
        mContext = context;
        detector = new android.view.ScaleGestureDetector(getContext(), new ScaleListener(null));
        rotationDetector = new RotationGestureDetector(this);
        scaleGestureDetector = new com.km.photogridbuilder.shapecut.ScaleGestureDetector(this);
        disableQuickScale();
    }

    public CropperView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        mScaleFactor = 1.0F;
        borderColor = Shape.borderColor;
        rotate = 0.0F;
        count = 0;
        isShapeTouched = false;
        initialTouchX = 0;
        initialTouchY = 0;
        rectOld = new Rect();
        borderSize = Shape.borderWidth;
        mContext = context;
        detector = new android.view.ScaleGestureDetector(getContext(), new ScaleListener(null));
        rotationDetector = new RotationGestureDetector(this);
        scaleGestureDetector = new com.km.photogridbuilder.shapecut.ScaleGestureDetector(this);
        disableQuickScale();
    }

    public CropperView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        mScaleFactor = 1.0F;
        borderColor = Shape.borderColor;
        rotate = 0.0F;
        count = 0;
        isShapeTouched = false;
        initialTouchX = 0;
        initialTouchY = 0;
        rectOld = new Rect();
        borderSize = Shape.borderWidth;
        mContext = context;
        detector = new android.view.ScaleGestureDetector(getContext(), new ScaleListener(null));
        rotationDetector = new RotationGestureDetector(this);
        scaleGestureDetector = new com.km.photogridbuilder.shapecut.ScaleGestureDetector(this);
        disableQuickScale();
    }

    private Rect calculateNewRect(Rect rect, float f)
    {
        Rect rect1 = new Rect();
        RectF rectf = new RectF();
        rectf.left = rect.left;
        rectf.top = rect.top;
        rectf.right = rect.right;
        rectf.bottom = rect.bottom;
        RectF rectf1 = new RectF();
        Matrix matrix = new Matrix();
        matrix.mapRect(rectf);
        matrix.postRotate(f, rectf.centerX(), rectf.centerY());
        matrix.mapRect(rectf1, rectf);
        rect1.left = (int)rectf1.left;
        rect1.right = (int)rectf1.right;
        rect1.top = (int)rectf1.top;
        rect1.bottom = (int)rectf1.bottom;
        return rect1;
    }

    @SuppressLint("NewApi")
	private void disableQuickScale()
    {
        if(android.os.Build.VERSION.SDK_INT >= 19)
        {
            detector.setQuickScaleEnabled(false);
        }
    }

    private Bitmap getBitmap(int i)
    {
        return BitmapFactory.decodeResource(getResources(), i);
    }

    private void handleDrag(MotionEvent motionevent)
    {
        width = rectShapeDest.width();
        height = rectShapeDest.height();
        rectOld.left = rectShapeDest.left;
        rectOld.top = rectShapeDest.top;
        rectOld.right = rectShapeDest.right;
        rectOld.bottom = rectShapeDest.bottom;
        rectShapeDest.left = (int)(motionevent.getX() - (float)initialTouchX);
        rectShapeDest.top = (int)(motionevent.getY() - (float)initialTouchY);
        rectShapeDest.right = rectShapeDest.left + width;
        rectShapeDest.bottom = rectShapeDest.top + height;
        rectBorder.left = rectShapeDest.left - borderSize;
        rectBorder.top = rectShapeDest.top - borderSize;
        rectBorder.bottom = rectShapeDest.bottom + borderSize;
        rectBorder.right = rectShapeDest.right + borderSize;
        if(!rectPhoto.contains(rectBorder))
        {
            rectShapeDest.left = rectOld.left;
            rectShapeDest.top = rectOld.top;
            rectShapeDest.right = rectOld.right;
            rectShapeDest.bottom = rectOld.bottom;
            rectBorder.left = rectShapeDest.left - borderSize;
            rectBorder.top = rectShapeDest.top - borderSize;
            rectBorder.bottom = rectShapeDest.bottom + borderSize;
            rectBorder.right = rectShapeDest.right + borderSize;
            Log.e("boundry", "out");
        }
        invalidate();
    }

    private void handleScale(float f)
    {
        width = (int)(f * (float)rectShapeOriginal.width());
        height = (int)(f * (float)rectShapeOriginal.height());
        centerX = rectShapeDest.centerX();
        centerY = rectShapeDest.centerY();
        rectOld.left = rectShapeDest.left;
        rectOld.top = rectShapeDest.top;
        rectOld.right = rectShapeDest.right;
        rectOld.bottom = rectShapeDest.bottom;
        rectShapeDest.left = centerX - width / 2;
        rectShapeDest.top = centerY - height / 2;
        rectShapeDest.right = rectShapeDest.left + width;
        rectShapeDest.bottom = rectShapeDest.top + height;
        rectBorder.left = rectShapeDest.left - borderSize;
        rectBorder.top = rectShapeDest.top - borderSize;
        rectBorder.bottom = rectShapeDest.bottom + borderSize;
        rectBorder.right = rectShapeDest.right + borderSize;
        if(!rectPhoto.contains(rectBorder))
        {
            rectShapeDest.left = rectOld.left;
            rectShapeDest.top = rectOld.top;
            rectShapeDest.right = rectOld.right;
            rectShapeDest.bottom = rectOld.bottom;
            rectBorder.left = rectShapeDest.left - borderSize;
            rectBorder.top = rectShapeDest.top - borderSize;
            rectBorder.bottom = rectShapeDest.bottom + borderSize;
            rectBorder.right = rectShapeDest.right + borderSize;
            scaleGestureDetector.seScale(mScaleFactor);
            Log.e("boundry", "out");
            return;
        } else
        {
            mScaleFactor = f;
            return;
        }
    }

    private void initializeRectangle(Bitmap bitmap, Rect rect, int i, int j)
    {
        rect.left = (i - bitmap.getWidth()) / 2;
        rect.right = rect.left + bitmap.getWidth();
        rect.top = (j - bitmap.getHeight()) / 2;
        rect.bottom = rect.top + bitmap.getHeight();
    }

    private boolean isShapeTouched(float f, float f1)
    {
        return rectShapeDest.contains((int)f, (int)f1);
    }

    public void OnRotation(RotationGestureDetector rotationgesturedetector)
    {
        rotateTemp = rotationgesturedetector.getAngle();
        Log.d("Cropper view", (new StringBuilder("degree ")).append(rotate).toString());
        invalidate();
    }

    public void OnScale(com.km.photogridbuilder.shapecut.ScaleGestureDetector scalegesturedetector)
    {
        handleScale(scalegesturedetector.getScale());
        invalidate();
    }

    public void changeShape(int i)
    {
        bitmapShape = getBitmap(i);
        bitmapShape = ScalingUtilities.createScaledBitmap(bitmapShape, bitmapPhoto.getWidth(), bitmapPhoto.getHeight(), ScalingUtilities.ScalingLogic.FIT);
        rectShapeOriginal.left = 0;
        rectShapeOriginal.top = 0;
        rectShapeOriginal.right = bitmapShape.getWidth();
        rectShapeOriginal.bottom = bitmapShape.getHeight();
        float f = (float)rectShapeOriginal.width() / (float)rectShapeOriginal.height();
        int j = rectBorder.centerX();
        int k = rectBorder.centerY();
        int l = (int)(f * (float)rectBorder.height());
        int i1 = rectBorder.height();
        rectBorder.left = j - l / 2;
        rectBorder.right = l + rectBorder.left;
        rectBorder.top = k - i1 / 2;
        rectBorder.bottom = i1 + rectBorder.top;
        rectShapeDest.left = rectBorder.left + borderSize;
        rectShapeDest.top = rectBorder.top + borderSize;
        rectShapeDest.bottom = rectBorder.bottom - borderSize;
        rectShapeDest.right = rectBorder.right - borderSize;
        invalidate();
    }

    public Bitmap getShapeBitmap()
    {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        Bitmap bitmap1 = Bitmap.createBitmap(getWidth(), getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Canvas canvas1 = new Canvas(bitmap1);
        canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
        canvas1.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
        canvas1.drawColor(0xff000000);
        canvas1.save();
        canvas1.rotate(rotate + rotateTemp, rectBorder.centerX(), rectBorder.centerY());
        canvas1.drawBitmap(bitmapShape, rectShapeOriginal, rectBorder, paintClearMode);
        canvas1.restore();
        Paint paint;
        Rect rect;
        int i;
        int j;
        int k;
        int l;
        int i1;
        int j1;
        if(borderSize != 0)
        {
            canvas1.drawColor(borderColor, android.graphics.PorterDuff.Mode.SRC_OUT);
        } else
        {
            canvas1.drawColor(0, android.graphics.PorterDuff.Mode.SRC_OUT);
        }
        canvas1.save();
        canvas1.rotate(rotate + rotateTemp, rectShapeDest.centerX(), rectShapeDest.centerY());
        canvas1.drawBitmap(bitmapShape, rectShapeOriginal, rectShapeDest, paintClearMode);
        canvas1.restore();
        canvas.save();
        canvas.rotate(rotate + rotateTemp, rectShapeDest.centerX(), rectShapeDest.centerY());
        canvas.drawBitmap(bitmapShape, rectShapeOriginal, rectShapeDest, null);
        canvas.restore();
        paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapPhoto, rectPhoto.left, rectPhoto.top, paint);
        Log.d("save photo size left top", (new StringBuilder(String.valueOf(bitmapPhoto.getWidth()))).append(" ").append(bitmapPhoto.getHeight()).append(" ").append(rectPhoto.left).append(" ").append(rectPhoto.top).toString());
        canvas.drawBitmap(bitmap1, rectOverlay.left, rectOverlay.top, null);
        rect = calculateNewRect(rectBorder, rotate + rotateTemp);
        i = rect.left;
        j = rect.top;
        rect.width();
        rect.height();
        k = rect.right;
        l = rect.bottom;
        if(i < rectPhoto.left)
        {
            i = rectPhoto.left;
        }
        if(j < rectPhoto.top)
        {
            j = rectPhoto.top;
        }
        if(k > rectPhoto.right)
        {
            k = rectPhoto.right;
        }
        if(l > rectPhoto.bottom)
        {
            l = rectPhoto.bottom;
        }
        i1 = k - i;
        j1 = l - j;
        if(i + i1 > bitmap.getWidth())
        {
            i1 = bitmap.getWidth() - i;
        }
        if(j + j1 > bitmap.getHeight())
        {
            j1 = bitmap.getHeight() - j;
        }
        Log.d("Cropper view", (new StringBuilder("save border width height shape width height")).append(rectBorder.width()).append(" ").append(rectBorder.height()).append(" ").append(rectShapeDest.width()).append(" ").append(rectShapeDest.height()).toString());
        return Bitmap.createBitmap(bitmap, i, j, i1, j1);
    }

    public void onDestroy()
    {
        Log.e("test", "onDestroy");
        if(bitmapPhoto != null)
        {
            if(bitmapShape == null);
        }
    }

    protected void onDraw(Canvas canvas)
    {
        if(canvasTopLayer != null && bitmapPhoto != null && bitmapShape != null && bitmapTopLayer != null)
        {
            canvas.drawBitmap(bitmapPhoto, rectPhoto.left, rectPhoto.top, null);
            canvasTopLayer.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
            canvasTopLayer.drawColor(0xff000000);
            canvasTopLayer.drawRect(rectPhoto, paintRectangle);
            canvasTopLayer.save();
            canvasTopLayer.rotate(rotate + rotateTemp, rectBorder.centerX(), rectBorder.centerY());
            canvasTopLayer.drawBitmap(bitmapShape, rectShapeOriginal, rectBorder, paintClearMode);
            canvasTopLayer.restore();
            if(borderSize != 0)
            {
                canvasTopLayer.drawColor(borderColor, android.graphics.PorterDuff.Mode.SRC_OUT);
            } else
            {
                canvasTopLayer.drawColor(0, android.graphics.PorterDuff.Mode.SRC_OUT);
            }
            canvasTopLayer.drawRect(rectPhoto, paintRectangle);
            canvasTopLayer.save();
            canvasTopLayer.rotate(rotate + rotateTemp, rectShapeDest.centerX(), rectShapeDest.centerY());
            canvasTopLayer.drawBitmap(bitmapShape, rectShapeOriginal, rectShapeDest, paintClearMode);
            canvasTopLayer.restore();
            canvas.drawBitmap(bitmapTopLayer, rectOverlay.left, rectOverlay.top, null);
            Log.d("Cropper view", (new StringBuilder("border w h shape w h ")).append(rectBorder.width()).append(" ").append(rectBorder.height()).append(" ").append(rectShapeDest.width()).append(" ").append(rectShapeDest.height()).toString());
        }
        super.onDraw(canvas);
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        rotationDetector.onTouchEvent(motionevent);
        scaleGestureDetector.onTouchEvent(motionevent);
        switch(motionevent.getAction()){
        default:
        	break;
        case 0:
        	rotateTemp = 0.0F;
            if(isShapeTouched(motionevent.getX(), motionevent.getY()))
            {
                isShapeTouched = true;
                initialTouchX = (int)(motionevent.getX() - (float)rectShapeDest.left);
                initialTouchY = (int)(motionevent.getY() - (float)rectShapeDest.top);
                return true;
            }
            break;
        case 1:
        	initialTouchX = 0;
            initialTouchY = 0;
            isShapeTouched = false;
            rotate = rotate + rotateTemp;
            rotateTemp = 0.0F;
            return true;
        case 2:
        	count = motionevent.getPointerCount();
            if(count > 1)
            {
                Log.d("Cropper view", "pointer 2");
                return true;
            }
            if(isShapeTouched)
            {
                handleDrag(motionevent);
                return true;
            }
            break;
        }
        return true;
    }

    public void populateView(Bitmap bitmap, int i, int j, int k)
    {
        bitmapPhoto = bitmap;
        bitmapShape = getBitmap(i);
        rectShapeDest = new Rect();
        rectBorder = new Rect();
        rectOverlay = new Rect();
        rectPhoto = new Rect();
        rectShapeOriginal = new Rect();
        if(bitmapPhoto != null && bitmapShape != null)
        {
            bitmapPhoto = ScalingUtilities.createScaledBitmap(bitmapPhoto, j, k, ScalingUtilities.ScalingLogic.FIT);
            initializeRectangle(bitmapPhoto, rectPhoto, j, k);
            bitmapTopLayer = Bitmap.createBitmap(j, k, android.graphics.Bitmap.Config.ARGB_8888);
            initializeRectangle(bitmapTopLayer, rectOverlay, j, k);
            bitmapShape = ScalingUtilities.createScaledBitmap(bitmapShape, bitmapPhoto.getWidth(), bitmapPhoto.getHeight(), ScalingUtilities.ScalingLogic.FIT);
            rectShapeOriginal.left = 0;
            rectShapeOriginal.top = 0;
            rectShapeOriginal.right = bitmapShape.getWidth();
            rectShapeOriginal.bottom = bitmapShape.getHeight();
            initializeRectangle(bitmapShape, rectShapeDest, bitmapTopLayer.getWidth(), bitmapTopLayer.getHeight());
            int l = (int)(0.15F * (float)rectShapeDest.width());
            rectBorder.left = l + rectShapeDest.left;
            rectBorder.top = l + rectShapeDest.top;
            rectBorder.bottom = rectShapeDest.bottom - l;
            rectBorder.right = rectShapeDest.right - l;
            rectShapeDest.left = rectBorder.left + borderSize;
            rectShapeDest.top = rectBorder.top + borderSize;
            rectShapeDest.bottom = rectBorder.bottom - borderSize;
            rectShapeDest.right = rectBorder.right - borderSize;
            canvasTopLayer = new Canvas(bitmapTopLayer);
            paintClearMode = new Paint();
            paintClearMode.setAntiAlias(true);
            paintClearMode.setDither(true);
            paintClearMode.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_OUT));
            paintRectangle = new Paint();
            paintRectangle = new Paint();
            paintRectangle.setAntiAlias(true);
            paintRectangle.setColor(Color.parseColor("#70000000"));
            invalidate();
        }
    }

    public void updateBorderColor(int i)
    {
        borderColor = i;
        invalidate();
    }

    public void updateBorderSize(int i)
    {
        borderSize = i;
        rectShapeDest.left = rectBorder.left + borderSize;
        rectShapeDest.top = rectBorder.top + borderSize;
        rectShapeDest.bottom = rectBorder.bottom - borderSize;
        rectShapeDest.right = rectBorder.right - borderSize;
        Log.d("border", (new StringBuilder(String.valueOf(borderSize))).toString());
        invalidate();
    }

    static 
    {
        MIN_ZOOM = 0.1F;
        MAX_ZOOM = 1.0F;
    }
}
