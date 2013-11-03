package com.pearson.openideas.cq5.components.twitter.servlet;

import com.google.gson.Gson;
import com.pearson.openideas.cq5.components.twitter.beans.Tweet;
import com.pearson.openideas.cq5.components.twitter.beans.User;
import com.pearson.openideas.cq5.components.twitter.compare.TwitterDateComparator;
import com.pearson.openideas.cq5.components.twitter.factory.TwitterAccountFactory;
import org.apache.felix.scr.annotations.sling.SlingServlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author John S. (jspyronis@tacitknowledge.com)
 *         Date: 17/10/2013
 *         Time: 17:12
 */

@SlingServlet(paths="/bin/twitterServlet", methods = "GET", metatype=true)
public class TwitterGetTweetsServlet extends SlingAllMethodsServlet
{

    private static final Logger LOGGER = LoggerFactory.getLogger(com.pearson.openideas.cq5.components.twitter.servlet.TwitterGetTweetsServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException
    {
        String[] usernames = request.getParameterValues("arrayTwitterAccounts");
        String maxTweetcount = request.getParameter("maxTweetcount");
        int maxTweetsPerComponent = Integer.parseInt(maxTweetcount);

        List<Tweet> listConcatenatedStatuses = new ArrayList<Tweet>();
        for (String str : usernames)
        {
            TwitterAccountFactory twitterAccountFactory = new TwitterAccountFactory();
            Twitter twitterInstance = twitterAccountFactory.getTwitterInstance();
            listConcatenatedStatuses.addAll(this.getTwitterStatusList(twitterInstance, str,  maxTweetsPerComponent));
        }

        Map<String, List> twitterResultList = new HashMap<String, List>();
        Collections.sort(listConcatenatedStatuses, Collections.reverseOrder(new TwitterDateComparator()));

        // remove elements only if size is smaller than max tweets
        if (listConcatenatedStatuses.size() > maxTweetsPerComponent)
        {
            Collection<Tweet> toBeRemoved = listConcatenatedStatuses.subList(maxTweetsPerComponent, listConcatenatedStatuses.size() );
            listConcatenatedStatuses.removeAll(toBeRemoved);
        }

        twitterResultList.put("results", listConcatenatedStatuses);

        String stringGson = new Gson().toJson(twitterResultList);

        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(stringGson);
    }

    private List<Tweet> getTwitterStatusList(Twitter twitter, String username, int maxTweetCount) throws UnsupportedEncodingException
    {
        List<Status> statuses = new ArrayList<Status>();
        List<Tweet> tweets = new ArrayList<Tweet>();

        try
        {
            Paging paging = new Paging(1, maxTweetCount);
            statuses = twitter.getUserTimeline(username, paging);
        }
        catch (TwitterException e)
        {
            LOGGER.error("Error when getting twitter user' s timeline...");
        }

        for (Status status : statuses)
        {
            tweets.add(buildTweetObject(status));
        }

        return tweets;
    }

    private Tweet buildTweetObject( Status status )
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