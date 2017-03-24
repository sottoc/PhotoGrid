package com.km.photogridbuilder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class HelpActivity extends Activity
{

    public HelpActivity()
    {
    }

    public void onClose(View view)
    {
        finish();
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(0x7f030005);
    }
}
