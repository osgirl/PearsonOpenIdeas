/**
 * Created: May 15, 2013 1:40:33 PM
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
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.foundation.Image;
import com.pearson.openideas.cq5.components.beans.Article;
import com.pearson.openideas.cq5.components.enums.NamespaceEnum;
import com.pearson.openideas.cq5.components.utils.ArticleUtils;
import com.pearson.openideas.cq5.components.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.PageContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Model Class for the Editor's Choice component.
 * 
 * @version 2.0
 * 
 * @author Todd M Guerra
 */
public class EditorsChoice extends AbstractComponent {

    // the logger
    private static final Logger log = LoggerFactory.getLogger(EditorsChoice.class);

    // rendering variables
    private String theme;
    private String category;
    private Image image;
    private String summary;
    private String url;
    private String title;

    // article variable
    private Article article;

    /**
     * Constructor for this class. Sets the page context.
     * 
     * @param pageContext
     *            the page context to set
     */
    public EditorsChoice(PageContext pageContext) {
        super(pageContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {

        if (StringUtils.isNotBlank(getProperties().get("articleurl", ""))) {
            findManualArticle();
        } else {
            findEditorsChoiceArticle();
        }

        if (article != null) {
            theme = article.getTheme();
            log.debug("do we have a theme here? " + theme);
            category = article.getCategory();
            image = article.getImage();
            summary = article.getShortSummary();
            title = article.getArticleTitle();
            url = article.getUrl();
            log.debug("url for this article is: " + url);
        }

    }

    /**
     * Gets the list of editor's choice articles and returns the newest one.
     * 
     */
    private void findEditorsChoiceArticle() {

        log.debug("using automatic editor's choice article");
        TagManager tagManager = getResourceResolver().adaptTo(TagManager.class);
        List<Article> editorsArticles = new ArrayList<Article>();
        Iterator<Resource> editorResources;
        Resource resource;

        Tag editorsChoiceTag = tagManager.resolve(NamespaceEnum.HOMEPAGE.getNamespaceTagId() + "editors-choice");

        editorResources = editorsChoiceTag.find();

        while (editorResources.hasNext()) {
            resource = editorResources.next();
            article = new Article();

            article = ArticleUtils.createArticleObject(resource, article, tagManager);

            editorsArticles.add(article);
        }

        log.debug("size of editors articles: " + editorsArticles.size());
        if (editorsArticles.size() > 0) {
            editorsArticles = DateUtils.sortByMostRecentArticles(editorsArticles);
        }

        article = editorsArticles.get(0);
    }

    private void findManualArticle() {

        log.debug("using manually chosen editor's choice article");
        TagManager tagManager = getResourceResolver().adaptTo(TagManager.class);
        Resource resource;
        url = getProperties().get("articleurl", "");
        log.debug("manual article link is: " + url);

        if (url.startsWith("/content")) {
            resource = getResourceResolver().getResource(url + "/jcr:content");

            article = new Article();

            article = ArticleUtils.createArticleObject(resource, article, tagManager);
        }
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
     * Gets image.
     * 
     * @return Value of image.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Sets new summary.
     * 
     * @param summary
     *            New value of summary.
     */
    public void setSummary(String summary) {
        this.summary = summary;
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
     * Gets summary.
     * 
     * @return Value of summary.
     */
    public String getSummary() {
        return summary;
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
     * Gets theme.
     * 
     * @return Value of theme.
     */
    public String getTheme() {
        return theme;
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
     * Sets new url.
     * 
     * @param url
     *            New value of url.
     */
    public void setUrl(String url) {
        this.url = url;
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
     * Sets new title.
     * 
     * @param articleTitle
     *            New value of title.
     */
    public void setTitle(String articleTitle) {
        this.title = articleTitle;
    }
}
