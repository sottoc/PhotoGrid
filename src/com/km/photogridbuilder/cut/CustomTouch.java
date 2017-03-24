package com.km.photogridbuilder.cut;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

// Referenced classes of package com.km.photogridbuilder.cut:
//            Image

public class CustomTouch
{
    public static interface CommonListener
    {

        public abstract Image getDraggableObjectAtPoint(PointInfo pointinfo);

        public abstract void getPositionAndScale(Image image, Image.PositionAndScale positionandscale);

        public abstract void onDoubleTap(Image image, PointInfo pointinfo);

        public abstract void selectObject(Image image, PointInfo pointinfo);

        public abstract boolean setPositionAndScale(Image image, Image.PositionAndScale positionandscale, PointInfo pointinfo);
    }

    public static class PointInfo
    {

        private int action;
        private float angle;
        private boolean angleIsCalculated;
        private float diameter;
        private boolean diameterIsCalculated;
        private float diameterSq;
        private boolean diameterSqIsCalculated;
        private float dx;
        private float dy;
        private long eventTime;
        private boolean isDown;
        private boolean isMultiTouch;
        private int numPoints;
        private int pointerIds[];
        private float pressureMid;
        private float pressures[];
        private float xMid;
        private float xs[];
        private float yMid;
        private float ys[];

        private int julery_isqrt(int i)
        {
            int j = 0;
            int k = 32768;
            int l = 15;
            do
            {
                int i1 = k + (j << 1);
                int j1 = l - 1;
                int k1 = i1 << l;
                if(i >= k1)
                {
                    j += k;
                    i -= k1;
                }
                k >>= 1;
                if(k <= 0)
                {
                    return j;
                }
                l = j1;
            } while(true);
        }

        private void set(int i, float af[], float af1[], float af2[], int ai[], int j, boolean flag, 
                long l)
        {
            eventTime = l;
            action = j;
            numPoints = i;
            int k = 0;
            do
            {
                if(k >= i)
                {
                    isDown = flag;
                    boolean flag1;
                    if(i >= 2)
                    {
                        flag1 = true;
                    } else
                    {
                        flag1 = false;
                    }
                    isMultiTouch = flag1;
                    if(isMultiTouch)
                    {
                        xMid = 0.5F * (af[0] + af[1]);
                        yMid = 0.5F * (af1[0] + af1[1]);
                        pressureMid = 0.5F * (af2[0] + af2[1]);
                        dx = Math.abs(af[1] - af[0]);
                        dy = Math.abs(af1[1] - af1[0]);
                    } else
                    {
                        xMid = af[0];
                        yMid = af1[0];
                        pressureMid = af2[0];
                        dy = 0.0F;
                        dx = 0.0F;
                    }
                    angleIsCalculated = false;
                    diameterIsCalculated = false;
                    diameterSqIsCalculated = false;
                    return;
                }
                xs[k] = af[k];
                ys[k] = af1[k];
                pressures[k] = af2[k];
                pointerIds[k] = ai[k];
                k++;
            } while(true);
        }

        public int getAction()
        {
            return action;
        }

        public long getEventTime()
        {
            return eventTime;
        }

        public float getMultiTouchAngle()
        {
            if(!angleIsCalculated)
            {
                if(!isMultiTouch)
                {
                    angle = 0.0F;
                } else
                {
                    angle = (float)Math.atan2(ys[1] - ys[0], xs[1] - xs[0]);
                }
                angleIsCalculated = true;
            }
            if(CustomTouch.notRequired)
            {
                return 0.0F;
            } else
            {
                return angle;
            }
        }

        public float getMultiTouchDiameter()
        {
            if(diameterIsCalculated)
            	return diameter;
            if(isMultiTouch){
            	float f = getMultiTouchDiameterSq();
                float f1 = 0.0F;
                if(f != 0.0F)
                {
                    f1 = (float)julery_isqrt((int)(256F * f)) / 16F;
                }
                diameter = f1;
                if(diameter < dx)
                {
                    diameter = dx;
                }
                if(diameter < dy)
                {
                    diameter = dy;
                }
            }else
            	diameter = 0.0F;
            diameterIsCalculated = true;
            return diameter;
        }

