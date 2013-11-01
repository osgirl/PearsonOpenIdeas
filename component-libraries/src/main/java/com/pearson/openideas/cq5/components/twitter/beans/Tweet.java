package com.pearson.openideas.cq5.components.twitter.beans;

import java.util.Date;

/**
 * @author John S. (jspyronis@tacitknowledge.com)
 *         Date: 01/11/2013
 *         Time: 14:49
 */
public class Tweet
{
    private User user;
    private String text;
    private Date createdAt;
    private String id;

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }




}
