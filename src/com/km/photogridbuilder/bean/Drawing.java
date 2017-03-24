package com.km.photogridbuilder.bean;

import android.graphics.Paint;
import android.graphics.Path;

public class Drawing
{

    private int color;
    private Paint paint;
    private Path path;
    private int strokeWidth;

    public Drawing(Path path1, int i, int j, Paint paint1)
    {
        path = path1;
        strokeWidth = i;
        paint = paint1;
        color = j;
    }

    public int getColor()
    {
        return color;
    }

    public Paint getPaint()
    {
        return paint;
    }

    public Path getPath()
    {
        return path;
    }

    public int getStrokeWidth()
    {
        return strokeWidth;
    }

    public void setColor(int i)
    {
        color = i;
    }

    public void setPaint(Paint paint1)
    {
        paint = paint1;
    }

    public void setPath(Path path1)
    {
        path = path1;
    }

    public void setStrokeWidth(int i)
    {
        strokeWidth = i;
    }
}
