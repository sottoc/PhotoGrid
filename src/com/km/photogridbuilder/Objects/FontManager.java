package com.km.photogridbuilder.Objects;

import android.content.Context;
import android.graphics.Typeface;
import java.io.File;
import java.util.HashMap;

// Referenced classes of package com.km.photogridbuilder.Objects:
//            TTFAnalyzer

public class FontManager
{

    public FontManager()
    {
    }

    public static HashMap enumerateFonts()
    {
        String as[];
        HashMap hashmap;
        TTFAnalyzer ttfanalyzer;
        int i;
        int j;
        File file;
        as = (new String[] {
            "/system/fonts", "/system/font", "/data/fonts"
        });
        hashmap = new HashMap();
        ttfanalyzer = new TTFAnalyzer();
        i = as.length;
        j = 0;
        while(j < i){
        	file = new File(as[j]);
            if(file.exists())
            {
            	File afile[] = file.listFiles();
                if(afile != null)
                {
                    int k = afile.length;
                    int l = 0;
                    while(l < k) 
                    {
                        File file1 = afile[l];
                        String s = ttfanalyzer.getTtfFontName(file1.getAbsolutePath());
                        if(s != null)
                        {
                            hashmap.put(s, file1.getAbsolutePath());
                        }
                        l++;
                    }
                }
            }
            j++;
        }
        if(hashmap.isEmpty())
        {
            hashmap = null;
        }
        return hashmap;
    }

    public static Typeface getCustomFontFace(Context context)
    {
        return Typeface.createFromAsset(context.getAssets(), "fonts/GOTHIC.TTF");
    }

    public static Typeface getTypeFace(Context context, String s)
    {
        Typeface typeface1;
        try
        {
            typeface1 = Typeface.createFromFile(s);
        }
        catch(Exception exception)
        {
            Typeface typeface;
            try
            {
                typeface = Typeface.createFromAsset(context.getAssets(), s);
            }
            catch(Exception exception1)
            {
                return null;
            }
            return typeface;
        }
        return typeface1;
    }
}
