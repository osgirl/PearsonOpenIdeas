/**
 * Created: Jun 19, 2013 11:02:05 AM
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
package com.pearson.openideas.cq5.components.services;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;

/**
 * Service for the site properties.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 */
@Component(label = "Pearson Open Ideas Properties Service", immediate = true, metatype = true)
@Service(PropertiesService.class)
public class PropertiesService {

    private static final Logger log = LoggerFactory.getLogger(PropertiesService.class);

    private String publishUrl;

    @Property(label = "Publish URL", description = "URL to the publish instance that will be used in the emails", value = PropertiesService.DEFAULT_URL)
    private static final String PUB_URL = "propertiesService.restUrl";
    private static final String DEFAULT_URL = "http://localhost:4503";

    /**
     * activates the service and sets the properties to usable variables.
     * 
     * @param context
     *            the component context.
     * @throws Exception
     *             if there is a problem activating
     */
    protected void activate(ComponentContext context) throws Exception {
        log.info("Service Client Class activating");
        Dictionary<?, ?> properties = context.getProperties();
        publishUrl = PropertiesUtil.toString(properties.get(PUB_URL), DEFAULT_URL);
    }

    /**
     * Gets the publish URL value.
     * 
     * @return the publish URL from the OSGI config
     */
    public String getPublishUrl() {
        return publishUrl;
    }

}
