/**
 * Created: Apr 24, 2013 1:27:00 PM
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
package com.pearson.openideas.cq5.components.filters;

import com.crownpartners.cq.quickstart.core.component.AbstractComponent;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.pearson.openideas.cq5.components.beans.Article;
import com.pearson.openideas.cq5.components.compare.TagExploreComparator;
import com.pearson.openideas.cq5.components.compare.TagTitleComparator;
import com.pearson.openideas.cq5.components.enums.NamespaceEnum;
import com.pearson.openideas.cq5.components.utils.ArticleUtils;
import com.pearson.openideas.cq5.components.utils.DateUtils;
import com.pearson.openideas.cq5.components.utils.ExploreUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.PageContext;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * model class for the Landing Page List component.
 *
 * @version 2.0
 *
 * @author Todd Guerra
 */
public class LandingPageList extends AbstractComponent {

    private static final String DEFAULTFILTER = "All";
    private static final int PAGESIZE = 10;

    private List<Article> articles;
    private String selector;
    private String landingTitle;

    // filter menu lists
    private Map<Tag, Long> categoryTags;
    private Map<Tag, Long> themeTags;
    private List<Tag> regionTagList;
    private List<Tag> countryTagList;

    // filter list booleans
    private Boolean useThemeFilter;
    private Boolean useRegionFilter;

    // filter items
    private String namespace;
    private String selectedThemeFilter;
    private String selectedCategoryFilter;
    private String selectedRegionFilter;
    private String selectedCountryFilter;

    private long totalArticles;
    private int pageNumber;
    private StringBuilder params;

    private ExploreUtils exploreUtils;

    private Map<String, List<String>> relationshipMap;

    private String relationshipJSON;

    // the logger
    private static final Logger log = LoggerFactory.getLogger(LandingPageList.class);

