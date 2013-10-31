/**
 * Created: Apr 23, 2013 1:40:33 PM
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

import com.crownpartners.cq.quickstart.core.component.AbstractComponent;
import com.day.cq.wcm.foundation.Image;

/**
 * Model class for Article download component.
 * 
 * @version 2.0
 * 
 * @author Mandeep Singh
 */
public class ArticleDownloadComponent extends AbstractComponent {

    private Image image;
    private boolean printImage;

    /**
     * Constructor for this class. Sets the page context.
     * 
     * @param pageContext
     *            the page context to set
     */
    public ArticleDownloadComponent(PageContext pageContext) {
        super(pageContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        // get the image for rendering
        image = new Image(getCurrentResource(), "image");
        image.setSelector(".img");
        if (image.getFileName().length() > 0) {
            printImage = true;
        }
    }

    /**
     * gets the image.
     * 
     * @return image the image to return
     */
    public Image getImage() {
        return image;
    }

    /**
     * sets the image.
     * 
     * @param image
     *            the image to set
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Get the printImage.
     * 
     * @return The printImage
     */
    public boolean isPrintImage() {
        return printImage;
    }

    /**
     * Set the printImage.
     * 
     * @param printImage
     *            The printImage to set
     */
    public void setPrintImage(boolean printImage) {
        this.printImage = printImage;
    }

}