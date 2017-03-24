package com.km.photogridbuilder.shapecut;

import android.util.Log;
import android.view.MotionEvent;

public class ScaleGestureDetector
{
    public static interface OnScaleGestureListener
    {

        public abstract void OnScale(ScaleGestureDetector scalegesturedetector);
    }


    private static final int INVALID_POINTER_ID = -1;
    private float fX;
    private float fY;
    private OnScaleGestureListener mListener;
    private float mScale;
    private float minScale;
    private float newDistance;
    private float newScalefactor;
    private float prevDistance;
    private int ptrID1;
    private int ptrID2;
    private float sX;
    private float sY;

    public ScaleGestureDetector(OnScaleGestureListener onscalegesturelistener)
    {
        mScale = 1.0F;
        prevDistance = 0.0F;
        newDistance = 0.0F;
        minScale = 0.1F;
        mListener = onscalegesturelistener;
        ptrID1 = -1;
        ptrID2 = -1;
    }

    public float getScale()
    {
        return mScale;
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        switch(motionevent.getActionMasked()){
        default:
        	break;
        case 0:
        	ptrID1 = motionevent.getPointerId(motionevent.getActionIndex());
        	break;
        case 1:
        	ptrID1 = -1;
            prevDistance = 0.0F;
            newDistance = 0.0F;
            break;
        case 2:
        	if(ptrID1 == -1 || ptrID2 == -1)
            {
                break;
            }
            float f = motionevent.getX(motionevent.findPointerIndex(ptrID1));
            float f1 = motionevent.getY(motionevent.findPointerIndex(ptrID1));
            float f2 = motionevent.getX(motionevent.findPointerIndex(ptrID2));
            float f3 = motionevent.getY(motionevent.findPointerIndex(ptrID2));
            Log.e("scale detector", "step1");
            newDistance = (float)Math.sqrt(Math.pow(Math.abs(f - f2), 2D) + Math.pow(Math.abs(f1 - f3), 2D));
            float f4;
            if(prevDistance < newDistance)
            {
                newScalefactor = newDistance / prevDistance;
                mScale = mScale + (newScalefactor - 1.0F);
                prevDistance = newDistance;
                Log.e("scale detector", "step2");
            } else
            if(prevDistance > newDistance)
            {
                newScalefactor = prevDistance / newDistance;
                mScale = mScale - (newScalefactor - 1.0F);
                prevDistance = newDistance;
                Log.e("scale detector", "step3");
            }
            Log.e("scale detector", (new StringBuilder("scale ")).append(mScale).toString());
            if(mScale >= minScale)
            {
                f4 = mScale;
            } else
            {
                f4 = minScale;
            }
            mScale = f4;
            if(mListener != null)
            {
                mListener.OnScale(this);
            }
            break;
        case 3:
        	ptrID2 = motionevent.getPointerId(motionevent.getActionIndex());
            sX = motionevent.getX(motionevent.findPointerIndex(ptrID1));
            sY = motionevent.getY(motionevent.findPointerIndex(ptrID1));
            fX = motionevent.getX(motionevent.findPointerIndex(ptrID2));
            fY = motionevent.getY(motionevent.findPointerIndex(ptrID2));
            newDistance = (float)Math.sqrt(Math.pow(Math.abs(sX - fX), 2D) + Math.pow(Math.abs(sY - fY), 2D));
            prevDistance = (float)Math.sqrt(Math.pow(Math.abs(sX - fX), 2D) + Math.pow(Math.abs(sY - fY), 2D));
            break;
        case 4:
        	ptrID2 = -1;
            prevDistance = 0.0F;
            newDistance = 0.0F;
            break;
        }
        return true;
    }

    public void seScale(float f)
    {
        mScale = f;
    }
}
