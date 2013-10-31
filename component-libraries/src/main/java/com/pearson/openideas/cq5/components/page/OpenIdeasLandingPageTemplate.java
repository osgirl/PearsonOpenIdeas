/**
 * Created: Apr 18, 2013 4:03:00 PM
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
package com.pearson.openideas.cq5.components.page;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

/**
 * The Landing Page template.
 * 
 * @version 2.0
 * 
 * @author Todd Guerra
 */
public class OpenIdeasLandingPageTemplate extends OpenIdeasMasterTemplate {

    private static final Logger log = LoggerFactory.getLogger(OpenIdeasLandingPageTemplate.class);
    private String pageTitle = null;

    /**
     * Constructor sets the page context.
     * 
     * @param pageContext
     *            The page context
     */
    public OpenIdeasLandingPageTemplate(PageContext pageContext) {
        super(pageContext);

        String namespace = null;
        try {
            namespace = getCurrentNode().getNode("landingPage").getProperty("landingPageType").getString();
        } catch (Exception ex) {
            log.error("Failed to get the landing page", ex);
        }

        if (namespace != null) {
            // pull the selector from the URL
            if (getSlingRequest().getRequestPathInfo().getSelectors() != null
                    && getSlingRequest().getRequestPathInfo().getSelectors().length > 0) {
                String selector = getSlingRequest().getRequestPathInfo().getSelectors()[0];
                if (StringUtils.isNotBlank(selector)) {
                    TagManager tagManager = getResourceResolver().adaptTo(TagManager.class);
                    Tag tag = tagManager.resolve(namespace + ':' + selector);
                    if (tag != null) {
                        pageTitle = getPagetitleprefix() + tag.getTitle();
                    }
                }
            } else {
                log.error("no selector present");
            }
        }

        if (pageTitle == null) {
            pageTitle = super.getPageTitle();
        }
    }

    /**
     * Get the pageTitle.
     * 
     * @return The pageTitle
     */
    public String getPageTitle() {
        return pageTitle;
    }

}
