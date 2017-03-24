package com.dexati.dexaticommons.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.Locale;

public class TypeFaceUtil
{

    public TypeFaceUtil()
    {
    }

    public static Typeface getTypeFace(Context context, String s)
    {
        return Typeface.createFromAsset(context.getAssets(), s);
    }

    public static void setViewGroupTypeFace(Context context, ViewGroup viewgroup, String s)
    {
        int i = 0;
        View view;
        while(i < viewgroup.getChildCount())
        {
	        view = viewgroup.getChildAt(i);
	        if(!(view instanceof ViewGroup))
	        {
	        	if(view != null){
	            	Typeface typeface;
	                if(view.getTag() != null && view.getTag().toString().toLowerCase(Locale.ENGLISH).equals("bold"))
	                {
	                    typeface = getTypeFace(context, s);
	                } else
	                {
	                    typeface = getTypeFace(context, s);
	                }
	                if(view instanceof TextView)
	                {
	                    ((TextView)view).setTypeface(typeface);
	                } else
	                if(view instanceof EditText)
	                {
	                    ((EditText)view).setTypeface(typeface);
	                } else
	                if(view instanceof RadioButton)
	                {
	                    ((RadioButton)view).setTypeface(typeface);
	                } else
	                if(view instanceof CheckBox)
	                {
	                    ((CheckBox)view).setTypeface(typeface);
	                }
	            }
	        }else
	        	setViewGroupTypeFace(context, (ViewGroup)view, s);
	        i++;
        }
    }
}
