package com.km.photogridbuilder.cut;

import android.graphics.Path;

public class Drawing
{

    private int color;
    private Path path;
    private int strokeWidth;

    public Drawing(Path path1, int i, int j)
    {
        path = path1;
        strokeWidth = i;
        setColor(j);
    }

    public int getColor()
    {
        return color;
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

    public void setPath(Path path1)
    {
        path = path1;
    }

    public void setStrokeWidth(int i)
    {
        strokeWidth = i;
    }
}
