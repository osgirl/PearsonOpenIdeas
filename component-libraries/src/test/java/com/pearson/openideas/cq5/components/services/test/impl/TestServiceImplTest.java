package com.pearson.openideas.cq5.components.services.test.impl;

import com.pearson.openideas.cq5.components.services.test.TestService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.service.component.ComponentContext;

/**
 * @author John S. (jspyronis@tacitknowledge.com)
 *         Date: 22/11/2013
 *         Time: 16:01
 */
public class TestServiceImplTest
{
    @InjectMocks
    private TestService testService = new TestServiceImpl();
    @Mock
    private ComponentContext componentContext;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRunTestService() throws Exception
    {
        testService.runTestService();
    }
}
