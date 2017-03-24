package com.dexati.photogridbuilder.social.facebook;


public class UserInfo
{

    private String firstName;
    private String gender;
    private String lastName;
    private int userId;

    public UserInfo()
    {
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getGender()
    {
        return gender;
    }

    public String getLastName()
    {
        return lastName;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setFirstName(String s)
    {
        firstName = s;
    }

    public void setGender(String s)
    {
        gender = s;
    }

    public void setLastName(String s)
    {
        lastName = s;
    }

    public void setUserId(int i)
    {
        userId = i;
    }

    public String toString()
    {
        return (new StringBuilder("UserInfo [userId=")).append(userId).append(", firstName=").append(firstName).append(", lastName=").append(lastName).append(", gender=").append(gender).append("]").toString();
    }
}
