package com.pearson.openideas.cq5.components.twitter.compare;

import com.pearson.openideas.cq5.components.twitter.beans.Tweet;

import java.util.Comparator;

/**
 * This class is representing comparators for the dates of tweets.
 * @author John S. (jspyronis@tacitknowledge.com)
 *         Date: 29/10/2013
 *         Time: 14:30
 */
public class TwitterDateComparator implements Comparator<Tweet>
{
    @Override
    public int compare(Tweet t1, Tweet t2)
    {
        return t1.getCreatedAt().compareTo(t2.getCreatedAt());
    }
}
