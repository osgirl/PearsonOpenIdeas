/**
 * Created: May 1, 2013 4:30:00 PM
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
package com.pearson.openideas.cq5.components.beans;

/**
 * A model to represent the navigation tag.
 * 
 * @version 2.0
 * 
 * @author Mandeep Singh
 */
public class NavigationTagItem {

    private String name;
    private String title;

    /**
     * Get the name.
     * 
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name.
     * 
     * @param name
     *            The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the title.
     * 
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title.
     * 
     * @param title
     *            The title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
