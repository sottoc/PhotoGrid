package com.dexati.photogridbuilder.social.facebook;


public class FacebookDto
{

    private boolean isSelected;
    private String url;

    public FacebookDto()
    {
    }

    public String getUrl()
    {
        return url;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean flag)
    {
        isSelected = flag;
    }

    public void setUrl(String s)
    {
        url = s;
    }
}
