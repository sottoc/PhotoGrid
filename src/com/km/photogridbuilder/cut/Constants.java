package com.km.photogridbuilder.cut;


public class Constants
{

    public static final String FILE_EXTENSION;
    public static final int MAXPASTELISTLIMIT = 50;
    public static final int MAXUNDOLIMIT = 10;

    public Constants()
    {
    }

    static 
    {
        FILE_EXTENSION = android.graphics.Bitmap.CompressFormat.PNG.toString();
    }
}