        public float getMultiTouchDiameterSq()
        {
            if(!diameterSqIsCalculated)
            {
                float f;
                if(isMultiTouch)
                {
                    f = dx * dx + dy * dy;
                } else
                {
                    f = 0.0F;
                }
                diameterSq = f;
                diameterSqIsCalculated = true;
            }
            return diameterSq;
        }

        public float getMultiTouchHeight()
        {
            if(isMultiTouch)
            {
                return dy;
            } else
            {
                return 0.0F;
            }
        }

        public float getMultiTouchWidth()
        {
            if(isMultiTouch)
            {
                return dx;
            } else
            {
                return 0.0F;
            }
        }

        public int getNumTouchPoints()
        {
            return numPoints;
        }

        public int[] getPointerIds()
        {
            return pointerIds;
        }

        public float getPressure()
        {
            return pressureMid;
        }

        public float[] getPressures()
        {
            return pressures;
        }

        public float getX()
        {
            return xMid;
        }

        public float[] getXs()
        {
            return xs;
        }

        public float getY()
        {
            return yMid;
        }

        public float[] getYs()
        {
            return ys;
        }

        public boolean isDown()
        {
            return isDown;
        }

        public boolean isMultiTouch()
        {
            return isMultiTouch;
        }

        public void set(PointInfo pointinfo)
        {
            numPoints = pointinfo.numPoints;
            int i = 0;
            do
            {
                if(i >= numPoints)
                {
                    xMid = pointinfo.xMid;
                    yMid = pointinfo.yMid;
                    pressureMid = pointinfo.pressureMid;
                    dx = pointinfo.dx;
                    dy = pointinfo.dy;
                    diameter = pointinfo.diameter;
                    diameterSq = pointinfo.diameterSq;
                    angle = pointinfo.angle;
                    isDown = pointinfo.isDown;
                    action = pointinfo.action;
                    isMultiTouch = pointinfo.isMultiTouch;
                    diameterIsCalculated = pointinfo.diameterIsCalculated;
                    diameterSqIsCalculated = pointinfo.diameterSqIsCalculated;
                    angleIsCalculated = pointinfo.angleIsCalculated;
                    eventTime = pointinfo.eventTime;
                    return;
                }
                xs[i] = pointinfo.xs[i];
                ys[i] = pointinfo.ys[i];
                pressures[i] = pointinfo.pressures[i];
                pointerIds[i] = pointinfo.pointerIds[i];
                i++;
            } while(true);
        }



        public PointInfo()
        {
            xs = new float[20];
            ys = new float[20];
            pressures = new float[20];
            pointerIds = new int[20];
        }
    }


    private static int ACTION_POINTER_INDEX_SHIFT = 0;
    private static int ACTION_POINTER_UP = 0;
    public static final boolean DEBUG = false;
    private static final long EVENT_SETTLE_TIME = 20L;
    private static final float MAX_MULTITOUCH_DIM_JUMP_SIZE = 40F;
    private static final float MAX_MULTITOUCH_POS_JUMP_SIZE = 30F;
    public static final int MAX_TOUCH_POINTS = 20;
    private static final float MIN_MULTITOUCH_SEPARATION = 30F;
    private static final int MODE_DRAG = 1;
    private static final int MODE_NOTHING = 0;
    private static final int MODE_PINCH = 2;
    private static Method m_getHistoricalPressure;
    private static Method m_getHistoricalX;
    private static Method m_getHistoricalY;
    private static Method m_getPointerCount;
    private static Method m_getPointerId;
    private static Method m_getPressure;
    private static Method m_getX;
    private static Method m_getY;
    public static final boolean multiTouchSupported;
    public static boolean notRequired;
    private static final int pointerIds[];
    private static final float pressureVals[];
    private static final float xVals[];
    private static final float yVals[];
    final GestureDetector detector;
    private boolean handleSingleTouchEvents;
    final android.view.GestureDetector.SimpleOnGestureListener listener;
    private PointInfo mCurrPt;
    private float mCurrPtAng;
    private float mCurrPtDiam;
    private float mCurrPtHeight;
    private float mCurrPtWidth;
    private float mCurrPtX;
    private float mCurrPtY;
    private Image.PositionAndScale mCurrXform;
    private int mMode;
    private PointInfo mPrevPt;
    private long mSettleEndTime;
    private long mSettleStartTime;
    CommonListener objectCanvas;
    private Image selectedObject;
    private float startAngleMinusPinchAngle;
    private float startPosX;
    private float startPosY;
    private float startScaleOverPinchDiam;
    private float startScaleXOverPinchWidth;
    private float startScaleYOverPinchHeight;

