package com.pearson.openideas.cq5.components.servlets;

import com.google.gson.Gson;
import com.pearson.openideas.cq5.components.compare.TwitterDateComparator;
import com.pearson.openideas.cq5.components.factory.TwitterAccountFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterGetTweetsServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException
    {
        String[] usernames = request.getParameterValues("arrayTwitterAccounts");
        String maxTweetcount = request.getParameter("maxTweetcount");
        int maxTweetsPerComponent = Integer.parseInt(maxTweetcount);

        List<Status> listConcatenatedStatuses = new ArrayList<Status>();
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
            Collection<Status> toBeRemoved = listConcatenatedStatuses.subList(maxTweetsPerComponent, listConcatenatedStatuses.size() );
            listConcatenatedStatuses.removeAll(toBeRemoved);
        }

        twitterResultList.put("results", listConcatenatedStatuses);

        String stringGson = new Gson().toJson(twitterResultList);

        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(stringGson);
    }

    private List<Status> getTwitterStatusList(Twitter twitter, String username, int maxTweetCount) throws UnsupportedEncodingException
    {
        List<Status> statuses = new ArrayList<Status>();
        try
        {
            Paging paging = new Paging(1, maxTweetCount);
            statuses = twitter.getUserTimeline(username, paging);
        }
        catch (TwitterException e)
        {
            LOGGER.error("Error twitter...");
        }

        return statuses;
    }
}