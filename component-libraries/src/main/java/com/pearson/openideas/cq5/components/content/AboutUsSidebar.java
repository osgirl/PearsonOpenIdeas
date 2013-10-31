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

import com.crownpartners.cq.quickstart.core.component.AbstractComponent;
import com.day.cq.wcm.foundation.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.PageContext;

/**
 * Model class for the About Us Sidebar component.
 * 
 * @author Todd M Guerra
 * 
 * @version 2.0
 */
public class AboutUsSidebar extends AbstractComponent {

    // the logger
    private static final Logger log = LoggerFactory.getLogger(AboutUsSidebar.class);

    // render variables
    private String url;
    private String text;
    private Image image;
    private String txt;

    /**
     * Constructor. Sets the page context.
     * 
     * @param pageContext
     *            the page context to set
     */
    public AboutUsSidebar(PageContext pageContext) {
        super(pageContext);
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void init() {
        url = getProperties().get("promourl", "");
        if (url.toLowerCase().startsWith("/content")) {
            url += ".html";
        }

        log.debug("about us promo item url: " + url);
        text = getProperties().get("promotext", "");
        txt = getProperties().get("txt", "");
        image = new Image(getCurrentResource(), "image");
        image.setSelector(".img");

    }

    /**
     * Sets new image.
     * 
     * @param image
     *            New value of image.
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Sets new promoText.
     * 
     * @param promoText
     *            New value of promoText.
     */
    public void setText(String promoText) {
        this.text = promoText;
    }

    /**
     * Sets new url.
     * 
     * @param url
     *            New value of url.
     */
    public void setUrl(String url) {
        this.url = url;
    }
    
    /**
     * Sets new txt.
     * 
     * @param txt
     *            New value of txt.
     */
    public void setTxt(String txt) {
        this.txt = txt;
    }

    /**
     * Gets promoText.
     * 
     * @return Value of promoText.
     */
    public String getText() {
        return text;
    }

    /**
     * Gets image.
     * 
     * @return Value of image.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Gets url.
     * 
     * @return Value of url.
     */
    public String getUrl() {
        return url;
    }
    /**
     * Gets txt.
     * 
     * @return Value of txt.
     */
    public String getTxt() {
        return txt;
    }
}
