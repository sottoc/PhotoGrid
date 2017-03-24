package com.km.photogridbuilder.cut;

import android.view.MotionEvent;

public class RotationGestureDetector
{
    public static interface OnRotationGestureListener
    {

        public abstract void OnRotation(RotationGestureDetector rotationgesturedetector);
    }


    private static final int INVALID_POINTER_ID = -1;
    private float fX;
    private float fY;
    private float mAngle;
    private OnRotationGestureListener mListener;
    private int pointerCount;
    private int ptrID1;
    private int ptrID2;
    private float sX;
    private float sY;

    public RotationGestureDetector(OnRotationGestureListener onrotationgesturelistener)
    {
        pointerCount = 0;
        mListener = onrotationgesturelistener;
        ptrID1 = -1;
        ptrID2 = -1;
    }

    private float angleBetweenLines(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            float f7)
    {
        float f8 = (float)Math.toDegrees((float)Math.atan2(f1 - f3, f - f2) - (float)Math.atan2(f5 - f7, f4 - f6)) % 360F;
        if(f8 < -180F)
        {
            f8 += 360F;
        }
        if(f8 > 180F)
        {
            f8 -= 360F;
        }
        return f8;
    }

    public float getAngle()
    {
        return mAngle;
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        pointerCount = motionevent.getPointerCount();
        switch(motionevent.getActionMasked()){
        default:case 3:case 4:
        	break;
        case 0:
        	ptrID1 = motionevent.getPointerId(motionevent.getActionIndex());
            return true;
        case 1:
        	ptrID1 = -1;
            return true;
        case 2:
        	if(ptrID1 != -1 && ptrID2 != -1 && pointerCount > 1)
            {
                float f = motionevent.getX(motionevent.findPointerIndex(ptrID1));
                float f1 = motionevent.getY(motionevent.findPointerIndex(ptrID1));
                mAngle = angleBetweenLines(motionevent.getX(motionevent.findPointerIndex(ptrID2)), motionevent.getY(motionevent.findPointerIndex(ptrID2)), f, f1, fX, fY, sX, sY);
                if(mListener != null)
                {
                    mListener.OnRotation(this);
                    return true;
                }
            }
        	break;
        case 5:
        	ptrID2 = motionevent.getPointerId(motionevent.getActionIndex());
            sX = motionevent.getX(motionevent.findPointerIndex(ptrID1));
            sY = motionevent.getY(motionevent.findPointerIndex(ptrID1));
            fX = motionevent.getX(motionevent.findPointerIndex(ptrID2));
            fY = motionevent.getY(motionevent.findPointerIndex(ptrID2));
            return true;
        case 6:
        	ptrID2 = -1;
            return true;
        }
        return true;
    }

    public void setRotationAngle(float f)
    {
        mAngle = f;
    }
}
