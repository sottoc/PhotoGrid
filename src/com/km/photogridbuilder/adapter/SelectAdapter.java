package com.km.photogridbuilder.adapter;

import android.content.Context;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class SelectAdapter extends BaseAdapter
{

    private Context mContext;
    private Integer mInfoList[];

    public SelectAdapter(Context context, Integer ainteger[])
    {
        mContext = context;
        mInfoList = ainteger;
    }

    public int getCount()
    {
        return mInfoList.length;
    }

    public Object getItem(int i)
    {
        return Integer.valueOf(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        LayoutInflater layoutinflater = (LayoutInflater)mContext.getSystemService("layout_inflater");
        if(view == null)
        {
            view = layoutinflater.inflate(0x7f03002e, null);
        }
        ((ImageView)view.findViewById(0x7f0900d1)).setImageResource(mInfoList[i].intValue());
        return view;
    }
}
