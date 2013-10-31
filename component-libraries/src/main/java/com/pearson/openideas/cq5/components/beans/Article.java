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
package com.pearson.openideas.cq5.components.beans;

import com.day.cq.wcm.foundation.Image;

import java.util.List;
import java.util.Date;

/**
 * A model to represent the article.
 * 
 * @version 2.0
 * 
 * @author Todd Guerra
 */
public class Article {

    // the logger
    // private static final Logger log = LoggerFactory.getLogger(Article.class);

    private Image image;
    private Image thumbnail;
    private String articleTitle;
    private String shortSummary;
    private String author;
    private String pubDate;
    private String url;
    private String theme;
    private String category;
    private String country;
    private String region;
    private boolean suitableForEmail;
    private List<String> countries;
    private List<String> contributors;
    private List<String> keywords;
    private List<String> organisations;
    private List<String> contributorBiogs;
    private String articleText;
    private String citation;
    private String newsletterImageUrl;
    private String newsletterArticleUrl;
    private Date pubDateObject;

    /**
     * Get the image.
     * 
     * @return The image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Set the image.
     * 
     * @param image
     *            The image to set
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Get the articleTitle.
     * 
     * @return The articleTitle
     */
    public String getArticleTitle() {
        return articleTitle;
    }

    /**
     * Set the articleTitle.
     * 
     * @param articleTitle
     *            The articleTitle to set
     */
    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
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
     * Get the author.
     * 
     * @return The author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set the author.
     * 
     * @param author
     *            The author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Get the pubDate.
     * 
     * @return The pubDate
     */
    public String getPubDate() {
        return pubDate;
    }

    /**
     * Set the pubDate.
     * 
     * @param pubDate
     *            The pubDate to set
     */
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

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
     * Gets region.
     * 
     * @return Value of region.
     */
    public String getRegion() {
        return region;
    }

    /**
     * Gets country.
     * 
     * @return Value of country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets new region.
     * 
     * @param region
     *            New value of region.
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Sets new country.
     * 
     * @param country
     *            New value of country.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets suitableForEmail.
     * 
     * @return Value of suitableForEmail.
     */
    public boolean getSuitableForEmail() {
        return suitableForEmail;
    }

    /**
     * Sets new suitableForEmail.
     * 
     * @param suitableForEmail
     *            New value of suitableForEmail.
     */
    public void setSuitableForEmail(boolean suitableForEmail) {
        this.suitableForEmail = suitableForEmail;
    }

    /**
     * Gets countries.
     * 
     * @return Value of countries.
     */
    public List<String> getCountries() {
        return countries;
    }

    /**
     * Sets new contributers.
     * 
     * @param contributors
     *            New value of contributors.
     */
    public void setContributors(List<String> contributors) {
        this.contributors = contributors;
    }

    /**
     * Gets contributers.
     * 
     * @return Value of contributers.
     */
    public List<String> getContributors() {
        return contributors;
    }

    /**
     * Sets new countries.
     * 
     * @param countries
     *            New value of countries.
     */
    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    /**
     * Gets keywords.
     * 
     * @return Value of keywords.
     */
    public List<String> getKeywords() {
        return keywords;
    }

    /**
     * Sets new organisations.
     * 
     * @param organisations
     *            New value of organisations.
     */
    public void setOrganisations(List<String> organisations) {
        this.organisations = organisations;
    }

    /**
     * Gets organisations.
     * 
     * @return Value of organisations.
     */
    public List<String> getOrganisations() {
        return organisations;
    }

    /**
     * Sets new keywords.
     * 
     * @param keywords
     *            New value of keywords.
     */
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * Gets contributorBiogs.
     * 
     * @return Value of contributorBiogs.
     */
    public List<String> getContributorBiogs() {
        return contributorBiogs;
    }

    /**
     * Sets new contributorBiogs.
     * 
     * @param contributorBiogs
     *            New value of contributorBiogs.
     */
    public void setContributorBiogs(List<String> contributorBiogs) {
        this.contributorBiogs = contributorBiogs;
    }

    /**
     * Gets articleText.
     * 
     * @return Value of articleText.
     */
    public String getArticleText() {
        return articleText;
    }

    /**
     * Gets citation.
     * 
     * @return Value of citation.
     */
    public String getCitation() {
        return citation;
    }

    /**
     * Sets new articleText.
     * 
     * @param articleText
     *            New value of articleText.
     */
    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    /**
     * Sets new citation.
     * 
     * @param citation
     *            New value of citation.
     */
    public void setCitation(String citation) {
        this.citation = citation;
    }


    /**
     * Sets new newsletterImageUrl.
     *
     * @param newsletterImageUrl New value of newsletterImageUrl.
     */
    public void setNewsletterImageUrl(String newsletterImageUrl) {
        this.newsletterImageUrl = newsletterImageUrl;
    }

    /**
     * Sets new newsletterArticleUrl.
     *
     * @param newsletterArticleUrl New value of newsletterArticleUrl.
     */
    public void setNewsletterArticleUrl(String newsletterArticleUrl) {
        this.newsletterArticleUrl = newsletterArticleUrl;
    }

    /**
     * Gets newsletterArticleUrl.
     *
     * @return Value of newsletterArticleUrl.
     */
    public String getNewsletterArticleUrl() {
        return newsletterArticleUrl;
    }

    /**
     * Gets newsletterImageUrl.
     *
     * @return Value of newsletterImageUrl.
     */
    public String getNewsletterImageUrl() {
        return newsletterImageUrl;
    }


    /**
     * Sets new thumbnail.
     *
     * @param thumbnail New value of thumbnail.
     */
    public void setThumbnail(Image thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * Gets thumbnail.
     *
     * @return Value of thumbnail.
     */
    public Image getThumbnail() {
        return thumbnail;
    }

    /**
     * Sets new pubDateObject.
     *
     * @param pubDateObject New value of pubDateObject.
     */
    public void setPubDateObject(Date pubDateObject) {
        this.pubDateObject = pubDateObject;
    }

    /**
     * Gets pubDateObject.
     *
     * @return Value of pubDateObject.
     */
    public Date getPubDateObject() {
        return pubDateObject;
    }
}
