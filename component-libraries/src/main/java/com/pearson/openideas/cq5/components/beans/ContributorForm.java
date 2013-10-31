/**
 * Created: June 03, 2013 8:40:00 PM
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
 * The contributor form model.
 * 
 * @version 2.0
 * 
 * @author Mandeep Singh
 */
public class ContributorForm {

    private String name;
    private String email;
    private String comments;
    private String select;

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
     * Get the email.
     * 
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email.
     * 
     * @param email
     *            The email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the comments.
     * 
     * @return The comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * Set the comments.
     * 
     * @param comments
     *            The comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Get the select.
     * 
     * @return The select
     */
    public String getSelect() {
        return select;
    }

    /**
     * Set the select.
     * 
     * @param select
     *            The select to set
     */
    public void setSelect(String select) {
        this.select = select;
    }
}