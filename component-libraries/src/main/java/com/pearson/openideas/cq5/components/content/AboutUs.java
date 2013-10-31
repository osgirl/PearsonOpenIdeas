/**
 * Created: May 20, 2013 11:02:11 AM
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

import javax.servlet.jsp.PageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crownpartners.cq.quickstart.core.component.AbstractComponent;
import com.day.cq.wcm.foundation.Image;

/**
 * Model class for the main about us component.
 * 
 * @author mandeep.singh
 * 
 */

public class AboutUs extends AbstractComponent {
    private static final Logger log = LoggerFactory.getLogger(AboutUs.class);
    private static final String PROPERTY_NAME_ABOUT_TEXT = "text";
    private Image image;
    private String text;

    /**
     * Constructor.
     * 
     * @param pageContext
     *            the pageContext to set.
     */
    public AboutUs(PageContext pageContext) {
        super(pageContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        if (getProperties() == null) {
            log.warn("No Properties for About Us Component");
        } else {

            // set the about text
            setText(getProperties().get(PROPERTY_NAME_ABOUT_TEXT, "About Text Goes Here"));
            // get the image for rendering
            image = new Image(getCurrentResource(), "image");
            image.setSelector(".img");
            setImage(image);
        }

    }

    /**
     * Sets the about us text.
     * 
     * @param text
     *            the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * gets the text.
     * 
     * @return the about us text
     */
    public String getText() {
        return this.text;
    }

    /**
     * sets the about us image.
     * 
     * @param img
     *            the image to set
     */
    public void setImage(Image img) {
        this.image = img;
    }

    /**
     * Gets the about us image .
     * 
     * @return the about us image
     */
    public Image getImage() {
        return this.image;
    }
}