    public CustomTouch(CommonListener commonlistener)
    {
        this(commonlistener, true);
    }

    public CustomTouch(CommonListener commonlistener, boolean flag)
    {
        selectedObject = null;
        mCurrXform = new Image.PositionAndScale();
        mMode = 0;
        listener = new android.view.GestureDetector.SimpleOnGestureListener() {
            public boolean onDoubleTap(MotionEvent motionevent)
            {
                selectedObject = objectCanvas.getDraggableObjectAtPoint(mCurrPt);
                objectCanvas.onDoubleTap(selectedObject, mCurrPt);
                return true;
            }

            public void onLongPress(MotionEvent motionevent)
            {
            }
        };
        detector = new GestureDetector(listener);
        mCurrPt = new PointInfo();
        mPrevPt = new PointInfo();
        handleSingleTouchEvents = flag;
        objectCanvas = commonlistener;
    }

    private void anchorAtThisPositionAndScale()
    {
        if(selectedObject == null)
        {
            return;
        }
        objectCanvas.getPositionAndScale(selectedObject, mCurrXform);
        float f;
        float f1;
        if(!mCurrXform.updateScale)
        {
            f = 1.0F;
        } else
        if(mCurrXform.scale == 0.0F)
        {
            f = 1.0F;
        } else
        {
            f = mCurrXform.scale;
        }
        f1 = 1.0F / f;
        extractCurrPtInfo();
        startPosX = f1 * (mCurrPtX - mCurrXform.xOff);
        startPosY = f1 * (mCurrPtY - mCurrXform.yOff);
        startScaleOverPinchDiam = mCurrXform.scale / mCurrPtDiam;
        startScaleXOverPinchWidth = mCurrXform.scaleX / mCurrPtWidth;
        startScaleYOverPinchHeight = mCurrXform.scaleY / mCurrPtHeight;
        startAngleMinusPinchAngle = mCurrXform.angle - mCurrPtAng;
    }

    private void extractCurrPtInfo()
    {
        mCurrPtX = mCurrPt.getX();
        mCurrPtY = mCurrPt.getY();
        float f;
        float f1;
        float f2;
        boolean flag;
        float f3;
        if(!mCurrXform.updateScale)
        {
            f = 0.0F;
        } else
        {
            f = mCurrPt.getMultiTouchDiameter();
        }
        mCurrPtDiam = Math.max(21.3F, f);
        if(!mCurrXform.updateScaleXY)
        {
            f1 = 0.0F;
        } else
        {
            f1 = mCurrPt.getMultiTouchWidth();
        }
        mCurrPtWidth = Math.max(30F, f1);
        if(!mCurrXform.updateScaleXY)
        {
            f2 = 0.0F;
        } else
        {
            f2 = mCurrPt.getMultiTouchHeight();
        }
        mCurrPtHeight = Math.max(30F, f2);
        flag = mCurrXform.updateAngle;
        f3 = 0.0F;
        if(flag)
        {
            f3 = mCurrPt.getMultiTouchAngle();
        }
        mCurrPtAng = f3;
    }

