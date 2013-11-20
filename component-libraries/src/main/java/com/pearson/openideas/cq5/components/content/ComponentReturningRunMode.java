package com.pearson.openideas.cq5.components.content;
/**
 * @author John S. (jspyronis@tacitknowledge.com)
 *         Date: 19/11/2013
 *         Time: 15:05
 */

import com.crownpartners.cq.quickstart.core.component.AbstractComponent;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.PageContext;
import java.util.Set;

public class ComponentReturningRunMode extends AbstractComponent{

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentReturningRunMode.class);

    private String runMode;
    public String getRunMode()
    {
        return runMode;
    }
    public void setRunMode(String runMode)
    {
        this.runMode = runMode;
    }

    public ComponentReturningRunMode(PageContext pageContext)
    {
        super(pageContext);
    }

    @Override
    public void init()
    {
        SlingBindings bindings = (SlingBindings) getSlingRequest().getAttribute(SlingBindings.class.getName());
        SlingScriptHelper scriptHelper = bindings.getSling();
        SlingSettingsService settingsService = scriptHelper.getService(SlingSettingsService.class);

        final Set<String> runModes = settingsService.getRunModes();

        for (String runMode : runModes)
        {
            if (runMode.equals("author"))
            {
                setRunMode("author");
                break;
            }
            else if (runMode.equals("publish"))
            {
                setRunMode("publish");
                break;
            }
        }
    }
}