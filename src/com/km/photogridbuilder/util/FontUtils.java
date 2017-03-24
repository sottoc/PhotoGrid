package com.km.photogridbuilder.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;
import java.lang.ref.SoftReference;
import java.util.HashMap;

public class FontUtils
{

    private static HashMap cache = new HashMap();
    private static SoftReference stf;
    static Typeface tf;

    public FontUtils()
    {
    }

    public static Typeface getTypeface(Context context, String s)
    {
    	try{
	        HashMap hashmap = cache;
	        synchronized(hashmap){
	        	Typeface typeface1;
	            if(cache.containsKey(s))
	            {
	            	Typeface typeface;
	                stf = (SoftReference)cache.get(s);
	                typeface = (Typeface)stf.get();
	                return typeface;
	            }
	            tf = Typeface.createFromAsset(context.getAssets(), s);
	            if(tf == null)
	            {
	            	return tf;
	            }
	            cache.put(s, new SoftReference(tf));
	            typeface1 = tf;
	            return typeface1;
	        }
    	}catch(Exception e){
    		return null;
    	}
    }

    public static void setTypeface(Context context, TextView textview, String s)
    {
    	try{
	        HashMap hashmap = cache;
	        synchronized(hashmap){
	        	if(cache.containsKey(s))
	            {
	        		stf = (SoftReference)cache.get(s);
	                textview.setTypeface((Typeface)stf.get());
	                return;
	            }
	            tf = Typeface.createFromAsset(context.getAssets(), s);
	            if(tf != null)
	            {
	                cache.put(s, new SoftReference(tf));
	                textview.setTypeface(tf);
	            }
	        }
    	}catch(Exception e){}
    }
}
