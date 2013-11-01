package com.pearson.openideas.cq5.components.twitter.factory;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author John S. (jspyronis@tacitknowledge.com)
 *         Date: 31/10/2013
 *         Time: 13:02
 */

public class TwitterAccountFactory
{
    private static final String consumerKey = "KbXkX3X9vAPHfIwrkASXdA";
    private static final String consumerSecret = "0ljYH0DigNUQ4SOCuXm6NPUAeYdN6zbxzsYHYzmq380";
    private static final String accessToken = "117012393-gZ4crhozO7QHEUlQkI0QMmg9iyCdnm1soS4MMRyE";
    private static final String accessTokenSecret = "rmdAK3DEvR6HkVTMM12CghsUTlOFBJBQ02vQQJFSIg";

    protected Twitter twitter;
    private ConfigurationBuilder configBuilder;

    public TwitterAccountFactory()
    {
        configBuilder = new ConfigurationBuilder();
        configBuilder.setDebugEnabled(true);
        configBuilder.setOAuthConsumerKey(consumerKey);
        configBuilder.setOAuthConsumerSecret(consumerSecret);
        configBuilder.setOAuthAccessToken(accessToken);
        configBuilder.setOAuthAccessTokenSecret(accessTokenSecret);

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