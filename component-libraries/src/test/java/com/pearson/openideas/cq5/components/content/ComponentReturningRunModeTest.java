package com.pearson.openideas.cq5.components.content;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.settings.SlingSettingsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.jsp.tagext.TagSupport;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;

/**
 * @author John S. (jspyronis@tacitknowledge.com)
 *         Date: 21/11/2013
 *         Time: 16:55
 */
public class ComponentReturningRunModeTest
{
    private static final String authorMode = "author";
    private static final String publishMode = "publish";

    private Set<String> runModesWithAuthor;
    private Set<String> runModesWithPublish;

    @Mock
    private SlingHttpServletRequest slingHttpServletRequest;
    @Mock
    private SlingBindings slingBindings;
    @Mock
    private SlingScriptHelper scriptHelper;
    @Mock
    private SlingSettingsService slingSettingsService;

    @Mock
    private TagSupport tagSupport;

    @Mock
    private ComponentReturningRunMode componentReturningRunMode;


    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        addModesToSets();

        when(slingHttpServletRequest.getAttribute(SlingBindings.class.getName())).thenReturn(slingBindings);
        when(slingBindings.getSling()).thenReturn(scriptHelper);
        when(scriptHelper.getService(SlingSettingsService.class)).thenReturn(slingSettingsService);

    }

    @Test()
    public void testGetRunModeAuthor() throws Exception
    {
        when(slingSettingsService.getRunModes()).thenReturn(runModesWithAuthor);

        String runMode = componentReturningRunMode.getRunMode();
        //assertEquals("Author running mode is the one we are waiting", authorMode, runMode);
    }

    @Test
    public void testGetRunModePublish() throws Exception
    {
        when(slingSettingsService.getRunModes()).thenReturn(runModesWithPublish);

        String runMode = componentReturningRunMode.getRunMode();
        //assertEquals("Author running mode is the one we are waiting", publishMode, runMode);
    }

    private void addModesToSets()
    {
        runModesWithAuthor = new HashSet<String>();
        runModesWithAuthor.add("cq1stVersion");
        runModesWithAuthor.add(authorMode);
        runModesWithAuthor.add("vvvvv");
        runModesWithAuthor.add("vcvcvc");

        runModesWithPublish = new HashSet<String>();
        runModesWithPublish.add("cq1stVersion");
        runModesWithPublish.add(publishMode);
        runModesWithPublish.add("vvvvv");
        runModesWithPublish.add("vcvcvc");
    }
}
