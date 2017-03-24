package com.dexati.photogridbuilder.social.facebook;


public class AlbumsInfo
{

    private String albumName;
    private int count;
    private String coverPhoto;
    private String coverPicUrl;
    private String fbId;

    public AlbumsInfo()
    {
    }

    public String getAlbumName()
    {
        return albumName;
    }

    public int getCount()
    {
        return count;
    }

    public String getCoverPhotoId()
    {
        return coverPhoto;
    }

    public String getCoverPicUrl()
    {
        return coverPicUrl;
    }

    public String getFbId()
    {
        return fbId;
    }

    public void setAlbumName(String s)
    {
        albumName = s;
    }

    public void setCount(int i)
    {
        count = i;
    }

    public void setCoverPhotoId(String s)
    {
        coverPhoto = s;
    }

    public void setCoverPicUrl(String s)
    {
        coverPicUrl = s;
    }

    public void setFbId(String s)
    {
        fbId = s;
    }

    public String toString()
    {
        return (new StringBuilder("AlbumsInfo [fbId=")).append(fbId).append(", albumName=").append(albumName).append(", count=").append(count).append(", coverPhoto=").append(coverPhoto).append("]").toString();
    }
}
