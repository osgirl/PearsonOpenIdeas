/**
 * Created: May 9, 2013 1:03:00 PM
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
 * The Static Page Template.
 * 
 * @version 2.0
 * 
 * @author Mandeep Singh
 */

public class OpenIdeasStaticPageTemplate extends OpenIdeasMasterTemplate {

    /**
     * Constructor sets the page context.
     * 
     * @param pageContext
     *            The page context
     */
    public OpenIdeasStaticPageTemplate(PageContext pageContext) {
        super(pageContext);
    }

}