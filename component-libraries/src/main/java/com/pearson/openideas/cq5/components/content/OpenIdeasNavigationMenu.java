/**
 * Created: Apr 30, 2013 12:07:00 AM
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
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.pearson.openideas.cq5.components.utils.ExploreUtils;
import com.pearson.openideas.cq5.components.utils.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.PageContext;

/**
 * The navigation menu component.
 * 
 * @version 2.0
 * 
 * @author Mandeep Singh
 */
public class OpenIdeasNavigationMenu extends AbstractComponent {

    private ExploreUtils exploreUtils;
    private String currentUrl;
    private String logoutUrl;

    // the logger
    private static final Logger log = LoggerFactory.getLogger(OpenIdeasNavigationMenu.class);

    /**
     * Constructor.
     * 
     * @param pageContext
     *            The page context
     */
    public OpenIdeasNavigationMenu(PageContext pageContext) {
        super(pageContext);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        exploreUtils = new ExploreUtils(getPageProperties(), getResourceResolver().adaptTo(TagManager.class));
        currentUrl = UrlUtils.getPageUrl(getCurrentPage(), getSlingScriptHelper(), getSlingRequest());
        log.debug("current URL: " + currentUrl);

        if (currentUrl.contains("=") && currentUrl.contains("?")) {
            logoutUrl = currentUrl + "&logout=true";
        } else if (currentUrl.contains("?")) {
            logoutUrl = currentUrl + "logout=true";
        } else {
            logoutUrl = currentUrl + "?logout=true";
        }

    }

    /**
     * Get the categoryTags.
     * 
     * @return The categoryTags
     */
    public Tag[] getCategoryTags() {
        return exploreUtils.getCategoryTags();
    }

    /**
     * Get the themeTags.
     * 
     * @return The themeTags
     */
    public Tag[] getThemeTags() {
        return exploreUtils.getThemeTags();
    }

    /**
     * Get the regionTags.
     * 
     * @return The regionTags
     */
    public Tag[] getRegionTags() {
        return exploreUtils.getRegionTags();
    }

    /**
     * Get the currentUrl.
     * 
     * @return The currentUrl
     */
    public String getCurrentUrl() {
        return currentUrl;
    }

    /**
     * Get the logoutUrl.
     * 
     * @return The logoutUrl
     */
    public String getLogoutUrl() {
        return logoutUrl;
    }

}
