package com.km.photogridbuilder.Objects;

import android.graphics.Bitmap;

public class ImageItem
{

    private Bitmap image;
    private String imagePath;
    private String title;

    public ImageItem(Bitmap bitmap, String s, String s1)
    {
        image = bitmap;
        title = s;
        imagePath = s1;
    }

    public Bitmap getImage()
    {
        return image;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public String getTitle()
    {
        return title;
    }

    public void setImage(Bitmap bitmap)
    {
        image = bitmap;
    }

    public void setImagePath(String s)
    {
        imagePath = s;
    }

    public void setTitle(String s)
    {
        title = s;
    }
}
