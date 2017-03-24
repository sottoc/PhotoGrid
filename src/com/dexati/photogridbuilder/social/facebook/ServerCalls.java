package com.dexati.photogridbuilder.social.facebook;

import android.os.Bundle;
import com.facebook.*;
import com.facebook.model.GraphObject;
import java.util.ArrayList;
import java.util.Iterator;

// Referenced classes of package com.dexati.photogridbuilder.social.facebook:
//            AlbumsInfo, RequestListener, ResponseParser

public class ServerCalls
{

    public ServerCalls()
    {
    }

    public static void getImageUrl(String s, final RequestListener listener)
    {
        if(s != null)
        {
            Bundle bundle = new Bundle();
            bundle.putString("fields", "source");
            (new Request(Session.getActiveSession(), (new StringBuilder("/")).append(s).toString(), bundle, HttpMethod.GET, new com.facebook.Request.Callback() {
                public void onCompleted(Response response)
                {
                    if(response != null)
                    {
                        String s1 = ResponseParser.parseImageUrl(response.getGraphObject().getInnerJSONObject());
                        listener.onRequestComplete(s1);
                    }
                }
            })).executeAsync();
        }
    }

    public static void loadAlbumCovers(ArrayList arraylist, final AlbumListScreen.AlbumListAdapter adapter)
    {
        Iterator iterator = arraylist.iterator();
        do
        {
            AlbumsInfo album;
            do
            {
                if(!iterator.hasNext())
                {
                    return;
                }
                album = (AlbumsInfo)iterator.next();
            } while(album.getCoverPhotoId() == null);
            final AlbumsInfo album_temp = album;;
            getImageUrl(album.getCoverPhotoId(), new RequestListener() {
                public void onRequestComplete(String s)
                {
                	album_temp.setCoverPicUrl(s);
                    adapter.notifyDataSetChanged();
                }
            });
        } while(true);
    }
}
