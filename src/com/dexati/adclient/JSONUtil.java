package com.dexati.adclient;

import android.util.Log;
import java.io.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil
{

    public JSONUtil()
    {
    }

    public static JSONObject getJSONfromURL(String s)
    {
        String s1 = "";
        BufferedReader bufferedreader;
        StringBuilder stringbuilder;
        try{
	        InputStream inputstream1 = (new DefaultHttpClient()).execute(new HttpGet(s)).getEntity().getContent();
	        InputStream inputstream = inputstream1;
	        bufferedreader = new BufferedReader(new InputStreamReader(inputstream, "iso-8859-1"), 8);
	        stringbuilder = new StringBuilder();
	        while(true){
		        String s2 = bufferedreader.readLine();
		        if(s2 == null)
		        	break;
		        stringbuilder.append((new StringBuilder(String.valueOf(s2))).append("\n").toString());
	        }
	        String s3;
	        inputstream.close();
	        s3 = stringbuilder.toString();
	        s1 = s3;
	        JSONObject jsonobject;
            jsonobject = new JSONObject(s1);
            return jsonobject;
        }catch(Exception e){
        	return null;
        }
    }
}
