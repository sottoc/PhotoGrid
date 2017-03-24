package com.km.photogridbuilder.util;

import android.os.Environment;
import android.util.Log;
import java.io.*;
import java.nio.charset.Charset;

public final class IOUtilities
{

    public static final int IO_BUFFER_SIZE = 8192;
    private static final String TAG = "IOUtilities";

    public IOUtilities()
    {
    }

    public static void closeStream(Closeable closeable)
    {
    	try{
	        if(closeable != null)
	        {
	        	closeable.close();
	        }
    	}catch(Exception e){}
        
    }

    public static void copy(InputStream inputstream, OutputStream outputstream)
        throws IOException
    {
        byte abyte0[] = new byte[8192];
        do
        {
            int i = inputstream.read();
            if(i == -1)
            {
                return;
            }
            outputstream.write(abyte0, 0, i);
        } while(true);
    }

    public static File getExternalFile(String s)
    {
        return new File(Environment.getExternalStorageDirectory(), s);
    }

    public static byte[] streamToByteArray(InputStream inputstream)
    {
        ByteArrayOutputStream bytearrayoutputstream;
        byte abyte0[];
        bytearrayoutputstream = new ByteArrayOutputStream();
        abyte0 = new byte[16384];
        int i;
        try{
	        while(true){
	            i = inputstream.read(abyte0, 0, abyte0.length);
	            if(i == -1)
	            	break;
	        	bytearrayoutputstream.write(abyte0, 0, i);
	        }
	        bytearrayoutputstream.flush();
	        return bytearrayoutputstream.toByteArray();
        }catch(Exception e){
        	return null;
        }
    }

    public static String streamToString(InputStream inputstream)
    {
        return streamToString(inputstream, null);
    }

    public static String streamToString(InputStream inputstream, Charset charset)
    {
        StringBuffer stringbuffer;
        String s;
        BufferedReader bufferedreader;
        if(charset == null)
        {
            bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
        } else
        {
            bufferedreader = new BufferedReader(new InputStreamReader(inputstream, charset));
        }
        stringbuffer = new StringBuffer();
        try
        {
        	while(true){
    	        s = bufferedreader.readLine();
    	        if(s == null)
    	        	break;
   	        	stringbuffer.append((new StringBuilder(String.valueOf(s))).append("\n").toString());
            }
            inputstream.close();
         }
         catch(Exception exception4) { }
         return stringbuffer.toString();
    }
}
