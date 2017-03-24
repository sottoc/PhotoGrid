package com.km.photogridbuilder.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.*;
import android.widget.*;
import com.km.photogridbuilder.MainActivity;
import com.km.photogridbuilder.Objects.ImageItem;
import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter
{
    static class ViewHolder
    {

        ImageView image;
        TextView imageTitle;

        ViewHolder()
        {
        }
    }


    private Context context;
    private ArrayList data;
    private int layoutResourceId;

    public GridViewAdapter(Context context1, int i, ArrayList arraylist)
    {
        super(context1, i, arraylist);
        data = new ArrayList();
        layoutResourceId = i;
        context = context1;
        data = arraylist;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        View view1 = view;
        ViewHolder viewholder;
        ImageItem imageitem;
        if(view1 == null)
        {
            view1 = ((Activity)context).getLayoutInflater().inflate(layoutResourceId, viewgroup, false);
            viewholder = new ViewHolder();
            viewholder.imageTitle = (TextView)view1.findViewById(0x7f0900d7);
            viewholder.image = (ImageView)view1.findViewById(0x7f0900d8);
            view1.setTag(viewholder);
        } else
        {
            viewholder = (ViewHolder)view1.getTag();
        }
        imageitem = (ImageItem)data.get(i);
        viewholder.imageTitle.setText(imageitem.getTitle());
        viewholder.image.setImageBitmap(imageitem.getImage());
        view1.setLayoutParams(new android.widget.AbsListView.LayoutParams(-1, MainActivity.height / 3));
        return view1;
    }
}
