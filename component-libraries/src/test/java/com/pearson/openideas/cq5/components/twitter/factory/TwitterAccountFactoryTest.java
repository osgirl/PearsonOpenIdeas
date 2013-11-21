package com.pearson.openideas.cq5.components.twitter.factory;

import com.pearson.openideas.cq5.components.content.twitter.factory.TwitterAccountFactory;
import com.tacitknowledge.noexcuses.TestManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import twitter4j.Twitter;

import static org.mockito.Mockito.when;


/**
* @author John Spyronis (jspyronis@tacitknowledge.com)
*/
public class TwitterAccountFactoryTest {

    @Mock
    private Twitter twitter;
    @Mock
    private com.pearson.openideas.cq5.components.content.twitter.factory.TwitterAccountFactory twitterAccountFactory;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testConstructorOfTwitterFactory() throws Exception
    {
        TestManager.testConstruction(TwitterAccountFactory.class);
    }


    @Test
    public void testGetTwitterInstance() throws Exception
    {
        when(twitterAccountFactory.getTwitterInstance()).thenReturn(twitter);
    }


}
