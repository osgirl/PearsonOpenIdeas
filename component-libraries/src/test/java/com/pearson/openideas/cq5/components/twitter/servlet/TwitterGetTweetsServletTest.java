package com.pearson.openideas.cq5.components.twitter.servlet;

import com.pearson.openideas.cq5.components.content.twitter.servlet.TwitterGetTweetsServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
* @author John Spyronis (jspyronis@tacitknowledge.com)
*/
public class TwitterGetTweetsServletTest
{

    @InjectMocks
    private TwitterGetTweetsServlet twitterGetTweetsServlet = new TwitterGetTweetsServlet();
    @Mock
    private SlingHttpServletRequest request;
    @Mock
    private SlingHttpServletResponse response;
    @Mock
    private PrintWriter printWriter;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoGet() throws IOException {

        String[] twitterAccounts = {"jspyronis", "Arsenal"};
        when(request.getParameterValues("arrayTwitterAccounts")).thenReturn(twitterAccounts);
        when(request.getParameter("maxTweetcount")).thenReturn("5");

        when(response.getWriter()).thenReturn(printWriter);
        doNothing().when(printWriter).write("GSON...");

        twitterGetTweetsServlet.doGet(request, response);
    }



}
