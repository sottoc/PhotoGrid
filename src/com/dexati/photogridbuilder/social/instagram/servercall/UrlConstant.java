package com.dexati.photogridbuilder.social.instagram.servercall;


public class UrlConstant
{

    public static String BASE_URL = "https://api.instagram.com/v1/media/popular?client_id=";
    public static final String CALLBACK_URL = "instagram://connect";
    public static String CLIENT_ID = "49f1375bf71544e4be63f92f1ed371e9";
    public static final String CLIENT_SECRET = "e525447612934d6ea7d255ca0ba3cd49";

    public UrlConstant()
    {
    }

    public static String getUserFollowingUrl(String s, String s1)
    {
        return (new StringBuilder("https://api.instagram.com/v1/users/")).append(s).append("/follows?access_token=").append(s1).toString();
    }

    public static String getUserPhotoUrl(String s, String s1)
    {
        return (new StringBuilder("https://api.instagram.com/v1/users/")).append(s).append("/media/recent/?access_token=").append(s1).append("&count=100").toString();
    }

    public static String getUserPhotoUrlWithMaxID(String s, String s1, String s2)
    {
        return (new StringBuilder("https://api.instagram.com/v1/users/")).append(s).append("/media/recent/?access_token=").append(s1).append("&max_id=").append(s2).toString();
    }

}
