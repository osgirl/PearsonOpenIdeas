/**
 * Created: Jun 12, 2013 11:13:12 PM
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
package com.pearson.openideas.cq5.components.search;

import com.crownpartners.cq.quickstart.core.component.AbstractComponent;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.tagging.TagManager.FindResults;
import com.day.cq.wcm.foundation.Image;
import com.pearson.openideas.cq5.components.beans.solr.Page;
import com.pearson.openideas.cq5.components.beans.solr.SolrResults;
import com.pearson.openideas.cq5.components.beans.solr.SolrSearchParameters;
import com.pearson.openideas.cq5.components.enums.IndexedPageTypeEnum;
import com.pearson.openideas.cq5.components.enums.NamespaceEnum;
import com.pearson.openideas.cq5.components.services.solr.page.SolrPageSearchService;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * This component is used for searching and displaying the search results.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 */
public class SearchResults extends AbstractComponent {

    private static final Logger log = LoggerFactory.getLogger(SearchResults.class);

    private String searchText;
    private String searchType;
    private List<Page> pages;

    // Paging
    private static final int PAGESIZE = 10;
    private long totalMatches;
    private int matchesOnThisPage;
    private int start;
    private int pageNumber;
    private StringBuilder params;

    /**
     * Constructor.
     * 
     * @param pageContext
     *            The page context
     */
    public SearchResults(PageContext pageContext) {
        super(pageContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {

        searchText = getSlingRequest().getParameter("searchbox");
        searchType = getSlingRequest().getParameter("searchType");

        searchText = StringEscapeUtils.escapeJavaScript(searchText);
        searchText = StringEscapeUtils.escapeHtml(searchText);

        if (StringUtils.isNotBlank(searchText)) {
            // calculate page information
            String pageStr = getRequestParameter("page");
            pageNumber = 1;
            if (pageStr != null) {
                try {
                    pageNumber = Integer.parseInt(pageStr);
                } catch (NumberFormatException ex) {
                    log.error("The page number is not a valid number: " + pageNumber, ex);
                }
            }
            log.debug("The page number is: " + pageNumber);
            start = (PAGESIZE * (pageNumber - 1));

            if (start == 0) {
                TagManager tagManager = getResourceResolver().adaptTo(TagManager.class);
                FindResults findResults = tagManager.findByTitle(searchText);
                Tag[] tags = findResults.tags;
                if (tags.length > 0) {
                    for (Tag tag : tags) {
                        log.debug("The tag title is: " + tag.getTitle());
                        if (tag.getCount() > 0 && tag.getTitle().equalsIgnoreCase(searchText)) {
                            StringBuilder url = new StringBuilder(getSlingRequest().getContextPath());
                            url.append("explore/");
                            if (tag.getTagID().startsWith(NamespaceEnum.THEME.getNamespace())) {
                                url.append("theme");
                            } else if (tag.getTagID().startsWith(NamespaceEnum.CATEGORY.getNamespace())) {
                                url.append("sector");
                            } else if (tag.getTagID().startsWith(NamespaceEnum.REGION.getNamespace())) {
                                url.append("region");
                            } else {
                                log.info("Unknown tagID: " + tag.getTagID());
                                continue;
                            }
                            url.append('.');
                            url.append(tag.getName());
                            url.append(".html");
                            // Redirect to a landing page
                            try {
                                log.debug("redirecting to: " + url.toString());
                                getSlingResponse().sendRedirect(url.toString());
                            } catch (IOException ex) {
                                log.error("Failed to redirect to: " + url.toString(), ex);
                            }
                            return;
                        }
                    }
                }
            }

            SolrPageSearchService solrPageSearchService = getSlingScriptHelper()
                    .getService(SolrPageSearchService.class);
            SolrSearchParameters param = new SolrSearchParameters();
            String originalSearchText = searchText;
            if ("Exact phrase".equals(searchType)) {
                searchText = '"' + searchText + '"';
            } else if ("All words".equals(searchType)) {
                searchText = searchText.replaceAll(" ", " AND ");
            }
            log.debug("\n\nSearch Text: " + searchText);

            param.setQuery(searchText);
            param.setDisplayCount(PAGESIZE);
            param.setCurrentRecord(start);
            try {
                params = new StringBuilder("?searchbox=");
                params.append(URLEncoder.encode(searchText, "UTF-8"));
                if (StringUtils.isNotBlank(searchType)) {
                    params.append("&searchType=");
                    params.append(URLEncoder.encode(searchType, "UTF-8"));
                }
                params.append("&page=");
                SolrResults<Page> solrResults = solrPageSearchService.retrieve(param);
                totalMatches = solrResults.getTotalMatches();
                log.debug("Total matches for this search: " + totalMatches);
                pages = solrResults.getBeans();
                Map<String, Map<String, List<String>>> highlighting = solrResults.getHighlighting();

                matchesOnThisPage = pages.size();
                log.debug("RESULTS THIS PAGE: " + matchesOnThisPage);
                for (Page page : pages) {

                    //need to check for null here, in case a page was deleted from publish without being unindexed
                    if (getResourceResolver().getResource(page.getUrl() + "/jcr:content") != null) {
                    page.setIsValid(true);
                    log.info("title: " + page.getTitle());
                    if (IndexedPageTypeEnum.ARTICLE.getIndexedPageType().equals(page.getPageType())) {
                        Resource resource = getResourceResolver().getResource(page.getUrl() + "/jcr:content");
                        if (resource.getChild("articleBody").getChild("imagethumb") != null) {
                            Image image = new Image(resource.getChild("articleBody").getChild("imagethumb"));
                            image.setSelector(".img");
                            page.setImage(image);
                            log.debug("do we have an THUMBNAIL image on this search result? " + image.hasContent());
                        }  else {
                            Image image = new Image(resource.getChild("articleBody").getChild("image"));
                            image.setSelector(".img");
                            page.setImage(image);
                            log.debug("Or, do we have a regular image? " + image.hasContent());
                        }
                    }

                    Map<String, List<String>> map = highlighting.get(page.getUrl());
                    if (map != null && map.size() > 0) {
                        for (String key : map.keySet()) {
                            String excerpt = map.get(key).get(0);
                            excerpt = StringUtils.replace(excerpt, "<p>", "");
                            excerpt = StringUtils.replace(excerpt, "</p>", "");
                            page.setExcerpt(excerpt);
                            break;
                        }
                    }
                }
                }
            } catch (Exception ex) {
                log.error("Failed to get the search results", ex);
            }


            //ugly hack, I hate myself for doing this
            searchText = originalSearchText;
            //but it came in handy...this way the extra stuff added for specific searches is never sent back to the front end

        } else {
            log.warn("The search term is empty");
        }
    }

    /**
     * Get the searchText.
     * 
     * @return The searchText
     */
    public String getSearchText() {
        return searchText;
    }

    /**
     * Set the searchText.
     * 
     * @param searchText
     *            The searchText to set
     */
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    /**
     * Get the pages.
     * 
     * @return The pages
     */
    public List<Page> getPages() {
        return pages;
    }

    /**
     * Set the pages.
     * 
     * @param pages
     *            The pages to set
     */
    public void setPages(List<Page> pages) {
        this.pages = pages;
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
     * Get the totalMatches.
     * 
     * @return The totalMatches
     */
    public long getTotalMatches() {
        return totalMatches;
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
        return (int) Math.ceil(totalMatches / (double) PAGESIZE);
    }

    /**
     * Get the start of the list we're displaying.
     * 
     * @return The start of the list we're displaying.
     */
    public int getListStart() {
        return start + 1;
    }

    /**
     * Get the end of the list we're displaying.
     * 
     * @return The end of the list we're displaying
     */
    public long getListEnd() {
        int end = pageNumber * PAGESIZE;
        if (end > totalMatches) {
            return totalMatches;
        }
        return end;
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
     * Sets new matchesOnThisPage.
     *
     * @param matchesOnThisPage New value of matchesOnThisPage.
     */
    public void setMatchesOnThisPage(int matchesOnThisPage) {
        this.matchesOnThisPage = matchesOnThisPage;
    }

    /**
     * Gets matchesOnThisPage.
     *
     * @return Value of matchesOnThisPage.
     */
    public int getMatchesOnThisPage() {
        return matchesOnThisPage;
    }


    /**
     * Sets new searchType.
     *
     * @param searchType New value of searchType.
     */
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    /**
     * Gets searchType.
     *
     * @return Value of searchType.
     */
    public String getSearchType() {
        return searchType;
    }
}
