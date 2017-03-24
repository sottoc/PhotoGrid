package com.dexati.photogridbuilder.social.facebook;

import android.util.Log;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

// Referenced classes of package com.dexati.photogridbuilder.social.facebook:
//            FacebookDto, AlbumsInfo

public class ResponseParser
{

    public ResponseParser()
    {
    }

    public static ArrayList getAlbumImages(JSONObject jsonobject)
    {
        ArrayList arraylist = new ArrayList();
        JSONArray jsonarray;
        int i;
        int j;
        JSONObject jsonobject1;
        FacebookDto facebookdto;
        try
        {
            jsonarray = jsonobject.getJSONArray("data");
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return arraylist;
        }
        i = 0;
        j = jsonarray.length();
        while(i < j){
        	try{
		        jsonobject1 = jsonarray.getJSONObject(i);
		        if(jsonobject1.has("source"))
		        {
		            facebookdto = new FacebookDto();
		            facebookdto.setUrl(jsonobject1.getString("source"));
		            arraylist.add(facebookdto);
		        }
        	}catch(Exception e){}
	        i++;
        }
        return arraylist;
    }

    public static ArrayList parseAlbumData(JSONObject jsonobject)
    {
        ArrayList arraylist = new ArrayList();
        JSONArray jsonarray;
        int i;
        int j;
        JSONObject jsonobject1;
        AlbumsInfo albumsinfo;
        try
        {
            jsonarray = jsonobject.getJSONArray("data");
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return arraylist;
        }
        i = 0;
        j = jsonarray.length();
        while(i < j)
        {
        	try{
	        	jsonobject1 = jsonarray.getJSONObject(i);
	            albumsinfo = new AlbumsInfo();
	            if(jsonobject1.has("id"))
	            {
	                albumsinfo.setFbId(jsonobject1.getString("id"));
	            }
	            if(jsonobject1.has("name"))
	            {
	                albumsinfo.setAlbumName(jsonobject1.getString("name"));
	            }
	            if(jsonobject1.has("cover_photo"))
	            {
	                albumsinfo.setCoverPhotoId(jsonobject1.getString("cover_photo"));
	            }
	            if(jsonobject1.has("count"))
	            {
	                albumsinfo.setCount(jsonobject1.getInt("count"));
	            }
	            Log.e("albums ", albumsinfo.toString());
	            arraylist.add(albumsinfo);
        	}catch(Exception e){}
            i++;
        }
        return arraylist;
    }

    public static String parseImageUrl(JSONObject jsonobject)
    {
        boolean flag;
        String s;
        String s1;
        try
        {
            flag = jsonobject.has("source");
            s = null;
            if(!flag)
            {
                return s;
            }
            s1 = jsonobject.getString("source");
            s = s1;
            return s;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
    }
}
