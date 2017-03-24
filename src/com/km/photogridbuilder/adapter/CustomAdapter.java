package com.km.photogridbuilder.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.km.photogridbuilder.util.FontUtils;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter
{

    private Activity activity;
    private String array_paths[];
    private ArrayList data;
    LayoutInflater inflater;
    private boolean isFont;

    public CustomAdapter(Context context, int i, ArrayList arraylist, boolean flag, String as[])
    {
        super(context, i, arraylist);
        array_paths = null;
        array_paths = as;
        activity = (Activity)context;
        data = arraylist;
        isFont = flag;
        inflater = (LayoutInflater)activity.getSystemService("layout_inflater");
    }

    public View getCustomView(int i, View view, ViewGroup viewgroup)
    {
        View view1 = inflater.inflate(0x7f030034, viewgroup, false);
        String s = (String)data.get(i);
        TextView textview = (TextView)view1.findViewById(0x7f0900da);
        if(isFont)
        {
            FontUtils.setTypeface(activity, textview, array_paths[i]);
        }
        textview.setText(s.trim());
        return view1;
    }

    public View getDropDownView(int i, View view, ViewGroup viewgroup)
    {
        return getCustomView(i, view, viewgroup);
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        return getCustomView(i, view, viewgroup);
    }
}
