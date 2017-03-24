package com.km.photogridbuilder.shapecut;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

// Referenced classes of package com.km.photogridbuilder.shapecut:
//            Shape

public class CircularColorView extends View
{

    int color;
    private Paint paint;

    public CircularColorView(Context context)
    {
        super(context);
        color = Shape.borderColor;
        init();
    }

    public CircularColorView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        color = Shape.borderColor;
        init();
    }

    public CircularColorView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        color = Shape.borderColor;
        init();
    }

    private void init()
    {
        paint = new Paint();
        paint.setStyle(android.graphics.Paint.Style.FILL);
        paint.setColor(color);
    }

    protected void onDraw(Canvas canvas)
    {
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, -5 + getWidth() / 2, paint);
        super.onDraw(canvas);
    }

    public void setColor(int i)
    {
        color = i;
        paint.setColor(i);
        invalidate();
    }
}