    private void multiTouchController()
    {
        switch(mMode){
        default:
        	break;
        case 0:
        	if(mCurrPt.isDown())
            {
                selectedObject = objectCanvas.getDraggableObjectAtPoint(mCurrPt);
                if(selectedObject != null)
                {
                    mMode = 1;
                    objectCanvas.selectObject(selectedObject, mCurrPt);
                    anchorAtThisPositionAndScale();
                    long l = mCurrPt.getEventTime();
                    mSettleEndTime = l;
                    mSettleStartTime = l;
                    return;
                }
            }
        	break;
        case 1:
        	if(!mCurrPt.isDown())
            {
                mMode = 0;
                CommonListener commonlistener1 = objectCanvas;
                selectedObject = null;
                commonlistener1.selectObject(null, mCurrPt);
                return;
            }
            if(mCurrPt.isMultiTouch())
            {
                mMode = 2;
                anchorAtThisPositionAndScale();
                mSettleStartTime = mCurrPt.getEventTime();
                mSettleEndTime = 20L + mSettleStartTime;
                return;
            }
            if(mCurrPt.getEventTime() < mSettleEndTime)
            {
                anchorAtThisPositionAndScale();
                return;
            } else
            {
                performDragOrPinch();
                return;
            }
        case 2:
        	if(!mCurrPt.isMultiTouch() || !mCurrPt.isDown())
            {
                if(!mCurrPt.isDown())
                {
                    mMode = 0;
                    CommonListener commonlistener = objectCanvas;
                    selectedObject = null;
                    commonlistener.selectObject(null, mCurrPt);
                    return;
                } else
                {
                    mMode = 1;
                    anchorAtThisPositionAndScale();
                    mSettleStartTime = mCurrPt.getEventTime();
                    mSettleEndTime = 20L + mSettleStartTime;
                    return;
                }
            }
            if(Math.abs(mCurrPt.getX() - mPrevPt.getX()) > 30F || Math.abs(mCurrPt.getY() - mPrevPt.getY()) > 30F || 0.5F * Math.abs(mCurrPt.getMultiTouchWidth() - mPrevPt.getMultiTouchWidth()) > 40F || 0.5F * Math.abs(mCurrPt.getMultiTouchHeight() - mPrevPt.getMultiTouchHeight()) > 40F)
            {
                anchorAtThisPositionAndScale();
                mSettleStartTime = mCurrPt.getEventTime();
                mSettleEndTime = 20L + mSettleStartTime;
                return;
            }
            if(mCurrPt.eventTime < mSettleEndTime)
            {
                anchorAtThisPositionAndScale();
                return;
            } else
            {
                performDragOrPinch();
                return;
            }
        }
    }

    private void performDragOrPinch()
    {
        float f;
        float f1;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        f = 1.0F;
        if(selectedObject != null)
        {
                if(mCurrXform.updateScale && mCurrXform.scale != 0.0F)
                {
                    f = mCurrXform.scale;
                }
                extractCurrPtInfo();
                f1 = mCurrPtX - f * startPosX;
                f2 = mCurrPtY - f * startPosY;
                f3 = startScaleOverPinchDiam * mCurrPtDiam;
                f4 = startScaleXOverPinchWidth * mCurrPtWidth;
                f5 = startScaleYOverPinchHeight * mCurrPtHeight;
                f6 = startAngleMinusPinchAngle + mCurrPtAng;
                mCurrXform.set(f1, f2, f3, f4, f5, f6);
                if(!objectCanvas.setPositionAndScale(selectedObject, mCurrXform, mCurrPt))
                {
                    return;
                }
        }
    }

    protected boolean getHandleSingleTouchEvents()
    {
        return handleSingleTouchEvents;
    }

