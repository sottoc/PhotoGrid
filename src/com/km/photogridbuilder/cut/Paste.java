package com.km.photogridbuilder.cut;


public class Paste
{

    private Long modifiedDate;
    private String url;

    public Paste()
    {
    }

    public Long getModifiedDate()
    {
        return modifiedDate;
    }

    public String getUrl()
    {
        return url;
    }

    public void setModifiedDate(long l)
    {
        modifiedDate = Long.valueOf(l);
    }

    public void setUrl(String s)
    {
        url = s;
    }
}
