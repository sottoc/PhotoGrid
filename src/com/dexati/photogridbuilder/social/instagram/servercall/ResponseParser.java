package com.dexati.photogridbuilder.social.instagram.servercall;

import android.content.Context;
import com.dexati.photogridbuilder.social.instagram.oauth.InstagramSession;
import java.util.ArrayList;
import org.json.*;

// Referenced classes of package com.dexati.photogridbuilder.social.instagram.servercall:
//            InstaDto, UserInfoDto

public class ResponseParser
{

    public ResponseParser()
    {
    }

    public ArrayList parseInstaDatas(String s)
    {
    	JSONArray jsonarray;
        int i;
        int j;
        JSONObject jsonobject1;
        JSONObject jsonobject2;
        JSONObject jsonobject3;
        JSONObject jsonobject4;
        JSONObject jsonobject5;
        InstaDto instadto;
        ArrayList arraylist = new ArrayList();
        try
        {
            JSONObject jsonobject = new JSONObject(s);
            jsonarray = jsonobject.getJSONArray("data");
            i = 0;
            j = jsonarray.length();
            while(i < j)
            {
            	try{
    	        	jsonobject1 = jsonarray.getJSONObject(i);
    	            jsonobject2 = jsonobject1.getJSONObject("images");
    	            jsonobject3 = jsonobject2.getJSONObject("thumbnail");
    	            jsonobject4 = jsonobject2.getJSONObject("standard_resolution");
    	            jsonobject5 = jsonobject1.getJSONObject("user");
    	            instadto = new InstaDto();
    	            instadto.setInstaThumbPhotos(jsonobject3.getString("url"));
    	            instadto.setInstaStandardResoPhotos(jsonobject4.getString("url"));
    	            instadto.setUserName(jsonobject5.getString("username"));
    	            arraylist.add(instadto);
            	}catch(Exception e){}
                i++;
            }
        }catch(Exception exception){}
        return arraylist;
    }

    public ArrayList parseUserInfos(String s, Context context)
    {
        ArrayList arraylist;
        arraylist = new ArrayList();
        UserInfoDto userinfodto = new UserInfoDto();
        InstagramSession instagramsession = new InstagramSession(context);
        userinfodto.setId(instagramsession.getId());
        userinfodto.setPicUrl(instagramsession.getProfilePic());
        userinfodto.setUserName(instagramsession.getName());
        arraylist.add(userinfodto);
        JSONArray jsonarray;
        int i;
        int j;
        JSONObject jsonobject1;
        UserInfoDto userinfodto1;
        try
        {
        	JSONObject jsonobject = new JSONObject(s);
            jsonarray = jsonobject.getJSONArray("data");
            i = 0;
            j = jsonarray.length();
            while(i < j)
            {
            	try{
    	        	jsonobject1 = jsonarray.getJSONObject(i);
    	            userinfodto1 = new UserInfoDto();
    	            userinfodto1.setId(jsonobject1.getString("id"));
    	            userinfodto1.setPicUrl(jsonobject1.getString("profile_picture"));
    	            userinfodto1.setUserName(jsonobject1.getString("full_name"));
    	            arraylist.add(userinfodto1);
            	}catch(Exception e){}
                i++;
            }
        }catch(Exception exception){}
        return arraylist;
    }
}
