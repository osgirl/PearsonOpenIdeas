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

/**
 * The Artical template.
 * 
 * @version 2.0
 * 
 * @author Todd Guerra
 */
public class OpenIdeasArticleTemplate extends OpenIdeasMasterTemplate {

    private String pageTitle;

    /**
     * Constructor sets the page context.
     * 
     * @param pageContext
     *            The page context
     */
    public OpenIdeasArticleTemplate(PageContext pageContext) {
        super(pageContext);

        pageTitle = getProperties().get("jcr:title", "");
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