    public int getMode()
    {
        return mMode;
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
    	int i1;
        int j1;
        Method method;
        Object aobj[];
        int k1;
        float af[];
        Method method1;
        Object aobj1[];
        Object obj;
        float af1[];
        Method method2;
        Object aobj2[];
        Object obj1;
        float af2[];
        Method method3;
        Object aobj3[];
        Object obj2;
        Method method4;
        Object aobj4[];
        Method method5;
        Object aobj5[];
        Method method6;
        Object aobj6[];
        long l1;
        PointInfo pointinfo;
        long l2;
        float af3[];
        float f;
        float af4[];
        float f1;
        float af5[];
        float f2;
        int j;
        int k;
        int l;
        boolean flag;
        boolean flag1;
        int i;
        try
        {
        	if(!multiTouchSupported)
            	i = 1;
            else
            	i = ((Integer)m_getPointerCount.invoke(motionevent, new Object[0])).intValue();
            if(mMode == 0 && !handleSingleTouchEvents && i == 1)
            {
                return false;
            }
            j = motionevent.getAction();
            k = motionevent.getHistorySize() / i;
            l = 0;
            while(l <= k){
    	        if(l < k)
    	        {
    	            flag = true;
    	        } else
    	        {
    	            flag = false;
    	        }
    	        if(multiTouchSupported && i != 1){
    	        	i1 = Math.min(i, 20);
    	            j1 = 0;
    	    		while(j1 < i1){
    	    	        method = m_getPointerId;
    	    	        aobj = new Object[1];
    	    	        aobj[0] = Integer.valueOf(j1);
    	    	        k1 = ((Integer)method.invoke(motionevent, aobj)).intValue();
    	    	        pointerIds[j1] = k1;
    	    	        af = xVals;
    	    	        if(!flag){
    	    	        	method6 = m_getX;
    	    	            aobj6 = new Object[1];
    	    	            aobj6[0] = Integer.valueOf(j1);
    	    	            obj = method6.invoke(motionevent, aobj6);
    	    	        }else{
    	    		        method1 = m_getHistoricalX;
    	    		        aobj1 = new Object[2];
    	    		        aobj1[0] = Integer.valueOf(j1);
    	    		        aobj1[1] = Integer.valueOf(l);
    	    		        obj = method1.invoke(motionevent, aobj1);
    	    	        }
    	    	        af[j1] = ((Float)obj).floatValue();
    	    	        af1 = yVals;
    	    	        if(!flag){
    	    	        	method5 = m_getY;
    	    	            aobj5 = new Object[1];
    	    	            aobj5[0] = Integer.valueOf(j1);
    	    	            obj1 = method5.invoke(motionevent, aobj5);
    	    	        }else{
    	    		        method2 = m_getHistoricalY;
    	    		        aobj2 = new Object[2];
    	    		        aobj2[0] = Integer.valueOf(j1);
    	    		        aobj2[1] = Integer.valueOf(l);
    	    		        obj1 = method2.invoke(motionevent, aobj2);
    	    	        }
    	    	        af1[j1] = ((Float)obj1).floatValue();
    	    	        af2 = pressureVals;
    	    	        if(!flag)
    	    	        {
    	    	        	method4 = m_getPressure;
    	    	            aobj4 = new Object[1];
    	    	            aobj4[0] = Integer.valueOf(j1);
    	    	            obj2 = method4.invoke(motionevent, aobj4);
    	    	        }else{
    	    		        method3 = m_getHistoricalPressure;
    	    		        aobj3 = new Object[2];
    	    		        aobj3[0] = Integer.valueOf(j1);
    	    		        aobj3[1] = Integer.valueOf(l);
    	    		        obj2 = method3.invoke(motionevent, aobj3);
    	    	        }
    	    	        af2[j1] = ((Float)obj2).floatValue();
    	    	        j1++;
    	    		}
    	        }else{
    		        af3 = xVals;
    		        if(!flag)
    		        	f = motionevent.getX();
    		        else
    		        	f = motionevent.getHistoricalX(l);
    		        af3[0] = f;
    		        af4 = yVals;
    		        if(!flag)
    		        	f1 = motionevent.getY();
    		        else
    		        	f1 = motionevent.getHistoricalY(l);
    		        af4[0] = f1;
    		        af5 = pressureVals;
    		        if(!flag)
    		        	f2 = motionevent.getPressure();
    		        else
    		        	f2 = motionevent.getHistoricalPressure(l);
    		        af5[0] = f2;
    	        }
    	        if(flag)
    	        {
    	            j = 2;
    	        }
    	        if(!flag){
    	        	if(j == 1)
    	            {
    	        		flag1 = false;
    	            }else if((j & -1 + (1 << ACTION_POINTER_INDEX_SHIFT)) == ACTION_POINTER_UP || j == 3)
    	            {
    	            	flag1 = false;
    	            }else
    	            	flag1 = true;
    	        }else
    	        	flag1 = true;
    	        if(!flag){
    	        	l2 = motionevent.getEventTime();
    	        	l1 = l2;
    	        }else
    	        	l1 = motionevent.getHistoricalEventTime(l);
    	        pointinfo = mPrevPt;
    	        mPrevPt = mCurrPt;
    	        mCurrPt = pointinfo;
    	        mCurrPt.set(i, xVals, yVals, pressureVals, pointerIds, j, flag1, l1);
    	        detector.onTouchEvent(motionevent);
    	        multiTouchController();
    	        l++;
            }
            return true;
        }
        catch(Exception exception)
        {
            Log.e("MultiTouchController", "onTouchEvent() failed", exception);
            return false;
        }
    }

