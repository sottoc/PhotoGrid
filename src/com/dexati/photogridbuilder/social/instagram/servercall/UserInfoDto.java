package com.dexati.photogridbuilder.social.instagram.servercall;

import java.io.Serializable;

public class UserInfoDto
    implements Serializable
{

    private String id;
    private String picUrl;
    private String userName;

    public UserInfoDto()
    {
    }

    public String getId()
    {
        return id;
    }

    public String getPicUrl()
    {
        return picUrl;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setId(String s)
    {
        id = s;
    }

    public void setPicUrl(String s)
    {
        picUrl = s;
    }

    public void setUserName(String s)
    {
        userName = s;
    }
}
