package com.km.photogridbuilder;

import android.app.Activity;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class BaseActivity extends Activity
{

    protected ImageLoader imageLoader;

    public BaseActivity()
    {
        imageLoader = ImageLoader.getInstance();
    }
}
