/**
 * Created: Apr 22, 2013 4:15:00 PM
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

import java.util.Iterator;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crownpartners.cq.quickstart.core.component.AbstractComponent;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

/**
 * The article contributor information component.
 * 
 * @version 2.0
 * 
 * @author Todd Guerra
 */
public class ArticleContributorInfo extends AbstractComponent {

    // the logger
    private static final Logger log = LoggerFactory.getLogger(ArticleContributorInfo.class);

    private static final String PROPERTY_CONTRIBUTOR_CHOICE = "leadContributorChoice";
    private static final String PROPERTY_CONTRIBUTOR_URL = "contributorUrl";

    private String leadContributor;
    private String contributorBio;
    private String contributorUrl;
    private String contributorName;
    private String imageUrl;
    private Boolean renderForPublish;

    /**
     * Constructor.
     * 
     * @param pageContext
     *            The page context
     */
    public ArticleContributorInfo(PageContext pageContext) {
        super(pageContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {

        if (getProperties() == null) {
            log.warn("No Properties");
            renderForPublish = false;
        } else {
            setLeadContributor(getProperties().get(PROPERTY_CONTRIBUTOR_CHOICE, ""));
            setContributorUrl(getProperties().get(PROPERTY_CONTRIBUTOR_URL, ""));

            TagManager tagManager = getResourceResolver().adaptTo(TagManager.class);
            Tag[] tagsFromComponent = tagManager.getTags(getCurrentResource());
            if (tagsFromComponent != null && tagsFromComponent.length > 0) {
                String tagID = tagsFromComponent[0].getTagID();

                Tag tag = tagManager.resolve(tagID);
                if (tag != null) {
                    Iterator<Resource> resourceIterator = tag.find();
                    int count = 1;
                    while (resourceIterator.hasNext()) {
                        String path = resourceIterator.next().getPath();
                        log.debug("item #" + count + ": " + path);
                        if (path.contains("dam")) {
                            imageUrl = StringUtils.substring(path, 0, path.lastIndexOf("/jcr"));
                            log.debug("image url: " + imageUrl);
                            setImageUrl(imageUrl);
                        }
                    }

                    setContributorBio(tag.getDescription());
                    setContributorName(tag.getTitle());
                } else {
                    log.warn("Could not resolve the tag id: " + tagID);
                }
            } else {
                log.info("No tags in contributor component");
            }

            if (StringUtils.isNotBlank(contributorBio)) {
                renderForPublish = true;
            }
        }
    }

    /**
     * Get the leadContributor.
     * 
     * @return The leadContributor
     */
    public String getLeadContributor() {
        return leadContributor;
    }

    /**
     * Set the leadContributor.
     * 
     * @param leadContributor
     *            The leadContributor to set
     */
    public void setLeadContributor(String leadContributor) {
        this.leadContributor = leadContributor;
    }

    /**
     * Get the contributorBio.
     * 
     * @return The contributorBio
     */
    public String getContributorBio() {
        return contributorBio;
    }

    /**
     * Set the contributorBio.
     * 
     * @param contributorBio
     *            The contributorBio to set
     */
    public void setContributorBio(String contributorBio) {
        this.contributorBio = contributorBio;
    }

    /**
     * Get the contributorUrl.
     * 
     * @return The contributorUrl
     */
    public String getContributorUrl() {
        return contributorUrl;
    }

    /**
     * Set the contributorUrl.
     * 
     * @param contributorUrl
     *            The contributorUrl to set
     */
    public void setContributorUrl(String contributorUrl) {
        this.contributorUrl = contributorUrl;
    }

    /**
     * Get the contributorName.
     * 
     * @return The contributorName
     */
    public String getContributorName() {
        return contributorName;
    }

    /**
     * Set the contributorName.
     * 
     * @param contributorName
     *            The contributorName to set
     */
    public void setContributorName(String contributorName) {
        this.contributorName = contributorName;
    }

    /**
     * gets the image url.
     * 
     * @return The image url string
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * sets the image url.
     * 
     * @param imageUrl
     *            the image url to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Gets renderForPublish.
     * 
     * @return Value of renderForPublish.
     */
    public Boolean getRenderForPublish() {
        return renderForPublish;
    }

    /**
     * Sets new renderForPublish.
     * 
     * @param renderForPublish
     *            New value of renderForPublish.
     */
    public void setRenderForPublish(Boolean renderForPublish) {
        this.renderForPublish = renderForPublish;
    }
}
