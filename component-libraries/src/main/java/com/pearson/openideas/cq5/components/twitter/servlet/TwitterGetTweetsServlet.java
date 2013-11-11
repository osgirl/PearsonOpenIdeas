package com.pearson.openideas.cq5.components.twitter.servlet;

import com.google.gson.Gson;
import com.pearson.openideas.cq5.components.twitter.beans.Tweet;
import com.pearson.openideas.cq5.components.twitter.compare.TwitterDateComparator;
import com.pearson.openideas.cq5.components.twitter.factory.TweetObjectFactory;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is representing a servlet for returning a json with all information related to latest tweets from twitter user accounts.
 * @author John S. (jspyronis@tacitknowledge.com)
 *         Date: 17/10/2013
 *         Time: 17:12
 */

@SlingServlet(paths="/bin/twitterServlet", methods = "GET", metatype=true)
public class TwitterGetTweetsServlet extends SlingAllMethodsServlet
{
    private static final Logger LOGGER = LoggerFactory.getLogger(com.pearson.openideas.cq5.components.twitter.servlet.TwitterGetTweetsServlet.class);


    /**
     * This method writes a json response. The request includes all twitter accounts usernames and max number of tweets.

     * @param request sling request.
     * @param response sling response to write the json in.
     *
     */

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

        int maxExistingTweets = 0;
        if (listConcatenatedStatuses.size() <  maxTweetsPerComponent)
        {
            maxExistingTweets = listConcatenatedStatuses.size();
        }
        else
        {
            maxExistingTweets = maxTweetsPerComponent;
        }

        twitterResultList.put("results", listConcatenatedStatuses.subList(0, maxExistingTweets));

        String stringGson = new Gson().toJson(twitterResultList);

        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(stringGson);
    }

    /**
     * This method returns a list with all Tweets, based on the authenticated twitter instance,
     * the username of the account
     * and the maximum number of returned tweets.
     *
     * @param twitter The created twitter instance from TwitterAccountFactory.
     * @param username The username of the twitter account.
     * @param maxTweetCount The maximum number of returned tweets.
     * @return list of tweets based on these parameters.
    */
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

        TweetObjectFactory tweetObjectFactory = new TweetObjectFactory();
        for (Status status : statuses)
        {
            tweets.add(tweetObjectFactory.getTweetForCurrentStatus(status));
        }

        return tweets;
    }
}