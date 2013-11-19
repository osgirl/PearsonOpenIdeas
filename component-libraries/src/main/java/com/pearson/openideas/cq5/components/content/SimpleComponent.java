package com.pearson.openideas.cq5.components.content;

import com.crownpartners.cq.quickstart.core.component.AbstractComponent;
import com.day.cq.wcm.api.WCMMode;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.PageContext;

/**
 * @author John S. (jspyronis@tacitknowledge.com)
 *         Date: 19/11/2013
 *         Time: 15:05
 */

public class SimpleComponent extends AbstractComponent
{
    @Reference
    private SlingSettingsService slingSettings;


    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleComponent.class);
    private String stringWcmMode;

    /**
     * Constructor for simple Component...
     * @param pageContext
     */
    public SimpleComponent(PageContext pageContext)
    {
        super(pageContext);
    }


    @Override
    public void init()
    {

        WCMMode mode = WCMMode.fromRequest(getSlingRequest());
        //stringWcmMode = slingSettingsService.getRunModes().toString();

        stringWcmMode = "test";

//        // Get run mode of server
//        final Set<String> runModes = slingSettings.getRunModes();
//
//        LOGGER.debug("----------------------------------------------------------------------------------------------------" + runModes);
//
//        for (String rm : runModes)
//        {
//            if (rm.equalsIgnoreCase("publish") || rm.equalsIgnoreCase("author"))
//            {
//                stringWcmMode = rm;
//                break;
//            }
//        }

        LOGGER.debug("The run mode is ::::::::::::::::::: " + stringWcmMode);
    }

    public String getStringWcmMode()
    {
        return this.stringWcmMode;
    }


}
