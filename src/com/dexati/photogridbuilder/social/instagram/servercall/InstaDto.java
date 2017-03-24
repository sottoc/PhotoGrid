package com.dexati.photogridbuilder.social.instagram.servercall;

import java.io.Serializable;

public class InstaDto
    implements Serializable
{

    private static final long serialVersionUID = 1L;
    private String instaStandardResoPhotos;
    private String instaThumbPhotos;
    private boolean isSelected;
    private String maxId;
    private String userName;

    public InstaDto()
    {
    }

    public String getInstaStandardResoPhotos()
    {
        return instaStandardResoPhotos;
    }

    public String getInstaThumbPhotos()
    {
        return instaThumbPhotos;
    }

    public String getMaxId()
    {
        return maxId;
    }

    public String getUserName()
    {
        return userName;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setInstaStandardResoPhotos(String s)
    {
        instaStandardResoPhotos = s;
    }

    public void setInstaThumbPhotos(String s)
    {
        instaThumbPhotos = s;
    }

    public void setMaxId(String s)
    {
        maxId = s;
    }

    public void setSelected(boolean flag)
    {
        isSelected = flag;
    }

    public void setUserName(String s)
    {
        userName = s;
    }

    public String toString()
    {
        return (new StringBuilder("InstaDto [instaThumbPhotos=")).append(instaThumbPhotos).append(", instaStandardResoPhotos=").append(instaStandardResoPhotos).append(", userName=").append(userName).append(", maxId=").append(maxId).append("]").toString();
    }
}