    /**
     * Constructor.
     *
     * @param pageContext
     *            The page context
     */
    public LandingPageList(PageContext pageContext) {
        super(pageContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {

        if (getProperties() == null) {
            log.warn("No Properties");
            landingTitle = "Double Click Here to Set Landing Page Type";
        } else {

            relationshipMap = new HashMap<String, List<String>>();

            String tagId = "";

            namespace = getProperties().get("landingPageType", "");
            log.debug("type of landing page " + namespace);

            // set filter drop down boolean
            if (namespace.contains(NamespaceEnum.THEME.getNamespace())) {
                setUseThemeFilter(true);
            } else if (namespace.contains(NamespaceEnum.REGION.getNamespace())) {
                setUseRegionFilter(true);
            }

            // pull the selector from the URL
            if (getSlingRequest().getRequestPathInfo().getSelectors() != null
                    && getSlingRequest().getRequestPathInfo().getSelectors().length > 0) {
                selector = getSlingRequest().getRequestPathInfo().getSelectors()[0];
                tagId = namespace + ":" + selector;
                log.debug("Tag ID is: " + tagId);
            } else {
                log.error("no selector present");
            }

            exploreUtils = new ExploreUtils(getPageProperties(), getResourceResolver().adaptTo(TagManager.class));

            articles = new ArrayList<Article>();

            articles = findArticles(tagId);

            Collections.sort(regionTagList, new TagExploreComparator(exploreUtils.getRegionTags()));
            Collections.sort(countryTagList, new TagTitleComparator());
        }

    }

    /**
     * Method decides if we should be using the theme filter.
     *
     * @return hasFilter the boolean to return
     */
    private boolean hasFilter() {
        boolean hasFilter = false;

        selectedThemeFilter = getRequestParameter("theme");
        selectedCategoryFilter = getRequestParameter("category");
        selectedRegionFilter = getRequestParameter("region");
        selectedCountryFilter = getRequestParameter("country");

        // test for country parameter, it's the only one that will ALWAYS be there
        if (StringUtils.isNotBlank(selectedCountryFilter)) {
            hasFilter = true;
            log.debug("we'll be filtering this list now, by: " + selectedThemeFilter + "," + selectedCategoryFilter
                    + "," + selectedRegionFilter + "," + selectedCountryFilter);
        }

        return hasFilter;
    }

    /**
     * Check if the article should be included or not.
     *
     * @param article
     *            The article to check
     * @param skipTheme
     *            True if we should skip the theme filter, false otherwise.
     * @param skipCategory
     *            True if we should category the theme filter, false otherwise.
     * @param skipRegion
     *            True if we should skip the region filter, false otherwise.
     * @param skipCountry
     *            True if we should skip the country filter, false otherwise.
     * @return A list of filtered articles
     */
    private boolean includeArticle(Article article, boolean skipTheme, boolean skipCategory, boolean skipRegion,
                                   boolean skipCountry) {

        log.debug("\n country for this article is: " + article.getCountry());
        if ((skipTheme || selectedThemeFilter.equals(article.getTheme()))
                && (skipCategory || selectedCategoryFilter.equals(article.getCategory()))
                && (skipRegion || selectedRegionFilter.equals(article.getRegion()))
                && (skipCountry || selectedCountryFilter.equals(article.getCountry()))) {
            return true;
        }
        return false;
    }

    /**
     * Method finds the initial list of articles.
     *
     * @param tagId
     *            tag ID that we'll be searching for
     *
     * @return articles the list of articles to return
     */
    private List<Article> findArticles(String tagId) {

        // get the tag manager instance for pulling tags
        TagManager tagManager = getResourceResolver().adaptTo(TagManager.class);

        // resolve a tag
        Tag tag = tagManager.resolve(tagId);
        if (tag != null) {

            setLandingTitle(tag.getTitle());
            Iterator<Resource> resourceIterator = tag.find();

            categoryTags = new TreeMap<Tag, Long>(new TagExploreComparator(exploreUtils.getCategoryTags()));
            themeTags = new TreeMap<Tag, Long>(new TagExploreComparator(exploreUtils.getThemeTags()));
            regionTagList = new ArrayList<Tag>();
            countryTagList = new ArrayList<Tag>();

            // calculate page information
            String page = getRequestParameter("page");
            pageNumber = 1;
            if (page != null) {
                try {
                    pageNumber = Integer.parseInt(page);
                } catch (NumberFormatException ex) {
                    log.error("The page number is not a valid number: " + pageNumber, ex);
                }
            }
            log.debug("The page number is: " + pageNumber);
            int start = (PAGESIZE * (pageNumber - 1));
            int end = PAGESIZE * pageNumber;

            // Prepare filters
            boolean noFilters = !hasFilter();
            boolean skipTheme = skipFilter(selectedThemeFilter);
            boolean skipCategory = skipFilter(selectedCategoryFilter);
            boolean skipRegion = skipFilter(selectedRegionFilter);
            boolean skipCountry = skipFilter(selectedCountryFilter);

            log.debug("The value for noFilter is: " + noFilters);
            totalArticles = 0;
            List<Article> articlesBeforeOrdering = new ArrayList<Article>();
            Article article;
            while (resourceIterator.hasNext()) {

                article = new Article();

                Resource resource = resourceIterator.next();

                article = ArticleUtils.createArticleObject(resource, article, tagManager);

                buildFilterLists(resource, tagManager, article);

                articlesBeforeOrdering.add(article);

            }

            DateUtils.sortByMostRecentArticles(articlesBeforeOrdering);

            //looping through this twice is not something I'm proud of
            for (Article orderedArticle : articlesBeforeOrdering) {
                if (noFilters || includeArticle(orderedArticle, skipTheme, skipCategory, skipRegion, skipCountry)) {
                    if (totalArticles >= start && totalArticles < end) {
                        articles.add(orderedArticle);
                    }
                    totalArticles++;
                }

            }

            // Build the params for paging
            params = new StringBuilder("?");
            if (!noFilters) {
                try {
                    if (!skipTheme) {
                        params.append("&theme=");
                        params.append(URLEncoder.encode(selectedThemeFilter, "UTF-8"));
                    }
                    if (!skipCategory) {
                        params.append("&category=");
                        params.append(URLEncoder.encode(selectedCategoryFilter, "UTF-8"));
                    }
                    if (!skipRegion) {
                        params.append("&region=");
                        params.append(URLEncoder.encode(selectedRegionFilter, "UTF-8"));
                    }
                    params.append("&country=");
                    if (!skipCountry) {
                        params.append(URLEncoder.encode(selectedCountryFilter, "UTF-8"));
                    } else {
                        params.append("All");
                    }
                } catch (UnsupportedEncodingException ex) {
                    log.error("Failed to encode the params", ex);
                }
            }
            params.append("&page=");
            params.deleteCharAt(params.indexOf("&"));

            articles = DateUtils.sortByMostRecentArticles(articles);

            log.debug("Total Articles: " + totalArticles + "\nArticles created: " + articles.size());
        } else {
            log.warn("tag ID was not a valid tag, therefore no articles found");
        }

        return articles;
    }

    /**
     * This method builds the filter lists used on the landing page.
     *
     * @param resource
     *            the resource to pull tags from
     * @param tagManager
     *            the tag manager instance
     * @param article
     *            the article we're pulling info from
     */
    private void buildFilterLists(Resource resource, TagManager tagManager, Article article) {
        Tag[] tags = tagManager.getTags(resource);
        for (Tag tagFromPage : tags) {
            String tagPath = tagFromPage.getPath();

            //let's create a list for each tag's relationships
            List<String> tagList = new ArrayList<String>();
            //if the list has already been started, we'll pull it and add to it
            if (relationshipMap.get(tagFromPage.getTitle()) != null) {
                tagList = relationshipMap.get(tagFromPage.getTitle());
            } else if (relationshipMap.get(tagFromPage.getName()) != null) {
                tagList = relationshipMap.get(tagFromPage.getName());
            }

            if (tagPath.contains(NamespaceEnum.THEME.getNamespace())) {

                // if this isn't a theme landing page, create the filter list
                if (!namespace.contains(NamespaceEnum.THEME.getNamespace())) {

                    if (themeTags.containsKey(tagFromPage)) {
                        themeTags.put(tagFromPage, themeTags.get(tagFromPage) + 1);
                    } else {
                        themeTags.put(tagFromPage, 1L);
                    }

                    for (Tag tag : tags) {
                        if (!tagList.contains(tag.getTitle()) || !tagList.contains(tag.getName())) {
                            if (tag.getPath().contains(NamespaceEnum.REGION.getNamespace()) || tag.getPath().contains(NamespaceEnum.COUNTRY.getNamespace())) {
                                tagList.add(tag.getName());
                            } else {
                                tagList.add(tag.getTitle());
                            }
                        }
                    }
                    relationshipMap.put(tagFromPage.getTitle(), tagList);
                }
            } else if (tagPath.contains(NamespaceEnum.CATEGORY.getNamespace())) {
                // if this isn't a category landing page, create the filter list
                if (!namespace.contains(NamespaceEnum.CATEGORY.getNamespace())) {
                    if (categoryTags.containsKey(tagFromPage)) {
                        categoryTags.put(tagFromPage, categoryTags.get(tagFromPage) + 1);
                    } else {
                        categoryTags.put(tagFromPage, 1L);
                    }

                    for (Tag tag : tags) {
                        if (!tagList.contains(tag.getTitle()) || !tagList.contains(tag.getName())) {
                            if (tag.getPath().contains(NamespaceEnum.REGION.getNamespace()) || tag.getPath().contains(NamespaceEnum.COUNTRY.getNamespace())) {
                                tagList.add(tag.getName());
                            } else {
                                tagList.add(tag.getTitle());
                            }
                        }
                    }
                    relationshipMap.put(tagFromPage.getTitle(), tagList);
                }
            } else if (tagPath.contains(NamespaceEnum.REGION.getNamespace())) {
                // if this isn't a region landing page, create the filter list
                if (article != null) {
                    article.setRegion(tagFromPage.getName());
                }
                if (!namespace.contains(NamespaceEnum.REGION.getNamespace())) {
                    if (!regionTagList.contains(tagFromPage)) {
                        regionTagList.add(tagFromPage);
                    }
                    for (Tag tag : tags) {
                        if (!tagList.contains(tag.getTitle()) || !tagList.contains(tag.getName())) {
                            if (tag.getPath().contains(NamespaceEnum.REGION.getNamespace()) || tag.getPath().contains(NamespaceEnum.COUNTRY.getNamespace())) {
                                tagList.add(tag.getName());
                            } else {
                                tagList.add(tag.getTitle());
                            }
                        }
                    }
                    relationshipMap.put(tagFromPage.getTitle(), tagList);
                }
            } else if (tagPath.contains(NamespaceEnum.COUNTRY.getNamespace())) {
                // no matter what, create the country filter list
                if (article != null) {
                    if (tagFromPage.getName().equalsIgnoreCase(selectedCountryFilter)) {
                        article.setCountry(tagFromPage.getName());
                    }
                }
                if (!countryTagList.contains(tagFromPage)) {
                    if (!StringUtils.contains(tagFromPage.getTagID(), "/")) {
                        countryTagList.add(tagFromPage);
                    }
                }
                for (Tag tag : tags) {
                    if (tag.getPath().contains(NamespaceEnum.REGION.getNamespace()) || tag.getPath().contains(NamespaceEnum.THEME.getNamespace()) || tag.getPath().contains(NamespaceEnum.CATEGORY.getNamespace())) {
                        if (!tagList.contains(tag.getTitle()) || !tagList.contains(tag.getName())) {
                            if (tag.getPath().contains(NamespaceEnum.REGION.getNamespace()) || tag.getPath().contains(NamespaceEnum.COUNTRY.getNamespace())) {
                            tagList.add(tag.getName());
                            } else {
                                tagList.add(tag.getTitle());
                            }
                        }
                    }
                }
                relationshipMap.put(tagFromPage.getTitle(), tagList);
            }
            for (Tag tag : tags) {
                if (tag.getPath().contains(NamespaceEnum.REGION.getNamespace()) || tag.getPath().contains(NamespaceEnum.THEME.getNamespace()) || tag.getPath().contains(NamespaceEnum.CATEGORY.getNamespace())) {
                    if (!tagList.contains(tag.getTitle())) {
                        tagList.add(tag.getTitle());
                    }
                }
            }
            if (tagFromPage.getPath().contains(NamespaceEnum.REGION.getNamespace()) || tagFromPage.getPath().contains(NamespaceEnum.COUNTRY.getNamespace())) {
                relationshipMap.put(tagFromPage.getName(), tagList);
            } else if (tagFromPage.getPath().contains(NamespaceEnum.THEME.getNamespace()) || tagFromPage.getPath().contains(NamespaceEnum.CATEGORY.getNamespace())) {
                relationshipMap.put(tagFromPage.getTitle(), tagList);
            }
        }

        //create a JSON object to store these relationships for use on the front end
        JSONObject jsonObject = new JSONObject(relationshipMap);
        //create a string for the front end
        relationshipJSON = jsonObject.toString();
    }

    /**
     * gets list of articles.
     *
     * @return articles the list of articles
     */
    public List<Article> getArticles() {
        return articles;
    }

    /**
     * sets list of articles.
     *
     * @param articles
     *            the articles to set
     */
    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    /**
     * gets the landing title of the page.
     *
     * @return landingTitle the landing title of the page
     */
    public String getLandingTitle() {
        return landingTitle;
    }

    /**
     * Sets the landing title to the tag title of currently being searched for.
     *
     * @param landingTitle
     *            the landing title to display
     */
    public void setLandingTitle(String landingTitle) {
        this.landingTitle = landingTitle;
    }

    /**
     * Get the selector.
     *
     * @return The selector
     */
    public String getSelector() {
        return selector;
    }

    /**
     * Set the selector.
     *
     * @param selector
     *            The selector to set
     */
    public void setSelector(String selector) {
        this.selector = selector;
    }

    /**
     * Get the categoryTags.
     *
     * @return The categoryTags
     */
    public Map<Tag, Long> getCategoryTags() {
        return categoryTags;
    }

    /**
     * Set the categoryTags.
     *
     * @param categoryTags
     *            The categoryTags to set
     */
    public void setCategoryTags(Map<Tag, Long> categoryTags) {
        this.categoryTags = categoryTags;
    }

    /**
     * Get the themeTags.
     *
     * @return The themeTags
     */
    public Map<Tag, Long> getThemeTags() {
        return themeTags;
    }

    /**
     * Set the themeTags.
     *
     * @param themeTags
     *            The themeTags to set
     */
    public void setThemeTags(Map<Tag, Long> themeTags) {
        this.themeTags = themeTags;
    }

    /**
     * Get the regionTagList.
     *
     * @return The regionTagList
     */
    public List<Tag> getRegionTagList() {
        return regionTagList;
    }

    /**
     * Set the regionTagList.
     *
     * @param regionTagList
     *            The regionTagList to set
     */
    public void setRegionTagList(List<Tag> regionTagList) {
        this.regionTagList = regionTagList;
    }

    /**
     * Get the countryTagList.
     *
     * @return The countryTagList
     */
    public List<Tag> getCountryTagList() {
        return countryTagList;
    }

    /**
     * Set the countryTagList.
     *
     * @param countryTagList
     *            The countryTagList to set
     */
    public void setCountryTagList(List<Tag> countryTagList) {
        this.countryTagList = countryTagList;
    }

    /**
     * Get the useThemeFilter.
     *
     * @return The useThemeFilter
     */
    public Boolean getUseThemeFilter() {
        return useThemeFilter;
    }

    /**
     * Set the useThemeFilter.
     *
     * @param useThemeFilter
     *            The useThemeFilter to set
     */
    public void setUseThemeFilter(Boolean useThemeFilter) {
        this.useThemeFilter = useThemeFilter;
    }

    /**
     * Sets new useRegionFilter.
     *
     * @param useRegionFilter
     *            New value of useRegionFilter. the region filter value to set
     */
    public void setUseRegionFilter(Boolean useRegionFilter) {
        this.useRegionFilter = useRegionFilter;
    }

    /**
     * Gets useRegionFilter.
     *
     * @return Value of useRegionFilter. boolean to say whether or not to use the region filter
     */
    public Boolean getUseRegionFilter() {
        return useRegionFilter;
    }

    /**
     * Check if the filter should be used or skipped.
     *
     * @param filterString
     *            The filter string to check
     * @return True if the filter should be skippped, false otherwise
     */
    private boolean skipFilter(String filterString) {
        return DEFAULTFILTER.equals(filterString) || StringUtils.isBlank(filterString);
    }

    /**
     * Sets new selectedCategoryFilter.
     *
     * @param selectedCategoryFilter
     *            New value of selectedCategoryFilter.
     */
    public void setSelectedCategoryFilter(String selectedCategoryFilter) {
        this.selectedCategoryFilter = selectedCategoryFilter;
    }

    /**
     * Sets new selectedThemeFilter.
     *
     * @param selectedThemeFilter
     *            New value of selectedThemeFilter.
     */
    public void setSelectedThemeFilter(String selectedThemeFilter) {
        this.selectedThemeFilter = selectedThemeFilter;
    }

    /**
     * Sets new selectedRegionFilter.
     *
     * @param selectedRegionFilter
     *            New value of selectedRegionFilter.
     */
    public void setSelectedRegionFilter(String selectedRegionFilter) {
        this.selectedRegionFilter = selectedRegionFilter;
    }

    /**
     * Gets selectedRegionFilter.
     *
     * @return Value of selectedRegionFilter.
     */
    public String getSelectedRegionFilter() {
        return selectedRegionFilter;
    }

    /**
     * Gets selectedCategoryFilter.
     *
     * @return Value of selectedCategoryFilter.
     */
    public String getSelectedCategoryFilter() {
        return selectedCategoryFilter;
    }

    /**
     * Gets selectedCountryFilter.
     *
     * @return Value of selectedCountryFilter.
     */
    public String getSelectedCountryFilter() {
        return selectedCountryFilter;
    }

    /**
     * Gets selectedThemeFilter.
     *
     * @return Value of selectedThemeFilter.
     */
    public String getSelectedThemeFilter() {
        return selectedThemeFilter;
    }

    /**
     * Sets new selectedCountryFilter.
     *
     * @param selectedCountryFilter
     *            New value of selectedCountryFilter.
     */
    public void setSelectedCountryFilter(String selectedCountryFilter) {
        this.selectedCountryFilter = selectedCountryFilter;
    }

    /**
     * Get the pagesize.
     *
     * @return The pagesize
     */
    public int getPagesize() {
        return PAGESIZE;
    }

    /**
     * Get the totalArticles.
     *
     * @return The totalArticles
     */
    public long getTotalArticles() {
        return totalArticles;
    }

    /**
     * Get the pageNumber.
     *
     * @return The pageNumber
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Get the page count.
     *
     * @return The page count
     */
    public int getPageCount() {
        return (int) Math.ceil(totalArticles / (double) PAGESIZE);
    }

    /**
     * Get the parameters for paging.
     *
     * @return The parameters for paging
     */
    public String getParams() {
        return params.toString();
    }


    /**
     * Gets relationshipMap.
     *
     * @return Value of relationshipMap.
     */
    public Map<String, List<String>> getRelationshipMap() {
        return relationshipMap;
    }

    /**
     * Sets new relationshipMap.
     *
     * @param relationshipMap New value of relationshipMap.
     */
    public void setRelationshipMap(Map<String, List<String>> relationshipMap) {
        this.relationshipMap = relationshipMap;
    }


    /**
     * Sets new relationshipJSON.
     *
     * @param relationshipJSON New value of relationshipJSON.
     */
    public void setRelationshipJSON(String relationshipJSON) {
        this.relationshipJSON = relationshipJSON;
    }

    /**
     * Gets relationshipJSON.
     *
     * @return Value of relationshipJSON.
     */
    public String getRelationshipJSON() {
        return relationshipJSON;
    }
}
