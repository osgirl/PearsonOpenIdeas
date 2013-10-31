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
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.foundation.Image;
import com.pearson.openideas.cq5.components.enums.DateFormatEnum;
import com.pearson.openideas.cq5.components.enums.NamespaceEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.PageContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Model class for the article body component.
 * 
 * @version 2.0
 * 
 * @author Todd Guerra
 */
public class ArticleBody extends AbstractComponent {

    // the logger
    private static final Logger log = LoggerFactory.getLogger(ArticleBody.class);

    private static final String PROPERTY_NAME_NEWPUBDATE = "newpublishdate";
    private static final String PROPERTY_NAME_ARTICLE_TITLE = "articletitle";
    private static final String PROPERTY_NAME_ARTICLE_TEXT = "text";
    private static final String EDITORS_CHOICE_FLAG = "editors-choice";

    private List<String> organisationTags;

    private String articleTitle;
    private String newPublishDate;
    private String theme;
    private String category;
    private String editorsChoice;
    private String text;
    private Image image;
    private String originalPublishDate;
    private String lastModifiedDate;
    private Boolean useLastModifiedDate;
    private String leadContributor;

    /**
     * Constructor for this class. Sets the page context.
     * 
     * @param pageContext
     *            the page context to set
     */
    public ArticleBody(PageContext pageContext) {
        super(pageContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {

        if (getProperties() == null) {
            log.warn("No Properties");
        } else {
            // set the article title
            setArticleTitle(getProperties().get(PROPERTY_NAME_ARTICLE_TITLE, "Article Title Goes Here"));

            // set the article text
            setText(getProperties().get(PROPERTY_NAME_ARTICLE_TEXT, "Article Text Goes Here"));

            // get the image for rendering
            image = new Image(getCurrentResource(), "image");
            image.setSelector(".img");

            dealWithTags();

            dealWithDates();
        }
    }

    /**
     * Method pulls tags from the page for rendering and collects the tags from the component and sets them to the page.
     */
    private void dealWithTags() {

        // get the tag manager instance for pulling tags
        TagManager tagManager = getResourceResolver().adaptTo(TagManager.class);

        // get the current page resource for several things
        Resource resource = getCurrentPage().getContentResource();

        // first take the tags from the component and set them on the page

        // first update the component tags with keywords and contributors
        // should always run on edit since this is a require field
        if (StringUtils.isNotBlank(getProperties().get("newKeywords", ""))) {
            log.debug("updating tags from outside the component");
            addExtraTags(tagManager, resource, getProperties().get("newKeywords", ""));
        } else {
            log.debug("keywords not entered, probably this is just initial page edit");
        }
        if (isAuthorMode()) {
            // get the tags from the component
            Tag[] tagsFromComponent = tagManager.getTags(getCurrentResource());
            // set them onto the page to keep the lists the same
            tagManager.setTags(getCurrentPage().getContentResource(), tagsFromComponent);

        }

        // get tags for the page
        log.debug("Getting tags for resource: " + resource);
        Tag[] tags = tagManager.getTags(resource);
        log.debug("There are " + tags.length + " tags for this page");

        // let's make a list for organisation tags
        organisationTags = new ArrayList<String>();

        // loop through the tags and check for the namespaces we need
        for (Tag tag : tags) {
            String path = tag.getPath();
            // check for editor's choice
            if (path.contains(EDITORS_CHOICE_FLAG)) {
                setEditorsChoice("true");
            }

            // if we have theme or category tags (which are limited to one),
            // assign it for rendering
            if (path.contains(NamespaceEnum.THEME.getNamespace())) {
                setTheme(tag.getTitle());
                log.debug("Theme has been set to: " + theme);
            } else if (path.contains(NamespaceEnum.CATEGORY.getNamespace())) {
                setCategory(tag.getTitle());
                log.debug("Category has been set to: " + category);
            } else if (path.contains(NamespaceEnum.ORGANISATION.getNamespace())) {
                organisationTags.add(tag.getTitle());
            }
        }

        setOrganisationTags(organisationTags);
    }

    /**
     * Method manages a comma separated list of keywords. Turns them into tags if the tag doesn't exist already and then
     * sets them to the component. Also removes any keywords that are no longer in the list. Sets the component tags
     * from the contributor components as well.
     * 
     * @param tagManager
     *            passed in tag manager so we don't need to create a new one
     * @param newKeywords
     *            String containing a comma separated list of new keywords to create
     * @param resource
     *            the current page resource, used for pulling contributor tags
     */
    private void addExtraTags(TagManager tagManager, Resource resource, String newKeywords) {

        if (isAuthorMode()) {
            // pull the tags on the component
            Tag[] tagsFromComponent = tagManager.getTags(getCurrentResource());
            // turn it into an Array List so we can manipulate it
            List<Tag> oldTags = new ArrayList<Tag>(Arrays.asList(tagsFromComponent));
            // create a new list to put the tags in, minus the keyword tags
            List<Tag> newTags = new ArrayList<Tag>();

            List<String> tagsToCreate = Arrays.asList(newKeywords.split("\\s*,\\s*"));

            // loop through the tags currently on the component
            for (Tag tag : oldTags) {
                // add all but the keyword & contributor tags to the new list
                if (!tag.getTagID().contains(NamespaceEnum.KEYWORD.getNamespace())
                        && !tag.getTagID().contains(NamespaceEnum.CONTRIBUTOR.getNamespace())) {
                    newTags.add(tag);
                }

            }
            // now that we have a tag list sans keywords and contributors, we'll set the new ones

            // first we'll turn the list of keywords into tags and add them to the list
            for (String newTagToCreate : tagsToCreate) {
                log.debug("here's one word: " + newTagToCreate);
                // format the String so it's tagID ready, turning This Name into this-name
                String newTagTitle = newTagToCreate.trim();
                newTagToCreate = newTagToCreate.trim().toLowerCase().replace(" ", "-");

                log.debug("all set for tag creation? " + newTagToCreate);
                String newTagId = NamespaceEnum.KEYWORD.getNamespace() + ":" + newTagToCreate;

                try {
                    Tag newTag = tagManager.createTag(newTagId, newTagTitle, "Auto-Created During Article Creation");
                    log.debug("created tag id is: " + newTag.getTagID());
                    newTags.add(newTag);
                    log.debug("newTags size is now: " + newTags.size());
                } catch (Exception e) {
                    log.error("Error creating tag " + newTagId + ", tag probably already exists", e);
                }

            }

            // second, we'll pull the contributor tags from however many contributor components we have
            Resource leadContribResource = resource.getChild("leadContributor");

            // pull an array of one to get the lead contributor name for rendering
            Tag[] leadContribTags = tagManager.getTags(leadContribResource);
            if (leadContribTags != null && leadContribTags.length > 0) {
                setLeadContributor(leadContribTags[0].getTitle());
                newTags.add(leadContribTags[0]);
            }

            Resource par2 = resource.getChild("par2");
            if (par2 != null) {
                Iterator<Resource> contribResources = par2.listChildren();
                while (contribResources.hasNext()) {
                    Tag[] tags = tagManager.getTags(contribResources.next());
                    if (tags != null && tags.length > 0) {
                        log.debug("Adding this tag to the list: " + tags[0].getName());
                        newTags.add(tags[0]);
                    }
                }
            }

            log.debug("new tags list size: " + newTags.size());
            // turn it back into an Array since CQ only likes Arrays for some reason
            Tag[] newTagsArray = newTags.toArray(new Tag[newTags.size()]);
            // new the new tags to the component
            tagManager.setTags(getCurrentResource(), newTagsArray);
        } else {
            Resource leadContribResource = resource.getChild("leadContributor");
            // pull an array of one to get the lead contributor name for rendering
            Tag[] leadContribTags = tagManager.getTags(leadContribResource);
            if (leadContribTags != null && leadContribTags.length > 0) {
                setLeadContributor(leadContribTags[0].getTitle());
            }
        }
    }

    /**
     * This method compares pub dates with modified date and sets data for rendering.
     */
    private void dealWithDates() {

        // set up the date objects that we will need
        Date newPubDateObject = new Date();
        Date originalPubDateObject;
        Date lastModifiedDateObject = getCurrentPage().getLastModified().getTime();

        // set the last modified date string since we'll need it regardless
        lastModifiedDate = new SimpleDateFormat(DateFormatEnum.CLIENTDATEFORMAT.getDateFormat())
                .format(getCurrentPage().getLastModified().getTime());
        setLastModifiedDate(lastModifiedDate);

        // check for new publish date from user
        if (StringUtils.isNotBlank(getProperties().get(PROPERTY_NAME_NEWPUBDATE, ""))) {

            // If there, set up SDF objects for parsing
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormatEnum.CQDATEFORMAT.getDateFormat());
            try {
                // parse CQ's oddly small date object
                newPubDateObject = sdf.parse(getProperties().get(PROPERTY_NAME_NEWPUBDATE, ""));
            } catch (ParseException e) {
                // oh no, something went wrong!
                log.error("error parsing date", e);
            }
            // convert to the correct date format for rendering on the page
            String newDateString = new SimpleDateFormat(DateFormatEnum.CLIENTDATEFORMAT.getDateFormat())
                    .format(newPubDateObject);

            // finally, set that date and move on!
            setNewPublishDate(newDateString);
        }

        // if the user didn't enter a new pub date
        if (StringUtils.isBlank(newPublishDate)) {
            originalPubDateObject = getCurrentPage().getProperties().get("jcr:created", Date.class);
            // convert to the right format for rendering on the page
            originalPublishDate = new SimpleDateFormat(DateFormatEnum.CLIENTDATEFORMAT.getDateFormat())
                    .format(originalPubDateObject);
            setOriginalPublishDate(originalPublishDate);
            log.debug("orginal publish date being used: " + originalPublishDate);
            // check if modified date is after the publish date and if so, use it
            if (lastModifiedDateObject.after(originalPubDateObject)) {
                setUseLastModifiedDate(true);
                log.debug("boolean is being set to use the modifed date");
            }
        } else {
            if (lastModifiedDateObject.after(newPubDateObject)) {
                setUseLastModifiedDate(true);
                log.debug("boolean is being set to use the modifed date");
            }
        }

    }

    /**
     * gets editor's choice.
     * 
     * @return editorsChoice String to determine whether or not to display the editor's choice box.
     */
    public String getEditorsChoice() {
        return editorsChoice;
    }

    /**
     * sets editor's choice string.
     * 
     * @param editorsChoice
     *            String to show whether we need to show the editor's choice box.
     */
    public void setEditorsChoice(String editorsChoice) {
        this.editorsChoice = editorsChoice;
    }

    /**
     * gets the theme for the article for rendering.
     * 
     * @return theme string containing the theme of the article
     */
    public String getTheme() {
        return theme;
    }

    /**
     * sets the theme for the article.
     * 
     * @param theme
     *            the theme of the article
     */
    public void setTheme(String theme) {
        this.theme = theme;
    }

    /**
     * gets the category of the article.
     * 
     * @return category the category of the article
     */
    public String getCategory() {
        return category;
    }

    /**
     * sets the category.
     * 
     * @param category
     *            the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * gets the new publish date.
     * 
     * @return newPublishDate the new publish date to return
     */
    public String getNewPublishDate() {
        return newPublishDate;
    }

    /**
     * sets thew new publish date.
     * 
     * @param newPublishDate
     *            the new publish date to set
     */
    public void setNewPublishDate(String newPublishDate) {
        this.newPublishDate = newPublishDate;
    }

    /**
     * gets the article title.
     * 
     * @return articleTitle the article title to return
     */
    public String getArticleTitle() {
        return articleTitle;
    }

    /**
     * sets the article title.
     * 
     * @param articleTitle
     *            the article title to set
     */
    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    /**
     * gets the organisation tag list.
     * 
     * @return organisationTags a list of tag title strings to display on the page
     */
    public List<String> getOrganisationTags() {
        return organisationTags;
    }

    /**
     * sets the organisationTags list .
     * 
     * @param organisationTags
     *            the list to set
     */
    public void setOrganisationTags(List<String> organisationTags) {
        this.organisationTags = organisationTags;
    }

    /**
     * gets the text.
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

    /**
     * gets the image.
     * 
     * @return image the image to return
     */
    public Image getImage() {
        return image;
    }

    /**
     * sets the image.
     * 
     * @param image
     *            the image to set
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * gets the original publish date.
     * 
     * @return originalPublishDate actual creation date of the article
     */
    public String getOriginalPublishDate() {
        return originalPublishDate;
    }

    /**
     * sets the original publish date.
     * 
     * @param originalPublishDate
     *            the date string to set
     */
    public void setOriginalPublishDate(String originalPublishDate) {
        this.originalPublishDate = originalPublishDate;
    }

    /**
     * gets the last modified date for display.
     * 
     * @return lastModifiedDate the last modified date to get
     */
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * sets the last modified date string.
     * 
     * @param lastModifiedDate
     *            the last modified date string to set
     */
    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * gets the Boolean that tells the JSP whether or not to display the last modified date.
     * 
     * @return useLastModifiedDate Boolean to tell JSP whether or not to show the last modified date
     */
    public Boolean getUseLastModifiedDate() {
        return useLastModifiedDate;
    }

    /**
     * sets the use last modified date boolean.
     * 
     * @param useLastModifiedDate
     *            the boolean to set
     */
    public void setUseLastModifiedDate(Boolean useLastModifiedDate) {
        this.useLastModifiedDate = useLastModifiedDate;
    }

    /**
     * gets the lead contributor name.
     * 
     * @return leadContributor the string to display for lead contributor
     */
    public String getLeadContributor() {
        return leadContributor;
    }

    /**
     * sets the lead contributor name.
     * 
     * @param leadContributor
     *            the lead contributor name to set
     */
    public void setLeadContributor(String leadContributor) {
        this.leadContributor = leadContributor;
    }
}
