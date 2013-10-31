/**
 * Created: Juen 19, 2013 5:00:00 PM
 *
 * Copyright (c) 2000 - 2011, Crown Partners.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Crown Partners. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Crown Partners.
 */
package com.pearson.openideas.cq5.components.content;

import com.crownpartners.cq.quickstart.core.component.AbstractComponent;
import com.pearson.openideas.cq5.components.services.PropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.PageContext;

/**
 * Model class for the Unsubscribe component.
 * 
 * @author mandeep.singh
 * 
 */

public class CreateUnsubscribeLink extends AbstractComponent{
	 
	private static final Logger log = LoggerFactory.getLogger(Unsubscribe.class);

    private String unsubscribeUrl;
	
    /**
     * Constructor.
     * 
     * @param pageContext
     *            the pageContext to set.
     */
    public CreateUnsubscribeLink(PageContext pageContext) {
        super(pageContext);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {

        PropertiesService propertiesService = getSlingScriptHelper().getService(PropertiesService.class);

        unsubscribeUrl = propertiesService.getPublishUrl() + "/content/plc/prkc/uk/open-ideas/en/unsubscribe.html?email=${email}";

    	
    }


    /**
     * Sets new unsubscribeUrl.
     *
     * @param unsubscribeUrl New value of unsubscribeUrl.
     */
    public void setUnsubscribeUrl(String unsubscribeUrl) {
        this.unsubscribeUrl = unsubscribeUrl;
    }

    /**
     * Gets unsubscribeUrl.
     *
     * @return Value of unsubscribeUrl.
     */
    public String getUnsubscribeUrl() {
        return unsubscribeUrl;
    }
}