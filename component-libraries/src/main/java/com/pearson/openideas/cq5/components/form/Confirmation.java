/**
 * Created: May 23, 2013 3:50:00 PM
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

package com.pearson.openideas.cq5.components.form;

import com.crownpartners.cq.quickstart.core.component.AbstractComponent;

import javax.servlet.jsp.PageContext;

/**
 * Model Class for the Form Confirmation Text component.
 * 
 * @author Todd M Guerra
 * 
 * @version 2.0
 */
public class Confirmation extends AbstractComponent {

    //render variables
    private String title;
    private String text;

    /**
     * Constructor. Sets the page context.
     * 
     * @param pageContext
     *            The page context to set
     */
    public Confirmation(PageContext pageContext) {
        super(pageContext);
    }

    /**
     * {@inheritDoc}
     */
    public void init() {

        title = getProperties().get("title", "Title Will Go Here");
        text = getProperties().get("text", "Confirmation Text Will Go Here");

    }


    /**
     * Sets new text.
     *
     * @param text New value of text.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets title.
     *
     * @return Value of title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets text.
     *
     * @return Value of text.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets new title.
     *
     * @param title New value of title.
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
