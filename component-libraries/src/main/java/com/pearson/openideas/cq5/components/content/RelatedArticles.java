/**
 * Created: Apr 29, 2013 1:15:00 PM
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crownpartners.cq.quickstart.core.component.AbstractComponent;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.pearson.openideas.cq5.components.beans.Article;
import com.pearson.openideas.cq5.components.enums.NamespaceEnum;
import com.pearson.openideas.cq5.components.utils.ArticleUtils;
import com.pearson.openideas.cq5.components.utils.DateUtils;

/**
 * The related article class displays articles in the sidebar that have the same theme tag as the tag of the current
 * page.
 * 
 * @author mandeep.singh
 * 
 */
public class RelatedArticles extends AbstractComponent {

    private static final Logger log = LoggerFactory.getLogger(RelatedArticles.class);

    private TagManager tagManager;
    private List<Article> listOfArticles;
    private Iterator<Resource> themeResources = null;

    /**
     * Constructor.
     * 
     * @param pageContext
     *            The page context
     */
    public RelatedArticles(PageContext pageContext) {
        super(pageContext);
    }

    /**
     * The init method gets all resources that have the same theme tag as the current resource.
     */
    @Override
    public void init() {
        String tagID;
        tagManager = getResourceResolver().adaptTo(TagManager.class);
        Tag[] tags = tagManager.getTags(getCurrentPage().getContentResource());

        if (tags.length > 0) {
            log.debug("Number of tags found for Related Articles: " + tags.length);

            for (Tag tag : tags) {
                tagID = tag.getTagID();
                if (tagID.contains(NamespaceEnum.THEME.getNamespace())) {
                    themeResources = tag.find();
                }
            }
        }

        if (themeResources != null) {
            populateListOfArticles();
            DateUtils.sortByMostRecentArticles(listOfArticles);
        }

    }

    /**
     * This method goes through the list of theme resources found in the init function and creates a list of article
     * objects.
     */
    public final void populateListOfArticles() {
        Article article;
        listOfArticles = new ArrayList<Article>();
        String path;
        Resource resource;
        while (themeResources.hasNext()) {
            resource = themeResources.next();
            path = resource.getPath();
            String articleUrl = StringUtils.substring(path, 0, path.lastIndexOf("/"));
            article = new Article();
            article.setUrl(articleUrl);
            if (!(getCurrentPage().getContentResource().getPath().equals(resource.getPath()))) {

                article = ArticleUtils.createArticleObject(resource, article, tagManager);

                listOfArticles.add(article);
            }
        }

    }

    /**
     * Returns the list of articles.
     * 
     * @return listOfArticles
     */
    public final List<Article> getListOfArticles() {
        return this.listOfArticles;
    }

}
