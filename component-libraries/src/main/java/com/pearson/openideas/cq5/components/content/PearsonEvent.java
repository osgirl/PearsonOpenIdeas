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
 * Model class for the main about us component.
 * 
 * @author mandeep.singh
 * 
 */
public class PearsonEvent extends AbstractComponent {

    private static final Logger log = LoggerFactory.getLogger(AboutUs.class);

    private static final String DESCRIPTION = "eventDescription";
    private static final String NAME = "eventName";
    private static final String LOCATION = "eventLocation";
    private static final String COUNTRY = "eventCountry";
    private static final String URL = "eventUrl";
    private static final String DATE = "eventDate";
    private static final String URLTEXT = "urlText";

    private Image image;
    private String name;
    private String date;
    private String location;
    private String country;
    private String description;
    private String url;
    private String urlText;

    /**
     * Constructor.
     * 
     * @param pageContext
     *            the pageContext to set.
     */
    public PearsonEvent(PageContext pageContext) {
        super(pageContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        if (getProperties() == null) {
            log.warn("No Properties for Event Component");
        } else {

            // set properties
            setName(getProperties().get(NAME, ""));
            setDescription(getProperties().get(DESCRIPTION, "Double click to add Event Details"));
            setCountry(getProperties().get(COUNTRY, ""));
            setLocation(getProperties().get(LOCATION, ""));
            setUrl(getProperties().get(URL, ""));
            setDate(getProperties().get(DATE, ""));
            setUrlText(getProperties().get(URLTEXT, ""));

            // get the image for rendering
            image = new Image(getCurrentResource(), "image");
            image.setSelector(".img");
            setImage(image);
        }

    }

    /**
     * Gets the name.
     * 
     * @return the event name
     */
    public String getName() {
        return this.name;
    }

    /**
     * sets the name.
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description.
     * 
     * @return the event description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * sets the description.
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the location.
     * 
     * @return the image
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * sets location .
     * 
     * @param location
     *            the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the date.
     * 
     * @return the event date
     */
    public String getDate() {
        return this.date;
    }

    /**
     * sets the date.
     * 
     * @param date
     *            the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the url.
     * 
     * @return the url
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * sets the url.
     * 
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the url text.
     * 
     * @return the url text
     */
    public String getUrlText() {
        return this.urlText;
    }

    /**
     * sets the url text.
     * 
     * @param urlText
     *            the url text to set
     */
    public void setUrlText(String urlText) {
        this.urlText = urlText;
    }

    /**
     * Gets the country.
     * 
     * @return the event country
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * sets the country.
     * 
     * @param country
     *            the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * sets the image.
     * 
     * @param img
     *            the image to set
     */
    public void setImage(Image img) {
        this.image = img;
    }

    /**
     * Gets the image .
     * 
     * @return the image
     */
    public Image getImage() {
        return this.image;
    }

}