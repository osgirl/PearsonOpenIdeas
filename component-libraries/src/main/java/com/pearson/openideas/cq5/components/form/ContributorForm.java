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

package com.pearson.openideas.cq5.components.form;

import javax.servlet.jsp.PageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crownpartners.cq.quickstart.core.component.AbstractComponent;

/**
 * Model class for contributor form.
 * 
 * @author Mandeep Singh
 * 
 * @version 2.0
 */
public class ContributorForm extends AbstractComponent {

    private static final Logger log = LoggerFactory.getLogger(ContributorForm.class);
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String COMMENTS = "comments";
    private static final String SUBJECT = "subject";
    private String name;
    private String email;
    private String comments;
    private String select;

    /**
     * Constructor. Sets the page context.
     * 
     * @param pageContext
     *            The page context to set
     */
    public ContributorForm(PageContext pageContext) {
        super(pageContext);
    }

    /**
     * {@inheritDoc}
     */
    public void init() {
        if (getProperties() == null) {
            log.warn("No Properties for Contribute/Contact Us Page");
        } else {
            setName(getProperties().get(NAME, "Name"));
            setEmail(getProperties().get(EMAIL, "Email"));
            setComments(getProperties().get(COMMENTS, "Comments"));
            setSelect(getProperties().get(SUBJECT, "Subject"));
        }
    }

    /**
     * Sets new name.
     * 
     * @param name
     *            New value of name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets name.
     * 
     * @return Value of name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets email.
     * 
     * @return Value of email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets new email.
     * 
     * @param email
     *            New value of email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets comments.
     * 
     * @return Value of comments.
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets new comments.
     * 
     * @param comments
     *            New value of comments.
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Gets select.
     * 
     * @return Value of select.
     */
    public String getSelect() {
        return select;
    }

    /**
     * Sets new select.
     * 
     * @param select
     *            New value of select.
     */
    public void setSelect(String select) {
        this.select = select;
    }
}