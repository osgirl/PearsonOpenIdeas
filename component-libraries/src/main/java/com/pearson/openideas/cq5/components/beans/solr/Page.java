/**
 * Created: Jun 5, 2013 2:15:28 PM
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
package com.pearson.openideas.cq5.components.beans.solr;

import com.day.cq.wcm.foundation.Image;
import org.apache.solr.client.solrj.beans.Field;

import java.util.List;

/**
 * Model for Solr representing the article page.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 */
public class Page {

    @Field("id")
    private String url;
    @Field
    private String title;
    @Field
    private String shortSummary;
    @Field
    private String bodyText;
    @Field
    private String citation;
    @Field
    private List<String> organization;
    @Field
    private List<String> contributor;
    @Field
    private List<String> contributorBiog;
    @Field
    private List<String> keywords;
    @Field
    private String theme;
    @Field
    private String category;
    @Field
    private List<String> country;
    @Field
    private String region;
    @Field
    private String pageType;


    private Image image;
    private String excerpt;
    private boolean isValid;

    /**
     * Get the url.
     * 
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set the url.
     * 
     * @param url
     *            The url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Get the title.
     * 
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title.
     * 
     * @param title
     *            The title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the shortSummary.
     * 
     * @return The shortSummary
     */
    public String getShortSummary() {
        return shortSummary;
    }

    /**
     * Set the shortSummary.
     * 
     * @param shortSummary
     *            The shortSummary to set
     */
    public void setShortSummary(String shortSummary) {
        this.shortSummary = shortSummary;
    }

    /**
     * Get the bodyText.
     * 
     * @return The bodyText
     */
    public String getBodyText() {
        return bodyText;
    }

    /**
     * Set the bodyText.
     * 
     * @param bodyText
     *            The bodyText to set
     */
    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    /**
     * Get the citation.
     * 
     * @return The citation
     */
    public String getCitation() {
        return citation;
    }

    /**
     * Set the citation.
     * 
     * @param citation
     *            The citation to set
     */
    public void setCitation(String citation) {
        this.citation = citation;
    }

    /**
     * Get the organization.
     * 
     * @return The organization
     */
    public List<String> getOrganization() {
        return organization;
    }

    /**
     * Set the organization.
     * 
     * @param organization
     *            The organization to set
     */
    public void setOrganization(List<String> organization) {
        this.organization = organization;
    }

    /**
     * Get the contributor.
     * 
     * @return The contributor
     */
    public List<String> getContributor() {
        return contributor;
    }

    /**
     * Set the contributor.
     * 
     * @param contributor
     *            The contributor to set
     */
    public void setContributor(List<String> contributor) {
        this.contributor = contributor;
    }

    /**
     * Get the contributorBiog.
     * 
     * @return The contributorBiog
     */
    public List<String> getContributorBiog() {
        return contributorBiog;
    }

    /**
     * Set the contributorBiog.
     * 
     * @param contributorBiog
     *            The contributorBiog to set
     */
    public void setContributorBiog(List<String> contributorBiog) {
        this.contributorBiog = contributorBiog;
    }

    /**
     * Get the keywords.
     * 
     * @return The keywords
     */
    public List<String> getKeywords() {
        return keywords;
    }

    /**
     * Set the keywords.
     * 
     * @param keywords
     *            The keywords to set
     */
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * Get the theme.
     * 
     * @return The theme
     */
    public String getTheme() {
        return theme;
    }

    /**
     * Set the theme.
     * 
     * @param theme
     *            The theme to set
     */
    public void setTheme(String theme) {
        this.theme = theme;
    }

    /**
     * Get the category.
     * 
     * @return The category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set the category.
     * 
     * @param category
     *            The category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get the country.
     * 
     * @return The country
     */
    public List<String> getCountry() {
        return country;
    }

    /**
     * Set the country.
     * 
     * @param country
     *            The country to set
     */
    public void setCountry(List<String> country) {
        this.country = country;
    }

    /**
     * Get the region.
     * 
     * @return The region
     */
    public String getRegion() {
        return region;
    }

    /**
     * Set the region.
     * 
     * @param region
     *            The region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Get the pageType.
     * 
     * @return The pageType
     */
    public String getPageType() {
        return pageType;
    }

    /**
     * Set the pageType.
     * 
     * @param pageType
     *            The pageType to set
     */
    public void setPageType(String pageType) {
        this.pageType = pageType;
    }


    /**
     * Sets new image.
     *
     * @param image New value of image.
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
     * Gets excerpt.
     *
     * @return Value of excerpt.
     */
    public String getExcerpt() {
        return excerpt;
    }

    /**
     * Sets new excerpt.
     *
     * @param excerpt New value of excerpt.
     */
    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }


    /**
     * Gets isValid.
     *
     * @return Value of isValid.
     */
    public boolean isIsValid() {
        return isValid;
    }

    /**
     * Sets new isValid.
     *
     * @param isValid New value of isValid.
     */
    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }
}