    protected void setHandleSingleTouchEvents(boolean flag)
    {
        handleSingleTouchEvents = flag;
    }

    static 
    {
        notRequired = false;
        ACTION_POINTER_UP = 6;
        ACTION_POINTER_INDEX_SHIFT = 8;
        boolean flag = true;
        try{
	        m_getPointerCount = android.view.MotionEvent.class.getMethod("getPointerCount", new Class[0]);
	        Class aclass[] = new Class[1];
	        aclass[0] = Integer.TYPE;
	        m_getPointerId = android.view.MotionEvent.class.getMethod("getPointerId", aclass);
	        Class aclass1[] = new Class[1];
	        aclass1[0] = Integer.TYPE;
	        m_getPressure = android.view.MotionEvent.class.getMethod("getPressure", aclass1);
	        Class aclass2[] = new Class[2];
	        aclass2[0] = Integer.TYPE;
	        aclass2[1] = Integer.TYPE;
	        m_getHistoricalX = android.view.MotionEvent.class.getMethod("getHistoricalX", aclass2);
	        Class aclass3[] = new Class[2];
	        aclass3[0] = Integer.TYPE;
	        aclass3[1] = Integer.TYPE;
	        m_getHistoricalY = android.view.MotionEvent.class.getMethod("getHistoricalY", aclass3);
	        Class aclass4[] = new Class[2];
	        aclass4[0] = Integer.TYPE;
	        aclass4[1] = Integer.TYPE;
	        m_getHistoricalPressure = android.view.MotionEvent.class.getMethod("getHistoricalPressure", aclass4);
	        Class aclass5[] = new Class[1];
	        aclass5[0] = Integer.TYPE;
	        m_getX = android.view.MotionEvent.class.getMethod("getX", aclass5);
	        Class aclass6[] = new Class[1];
	        aclass6[0] = Integer.TYPE;
	        m_getY = android.view.MotionEvent.class.getMethod("getY", aclass6);
        }catch(Exception e){
        	flag = false;
        }
        multiTouchSupported = flag;
        Exception exception;
        if(multiTouchSupported)
        {
            try
            {
                ACTION_POINTER_UP = android.view.MotionEvent.class.getField("ACTION_POINTER_UP").getInt(null);
                ACTION_POINTER_INDEX_SHIFT = android.view.MotionEvent.class.getField("ACTION_POINTER_INDEX_SHIFT").getInt(null);
            }
            catch(Exception exception1) { }
        }
        xVals = new float[20];
        yVals = new float[20];
        pressureVals = new float[20];
        pointerIds = new int[20];
    }
}
