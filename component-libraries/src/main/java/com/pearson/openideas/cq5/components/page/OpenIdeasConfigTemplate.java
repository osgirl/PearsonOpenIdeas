/**
 * Created: Apr 11, 2013 3:06:00 PM
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
 * The Master template to help make sure all the templates are consistent.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 */
public class OpenIdeasConfigTemplate extends OpenIdeasMasterTemplate {

    /**
     * Constructor sets the page context.
     * 
     * @param pageContext
     *            The page context
     */
    public OpenIdeasConfigTemplate(final PageContext pageContext) {
        super(pageContext);
    }

    /**
     * init method doesn't do a whole lot right now.
     */
    public void init() {
    }
}
