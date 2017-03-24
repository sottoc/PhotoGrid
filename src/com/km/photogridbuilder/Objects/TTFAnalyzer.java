package com.km.photogridbuilder.Objects;

import java.io.*;

class TTFAnalyzer
{

    private RandomAccessFile m_file;

    TTFAnalyzer()
    {
        m_file = null;
    }

    private int getWord(byte abyte0[], int i)
    {
        int j = 0xff & abyte0[i];
        return 0xff & abyte0[i + 1] | j << 8;
    }

    private void read(byte abyte0[])
        throws IOException
    {
        if(m_file.read(abyte0) != abyte0.length)
        {
            throw new IOException();
        } else
        {
            return;
        }
    }

    private int readByte()
        throws IOException
    {
        return 0xff & m_file.read();
    }

    private int readDword()
        throws IOException
    {
        int i = readByte();
        int j = readByte();
        int k = readByte();
        return readByte() | (i << 24 | j << 16 | k << 8);
    }

    private int readWord()
        throws IOException
    {
        int i = readByte();
        return readByte() | i << 8;
    }

    public String getTtfFontName(String s)
    {
        int j;
        int k;
        int k1;
        int i2;
        int i;
        int l;
        int i1;
        int j1;
        byte abyte0[];
        int l1;
        int j2;
        int k2;
        int l2;
        int i3;
        String s1;
        try
        {
            RandomAccessFile randomaccessfile = new RandomAccessFile(s, "r");
            m_file = randomaccessfile;
            i = readDword();
            if(i != 0x74727565 && i != 0x10000)
            {
                return null;
            }
            j = readWord();
            readWord();
            readWord();
            readWord();
            k = 0;
            while(k < j){
    	        l = readDword();
    	        readDword();
    	        i1 = readDword();
    	        j1 = readDword();
    	        if(l != 0x6e616d65){
    	        	k++;
    	            continue;
    	        }
    	        abyte0 = new byte[j1];
    	        m_file.seek(i1);
    	        read(abyte0);
    	        k1 = getWord(abyte0, 2);
    	        l1 = getWord(abyte0, 4);
    	        i2 = 0;
    	        while(i2 < k1){
    	        	j2 = 6 + i2 * 12;
    		        k2 = getWord(abyte0, j2);
    		        if(getWord(abyte0, j2 + 6) != 4 || k2 != 1)
    		        {
    		        	i2++;
    		            continue;
    		        }
    		        l2 = getWord(abyte0, j2 + 8);
    		        i3 = l1 + getWord(abyte0, j2 + 10);
    		        if(i3 < 0)
    		        {
    		        	i2++;
    		            continue;
    		        }
    		        if(i3 + l2 >= abyte0.length)
    		        {
    		            i2++;
    		            continue;
    		        }
    		        s1 = new String(abyte0, i3, l2);
    		        return s1;
    	        }
    	        k++;
            }
            return null;
        }
        catch(FileNotFoundException filenotfoundexception)
        {
            return null;
        }
        catch(IOException ioexception)
        {
            return null;
        }
    }
}
