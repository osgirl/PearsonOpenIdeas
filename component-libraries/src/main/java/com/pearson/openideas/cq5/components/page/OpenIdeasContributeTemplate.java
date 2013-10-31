/**
 * Created: May 28, 2013 10:38:00 AM
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
 * The Contribute Template.
 * 
 * @version 1.0
 * 
 * @author Mandeep Singh
 */
public class OpenIdeasContributeTemplate extends OpenIdeasMasterTemplate {
    /**
     * Constructor sets the page context.
     *
     * @param pageContext
     *            The page context
     */
    public OpenIdeasContributeTemplate(PageContext pageContext) {
        super(pageContext);

    }

    @Override
    /**
     * Init method sets all forms to be non-cacheable.
     */
    public void init() {
        super.init();
        setResponseNonCacheable();
    }
}