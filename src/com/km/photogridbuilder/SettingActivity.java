package com.km.photogridbuilder;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import com.km.photogridbuilder.util.PreferenceUtil;

public class SettingActivity extends Activity
{

    private static int countSelected;
    CheckBox camera;
    CheckBox facebook;
    CheckBox gallery;
    CheckBox instagram;
    boolean isSourceSelected;
    boolean iscamera;
    boolean isfacebook;
    boolean isgallary;
    boolean isinstagram;

    public SettingActivity()
    {
    }

    private void setChecked()
    {
        isgallary = PreferenceUtil.getIsgallary(this);
        iscamera = PreferenceUtil.getIscamera(this);
        isfacebook = PreferenceUtil.getIsfacebook(this);
        isinstagram = PreferenceUtil.getIsinstagram(this);
        if(isgallary)
        {
            gallery.setChecked(true);
            gallery.setButtonDrawable(getResources().getDrawable(0x7f0200ac));
        } else
        {
            gallery.setChecked(false);
            gallery.setButtonDrawable(getResources().getDrawable(0x7f0200ab));
        }
        if(iscamera)
        {
            camera.setChecked(true);
            camera.setButtonDrawable(getResources().getDrawable(0x7f0200ac));
        } else
        {
            camera.setChecked(false);
            camera.setButtonDrawable(getResources().getDrawable(0x7f0200ab));
        }
        if(isfacebook)
        {
            facebook.setChecked(true);
            facebook.setButtonDrawable(getResources().getDrawable(0x7f0200ac));
        } else
        {
            facebook.setChecked(false);
            facebook.setButtonDrawable(getResources().getDrawable(0x7f0200ab));
        }
        if(isinstagram)
        {
            instagram.setChecked(true);
            instagram.setButtonDrawable(getResources().getDrawable(0x7f0200ac));
            return;
        } else
        {
            instagram.setChecked(false);
            instagram.setButtonDrawable(getResources().getDrawable(0x7f0200ab));
            return;
        }
    }

    public void onBackPressed()
    {
        isgallary = PreferenceUtil.getIsgallary(this);
        iscamera = PreferenceUtil.getIscamera(this);
        isfacebook = PreferenceUtil.getIsfacebook(this);
        isinstagram = PreferenceUtil.getIsinstagram(this);
        if(isgallary || iscamera || isfacebook || isinstagram)
        {
            PreferenceUtil.setcountSelected(this, countSelected);
            finish();
            return;
        } else
        {
            countSelected = 0;
            Toast.makeText(this, "Please select at least one image Source", 0).show();
            return;
        }
    }

    public void onCheckboxClicked(View view)
    {
        boolean flag = ((CheckBox)view).isChecked();
        switch(view.getId())
        {
        default:
            return;

        case 2131296342: 
            if(flag)
            {
                PreferenceUtil.setIsgallary(this, true);
                countSelected = 1 + countSelected;
                gallery.setButtonDrawable(getResources().getDrawable(0x7f0200ac));
                return;
            }
            if(countSelected > 1)
            {
                PreferenceUtil.setIsgallary(this, false);
                countSelected = -1 + countSelected;
                gallery.setButtonDrawable(getResources().getDrawable(0x7f0200ab));
                return;
            } else
            {
                countSelected = 1;
                gallery.setChecked(true);
                gallery.setButtonDrawable(getResources().getDrawable(0x7f0200ac));
                Toast.makeText(getApplicationContext(), "At least one image source should be selected!!", 0).show();
                return;
            }

        case 2131296345: 
            if(flag)
            {
                PreferenceUtil.setIscamera(this, true);
                countSelected = 1 + countSelected;
                camera.setButtonDrawable(getResources().getDrawable(0x7f0200ac));
                return;
            }
            if(countSelected > 1)
            {
                PreferenceUtil.setIscamera(this, false);
                countSelected = -1 + countSelected;
                camera.setButtonDrawable(getResources().getDrawable(0x7f0200ab));
                return;
            } else
            {
                countSelected = 1;
                camera.setChecked(true);
                camera.setButtonDrawable(getResources().getDrawable(0x7f0200ac));
                Toast.makeText(getApplicationContext(), "At least one image source should be selected!!", 0).show();
                return;
            }

        case 2131296348: 
            if(flag)
            {
                PreferenceUtil.setIsfacebook(this, true);
                countSelected = 1 + countSelected;
                facebook.setButtonDrawable(getResources().getDrawable(0x7f0200ac));
                return;
            }
            if(countSelected > 1)
            {
                PreferenceUtil.setIsfacebook(this, false);
                countSelected = -1 + countSelected;
                facebook.setButtonDrawable(getResources().getDrawable(0x7f0200ab));
                return;
            } else
            {
                countSelected = 1;
                facebook.setChecked(true);
                facebook.setButtonDrawable(getResources().getDrawable(0x7f0200ac));
                Toast.makeText(getApplicationContext(), "At least one image source should be selected!!", 0).show();
                return;
            }

        case 2131296351: 
            break;
        }
        if(flag)
        {
            PreferenceUtil.setIsinstagram(this, true);
            countSelected = 1 + countSelected;
            instagram.setButtonDrawable(getResources().getDrawable(0x7f0200ac));
            return;
        }
        if(countSelected > 1)
        {
            PreferenceUtil.setIsinstagram(this, false);
            countSelected = -1 + countSelected;
            instagram.setButtonDrawable(getResources().getDrawable(0x7f0200ab));
            return;
        } else
        {
            countSelected = 1;
            instagram.setChecked(true);
            instagram.setButtonDrawable(getResources().getDrawable(0x7f0200ac));
            Toast.makeText(getApplicationContext(), "At least one image source should be selected!!", 0).show();
            return;
        }
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(0x7f030007);
        countSelected = PreferenceUtil.getcountSelected(this);
        gallery = (CheckBox)findViewById(0x7f090056);
        camera = (CheckBox)findViewById(0x7f090059);
        facebook = (CheckBox)findViewById(0x7f09005c);
        instagram = (CheckBox)findViewById(0x7f09005f);
        setChecked();
    }

    protected void onDestroy()
    {
        super.onDestroy();
        if(isgallary || iscamera || isfacebook || isinstagram)
        {
            PreferenceUtil.setcountSelected(this, countSelected);
            finish();
            return;
        } else
        {
            countSelected = 0;
            Toast.makeText(this, "Please select at least one image Source", 0).show();
            return;
        }
    }

    protected void onResume()
    {
        super.onResume();
        setChecked();
    }
}
