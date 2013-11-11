package com.pearson.openideas.cq5.components.twitter.factory;

import com.pearson.openideas.cq5.components.twitter.beans.Tweet;
import com.pearson.openideas.cq5.components.twitter.beans.User;
import twitter4j.Status;

/**
 * * This class is representing a factory that creates tweets based on tweet statuses.
 * @author John Spyronis (jspyronis@tacitknowledge.com)
 * returns tweet bean object based on twitter status
 */
public class TweetObjectFactory
{
    /**
     * This method returns the Tweet object for current twitter status.
     *
     * @param status The status for which we want to get the Tweet POJO.
     * @return the Tweet POJO
     */
    public Tweet getTweetForCurrentStatus(Status status)
    {
        User user = new User();
        user.setName(status.getUser().getName());
        user.setScreenName(status.getUser().getScreenName());
        user.setProfileImageUrl(status.getUser().getProfileImageURL());

        Tweet tweet = new Tweet();
        tweet.setUser(user);
        tweet.setText(status.getText());
        tweet.setCreatedAt(status.getCreatedAt());
        tweet.setId(String.valueOf(status.getId()));

        return tweet;
    }
}
