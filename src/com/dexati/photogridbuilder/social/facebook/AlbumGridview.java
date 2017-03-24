package com.dexati.photogridbuilder.social.facebook;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.*;
import java.util.ArrayList;

// Referenced classes of package com.dexati.photogridbuilder.social.facebook:
//            AlbumListScreen, EndLessListener

public class AlbumGridview extends GridView
    implements android.widget.AbsListView.OnScrollListener
{

    private AlbumListScreen.AlbumListAdapter adapter;
    private Context context;
    private boolean isLoading;
    private EndLessListener listener;

    public AlbumGridview(Context context1)
    {
        super(context1);
        setOnScrollListener(this);
        context = context1;
    }

    public AlbumGridview(Context context1, AttributeSet attributeset)
    {
        super(context1, attributeset);
        setOnScrollListener(this);
        context = context1;
    }

    public AlbumGridview(Context context1, AttributeSet attributeset, int i)
    {
        super(context1, attributeset, i);
        setOnScrollListener(this);
        context = context1;
    }

    public void addNewData(ArrayList arraylist)
    {
        if(context instanceof AlbumListScreen)
        {
            ((AlbumListScreen)context).showFooter(false);
        }
        adapter.addAll(arraylist);
        adapter.notifyDataSetChanged();
        isLoading = false;
    }

    public void onScroll(AbsListView abslistview, int i, int j, int k)
    {
        while(getAdapter() == null || getAdapter().getCount() == 0 || j + i < k || isLoading) 
        {
            return;
        }
        if(context instanceof AlbumListScreen)
        {
            ((AlbumListScreen)context).showFooter(true);
        }
        isLoading = true;
        listener.loadData();
    }

    public void onScrollStateChanged(AbsListView abslistview, int i)
    {
    }

    public void setAdapter(AlbumListScreen.AlbumListAdapter albumlistadapter)
    {
        super.setAdapter(albumlistadapter);
        adapter = albumlistadapter;
        if(context instanceof AlbumListScreen)
        {
            ((AlbumListScreen)context).showFooter(false);
        }
    }

    public void setListener(EndLessListener endlesslistener)
    {
        listener = endlesslistener;
    }
}
