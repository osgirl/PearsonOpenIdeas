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

import com.crownpartners.cq.quickstart.core.component.AbstractComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.PageContext;

/**
 * Model class for hte Article Text Component.
 * 
 * @version 1.0
 * 
 * @author Todd M Guerra
 */
public class ArticleText extends AbstractComponent {

    // the logger
    private static final Logger log = LoggerFactory.getLogger(ArticleText.class);

    private String text;

    /**
     * Constructor, sets the page context.
     * 
     * @param pageContext
     *            the page context to set
     */
    public ArticleText(PageContext pageContext) {
        super(pageContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        setText(getProperties().get("text", "Double Click Here to Add Text"));
        log.debug("text is set for the article text component.");
    }

    /**
     * gets the article text.
     * 
     * @return text the text to return
     */
    public String getText() {
        return text;
    }

    /**
     * sets the text.
     * 
     * @param text
     *            the text to set
     */
    public void setText(String text) {
        this.text = text;
    }
}
