/**
 * Created: May 16, 2013 11:18:22 AM
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
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.foundation.Image;
import com.pearson.openideas.cq5.components.beans.Article;
import com.pearson.openideas.cq5.components.utils.ArticleUtils;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.PageContext;

/**
 * Model Class for the Homepage Promo component.
 * 
 * @version 2.0
 * 
 * @author Todd M Guerra
 */
public class HomepagePromo extends AbstractComponent {

    // the logger
    private static final Logger log = LoggerFactory.getLogger(HomepagePromo.class);

    private static final String MANUAL_CHOICE = "manual";
    private static final String AUTOMATIC_CHOICE = "articlelink";

    // render variables
    private String promoText;
    private String url;
    private Image image;
    private String theme;
    private String category;
    private String title;
    /**
     * Constructor. Sets the page context.
     * 
     * @param pageContext
     *            the page context to set
     */
    public HomepagePromo(PageContext pageContext) {
        super(pageContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {

        String choice = getProperties().get("promotype", "");

        if (choice.equals(MANUAL_CHOICE)) {
            createManualContent();
        } else if (choice.equals(AUTOMATIC_CHOICE)) {
            createAutomaticContent();
        } else {
            log.warn("There has not been a selection made");
            promoText = "Double click here to get started";
        }
    }

    /**
     * Creates the manually entered content for rendering.
     */
    private void createManualContent() {
        url = getProperties().get("promourl", "");
        if (url.toLowerCase().startsWith("/content")) {
            url += ".html";
        }

        log.debug("promo item url: " + url);
        promoText = getProperties().get("promotext", "");
        image = new Image(getCurrentResource(), "image");
        image.setSelector(".img");
        log.debug("Does the image on the promo box have content? " + image.hasContent());

    }

    /**
     * pulls the automatically generated content based on the path entered.
     */
    private void createAutomaticContent() {
        TagManager tagManager = getResourceResolver().adaptTo(TagManager.class);
        Resource resource;
        url = getProperties().get("articleurl", "");
        log.debug("article link is: " + url);

        if (url.startsWith("/content")) {
            resource = getResourceResolver().getResource(url + "/jcr:content");
            log.debug("Resource name: " + resource.getName());

            url += ".html";

            Article article = new Article();

            article = ArticleUtils.createArticleObject(resource, article, tagManager);
            theme = article.getTheme();
            category = article.getCategory();
            promoText = article.getShortSummary();
            title = article.getArticleTitle();
            image = article.getImage();

        } else {
            promoText = "You selected a bad URL";
        }

    }

    /**
     * Gets promoText.
     * 
     * @return Value of promoText.
     */
    public String getPromoText() {
        return promoText;
    }

    /**
     * Gets category.
     * 
     * @return Value of category.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets new promoText.
     * 
     * @param promoText
     *            New value of promoText.
     */
    public void setPromoText(String promoText) {
        this.promoText = promoText;
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
     * Gets url.
     * 
     * @return Value of url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets theme.
     * 
     * @return Value of theme.
     */
    public String getTheme() {
        return theme;
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
     * Gets image.
     * 
     * @return Value of image.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets new category.
     * 
     * @param category
     *            New value of category.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Sets new theme.
     * 
     * @param theme
     *            New value of theme.
     */
    public void setTheme(String theme) {
        this.theme = theme;
    }
    
    /**
     * Get title.
     * 
     * @return title
     *           
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets new title.
     * 
     * @param ttl
     *            New value of title.
     */
    public void setTitle(String ttle) {
        this.title = ttle;
    }
}
