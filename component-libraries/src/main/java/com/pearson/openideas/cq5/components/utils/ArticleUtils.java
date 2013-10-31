/**
 * Created: May 15, 2013 4:00:25 PM
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

package com.pearson.openideas.cq5.components.utils;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.foundation.Image;
import com.pearson.openideas.cq5.components.beans.Article;
import com.pearson.openideas.cq5.components.enums.DateFormatEnum;
import com.pearson.openideas.cq5.components.enums.NamespaceEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Utility class for article object creation.
 * 
 * @author Todd M Guerra
 * 
 * @version 2.0
 */
public class ArticleUtils {

    // the logger
    private static final Logger log = LoggerFactory.getLogger(ArticleUtils.class);

    private static final int BEFOREDAYS = 7;

    /**
     * Empty private constructor since this is a utility class.
     */
    private ArticleUtils() {

    }

    /**
     * This method turns a tagged resource into an article object.
     * 
     * @param resource
     *            the resource to extract data from
     * @param article
     *            the article object to add data to
     * @param tagManager
     *            the tag manager instance
     * @param indexing
     *            A boolean to indicate whether to include the indexing fields or not.
     * @return the completed/populated article object
     */
    public static Article createArticleObject(Resource resource, Article article, TagManager tagManager,
            boolean indexing) {

        List<String> countries = new ArrayList<String>();
        List<String> contributors = new ArrayList<String>();
        List<String> organisations = new ArrayList<String>();
        List<String> keywords = new ArrayList<String>();
        List<String> contributorBiogs = new ArrayList<String>();

        log.debug("Are we indexing? " + indexing);

        Calendar weekAgo = Calendar.getInstance();
        weekAgo.set(weekAgo.get(Calendar.YEAR), weekAgo.get(Calendar.MONTH), weekAgo.get(Calendar.DAY_OF_MONTH)
                - BEFOREDAYS);

        String path = resource.getPath();
        log.debug("path to resource: " + path);
        article.setUrl(StringUtils.substring(path, 0, path.lastIndexOf("/")));
        ValueMap articleMap = resource.getChild("articleBody").adaptTo(ValueMap.class);
        article.setShortSummary(articleMap.get("shortsummary", ""));
        article.setArticleTitle(articleMap.get("articletitle", ""));

        //pull the image from the article
        Image image = new Image(resource.getChild("articleBody").getChild("image"));
        image.setSelector(".img");
        article.setImage(image);

        //also, pull the thumbnail image from the article
        if (resource.getChild("articleBody").getChild("imagethumb") != null) {
            Image thumbnail = new Image(resource.getChild("articleBody").getChild("imagethumb"));
            thumbnail.setSelector(".img");
            log.debug("article thumbnail has an image: " + thumbnail.hasContent());
            article.setThumbnail(thumbnail);
        } else {
            log.debug("no thumbnail, falling back to article image");
            article.setThumbnail(image);
        }




        // second, we'll pull the contributor tags from however many contributor components we have
        Resource leadContribResource = resource.getChild("leadContributor");

        // pull an array of one to get the lead contributor name for rendering
        Tag[] leadContribTags = tagManager.getTags(leadContribResource);
        if (leadContribTags != null && leadContribTags.length > 0) {
            article.setAuthor(leadContribTags[0].getTitle());
        } else {
            log.warn("Article " + article.getArticleTitle() + "has no lead contributor set yet.");
        }

        if (StringUtils.isNotBlank(articleMap.get("newpublishdate", ""))) {
            Date newPubDateObject = null;
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormatEnum.CQDATEFORMAT.getDateFormat());

            try {
                newPubDateObject = sdf.parse(articleMap.get("newpublishdate", ""));
            } catch (ParseException e) {
                log.error("Error parsing date", e);
            }

            if (newPubDateObject != null) {
                String newDateString = new SimpleDateFormat(DateFormatEnum.CLIENTDATEFORMAT.getDateFormat())
                        .format(newPubDateObject);
                article.setPubDate(newDateString);
                article.setPubDateObject(newPubDateObject);
                if (newPubDateObject.after(weekAgo.getTime())) {
                    article.setSuitableForEmail(true);
                }

            } else {
                log.warn("new pub date object is null");
            }
        } else {
            log.debug("Trying to parse original date object");
            Date originalPubDateObject = resource.adaptTo(ValueMap.class).get("jcr:created", Date.class);
            log.debug("original pub date: " + originalPubDateObject.toString());
            String originalPublishDateString = new SimpleDateFormat(DateFormatEnum.CLIENTDATEFORMAT.getDateFormat())
                    .format(originalPubDateObject);
            article.setPubDate(originalPublishDateString);
            article.setPubDateObject(originalPubDateObject);
            if (originalPubDateObject.after(weekAgo.getTime())) {
                article.setSuitableForEmail(true);
            }
        }

        Tag[] tags = tagManager.getTags(resource);
        for (Tag tagFromPage : tags) {
            String tagPath = tagFromPage.getPath();

            if (tagPath.contains(NamespaceEnum.THEME.getNamespace())) {
                article.setTheme(tagFromPage.getTitle());
            } else if (tagPath.contains(NamespaceEnum.CATEGORY.getNamespace())) {
                article.setCategory(tagFromPage.getTitle());
            } else if (indexing && tagPath.contains(NamespaceEnum.CONTRIBUTOR.getNamespace())) {
                contributors.add(tagFromPage.getTitle());
                contributorBiogs.add(tagFromPage.getDescription());
            } else if (indexing && tagPath.contains(NamespaceEnum.KEYWORD.getNamespace())) {
                keywords.add(tagFromPage.getTitle());
            } else if (indexing && tagPath.contains(NamespaceEnum.ORGANISATION.getNamespace())) {
                organisations.add(tagFromPage.getTitle());
            } else if (indexing && tagPath.contains(NamespaceEnum.COUNTRY.getNamespace())) {
                countries.add(tagFromPage.getTitle());
            }
        }

        if (indexing) {
            article.setContributors(contributors);
            article.setCountries(countries);
            article.setOrganisations(organisations);
            article.setKeywords(keywords);
            article.setContributorBiogs(contributorBiogs);
            article.setCitation(articleMap.get("citation", ""));
            String articleText = articleMap.get("text", "");

            Resource par = resource.getChild("par");

            if (par != null) {
                Iterator<Resource> parResources = par.listChildren();
                while (parResources.hasNext()) {
                    Resource parResource = parResources.next();
                    ValueMap resourceMap = parResource.adaptTo(ValueMap.class);
                    String type = resourceMap.get("sling:resourceType", "");
                    if (type.endsWith("articleText")) {
                        articleText += resourceMap.get("text", "");
                    }
                }
            }
            article.setArticleText(articleText);
        }
        return article;

    }

    /**
     * This method turns a tagged resource into an article object.
     * 
     * @param resource
     *            the resource to extract data from
     * @param article
     *            the article object to add data to
     * @param tagManager
     *            the tag manager instance
     * @return the completed/populated article object
     */
    public static Article createArticleObject(Resource resource, Article article, TagManager tagManager) {
        return createArticleObject(resource, article, tagManager, false);
    }

}
