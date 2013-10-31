/**
 * Created: June 10, 2013 8:40:00 PM
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

package com.pearson.openideas.cq5.components.newsletter;

import com.crownpartners.cq.quickstart.core.component.AbstractComponent;
import com.day.cq.tagging.TagManager;
import com.pearson.openideas.cq5.components.beans.Article;
import com.pearson.openideas.cq5.components.services.ArticleQueryService;
import com.pearson.openideas.cq5.components.services.PropertiesService;
import com.pearson.openideas.cq5.components.utils.ArticleUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.PageContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model class for the newsletter new articles list component.
 * 
 * @author Todd M Guerra
 * 
 * @version 2.0
 */
public class NewArticlesList extends AbstractComponent {

    private static final Logger log = LoggerFactory.getLogger(NewArticlesList.class);

    private Map<String, List<Article>> articlesByTheme;

    /**
     * Constructor. Sets the page context
     * 
     * @param pageContext
     *            the page context to set
     */
    public NewArticlesList(PageContext pageContext) {
        super(pageContext);
    }

    /**
     * {@inheritDoc}
     */
    public void init() {

        TagManager tagManager = getResourceResolver().adaptTo(TagManager.class);

        List<String> paths;

        Map<String, String> queryParamMap = new HashMap<String, String>();
        queryParamMap.put("path", "/content/plc/prkc/uk/open-ideas");
        queryParamMap.put("type", "cq:Page");
        queryParamMap.put("1_property", "jcr:content/cq:template");
        queryParamMap.put("1_property.value", "/apps/plc/prkc/uk/open-ideas/templates/openIdeasArticleTemplate");
        queryParamMap.put("2_property", "jcr:content/cq:lastReplicationAction");
        queryParamMap.put("2_property.value", "Activate");
        queryParamMap.put("orderBy", "@jcr:content/jcr:created");
        queryParamMap.put("orderBy.sort", "desc");

        ArticleQueryService queryService = getSlingScriptHelper().getService(ArticleQueryService.class);
        PropertiesService propertiesService = getSlingScriptHelper().getService(PropertiesService.class);

        // get a list of paths returned from our query
        paths = queryService.getArticles(queryParamMap);

        // hash map used to separate articles into different lists by theme
        articlesByTheme = new HashMap<String, List<Article>>();

        // loop through paths, create articles and decide whether or not they should be in our email
        for (String path : paths) {
            Resource resource = getResourceResolver().getResource(path + "/jcr:content");
            Article article = new Article();
            try {
                article = ArticleUtils.createArticleObject(resource, article, tagManager);
            } catch (Exception e) {
                log.error("Error creating this article", e);
            }
            log.debug("Suitable for email? " + article.getSuitableForEmail());

            // add article to hash map, check will be true if it has been published in the last week
            if (article.getSuitableForEmail()) {
                log.debug("Article path: " + article.getUrl());
                article.setUrl(propertiesService.getPublishUrl() + article.getUrl() + ".html");
                log.debug("article full published URL: " + article.getUrl());
                log.debug("image path? " + article.getImage().getFileNodePath());
                String imagePath = article.getThumbnail().getFileNodePath();
                imagePath = StringUtils.replace(imagePath, "image/file", "image.img.jpg");
                imagePath = StringUtils.replace(imagePath, "jcr:content", "_jcr_content");
                imagePath = propertiesService.getPublishUrl() + imagePath;
                log.debug("image path NOW: " + imagePath);
                article.setNewsletterImageUrl(imagePath);
                List<Article> articleList = articlesByTheme.get(article.getTheme());
                if (articleList == null) {
                    articleList = new ArrayList<Article>();
                }
                articleList.add(article);
                articlesByTheme.put(article.getTheme(), articleList);

            }
        }

    }

    /**
     * Sets new articlesByTheme.
     * 
     * @param articlesByTheme
     *            New value of articlesByTheme.
     */
    public void setArticlesByTheme(Map<String, List<Article>> articlesByTheme) {
        this.articlesByTheme = articlesByTheme;
    }

    /**
     * Gets articlesByTheme.
     * 
     * @return Value of articlesByTheme.
     */
    public Map<String, List<Article>> getArticlesByTheme() {
        return articlesByTheme;
    }
}
