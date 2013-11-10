package com.pearson.openideas.cq5.components.twitter.factory;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author John S. (jspyronis@tacitknowledge.com)
 *         Date: 31/10/2013
 *         Time: 13:02
 *         sets up a new twitter OAUTH configuration for the new API (v1.1)
 */

public class TwitterAccountFactory
{
    private static final String CONSUMER_KEY = "KbXkX3X9vAPHfIwrkASXdA";
    private static final String CONSUMER_SECRET = "0ljYH0DigNUQ4SOCuXm6NPUAeYdN6zbxzsYHYzmq380";
    private static final String ACCESS_TOKEN = "117012393-gZ4crhozO7QHEUlQkI0QMmg9iyCdnm1soS4MMRyE";
    private static final String ACCESS_TOKEN_SECRET = "rmdAK3DEvR6HkVTMM12CghsUTlOFBJBQ02vQQJFSIg";

    private Twitter twitter;

    public TwitterAccountFactory()
    {
        ConfigurationBuilder configBuilder = new ConfigurationBuilder();
        configBuilder.setDebugEnabled(true);
        configBuilder.setOAuthConsumerKey(CONSUMER_KEY);
        configBuilder.setOAuthConsumerSecret(CONSUMER_SECRET);
        configBuilder.setOAuthAccessToken(ACCESS_TOKEN);
        configBuilder.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);

        //use the ConfigBuilder.build() method and pass the result to the TwitterFactory
        TwitterFactory tf = new TwitterFactory(configBuilder.build());

        //you can now get authenticated instance of Twitter object.
        twitter = tf.getInstance();
    }

    public Twitter getTwitterInstance()
    {
        return twitter;
    }


}