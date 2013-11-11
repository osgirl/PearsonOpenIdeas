package com.pearson.openideas.cq5.components.twitter.beans;

/**
 * This class represents the tweet User pojo objects.
 * @author John S. (jspyronis@tacitknowledge.com)
 *         Date: 01/11/2013
 *         Time: 14:25
 */
public class User
{
    private String name;
    private String screenName;
    private String profileImageUrl;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getScreenName()
    {
        return screenName;
    }

    public void setScreenName(String screenName)
    {
        this.screenName = screenName;
    }


    public String getProfileImageUrl()
    {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl)
    {
        this.profileImageUrl = profileImageUrl;
    }
}
