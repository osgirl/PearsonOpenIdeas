package com.pearson.openideas.cq5.components.content;

import com.crownpartners.cq.quickstart.core.component.AbstractComponent;
import com.day.cq.wcm.api.WCMMode;
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
        stringWcmMode = mode.toString();
        LOGGER.debug("The run mode is ::::::::::::::::::: " + mode);
    }

    public String getStringWcmMode()
    {
        return this.stringWcmMode;
    }


}
