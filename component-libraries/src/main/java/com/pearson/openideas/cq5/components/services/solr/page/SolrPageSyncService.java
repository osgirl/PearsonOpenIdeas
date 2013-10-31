/**
 * Created: Jun 17, 2013 1:15:39 PM
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
package com.pearson.openideas.cq5.components.services.solr.page;

import com.day.cq.tagging.JcrTagManagerFactory;
import com.day.cq.tagging.TagManager;
import com.pearson.openideas.cq5.components.beans.Article;
import com.pearson.openideas.cq5.components.beans.solr.Page;
import com.pearson.openideas.cq5.components.enums.IndexedPageTypeEnum;
import com.pearson.openideas.cq5.components.services.enums.SolrSyncPropertyEnum;
import com.pearson.openideas.cq5.components.utils.ArticleUtils;
import com.pearson.openideas.cq5.components.utils.IndexingUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * @version 2.0
 * 
 * @author Sam Labib
 */
@Component(label = "Pearson Open Ideas Solr Page Sync Service", immediate = true, metatype = true)
@Service(value = SolrPageSyncService.class)
public class SolrPageSyncService {

    private static final Logger log = LoggerFactory.getLogger(SolrPageSyncService.class);

    @Reference
    private SlingRepository repository;

    @Reference
    private SolrPageSearchService solrPageSearchService;

    @Reference
    private JcrTagManagerFactory jcrTagManagerFactory;

    @Reference
    private ResourceResolverFactory resolverFactory;

    /**
     * Sync the provided page to solr.
     * 
     * @param session
     *            The session to use
     * @param path
     *            The page path
     * @param action
     *            The sync action.
     */
    public void syncPage(Session session, String path, String action) {
        try {
            TagManager tagManager = jcrTagManagerFactory.getTagManager(session);
            Node node = session.getNode(path).getNode("jcr:content");
            if (node == null) {
                log.warn("Not syncing node: " + path);
            } else {
                node.setProperty(SolrSyncPropertyEnum.PROPERTY_NAME.getPropertyValue(),
                        SolrSyncPropertyEnum.PROCESSING.getPropertyValue());
                session.save();
                if ("Activate".equals(action)) {
                    log.info("Activating the node: " + path);
                    try {
                        String slingType = node.getProperty("sling:resourceType").getString();
                        ResourceResolver resourceResolver = resolverFactory.getAdministrativeResourceResolver(null);
                        Page page = new Page();
                        if (slingType.endsWith("openIdeasArticleTemplate")) {

                            // set the page path
                            page.setUrl(path);
                            page = processArticlePage(page, node, resourceResolver, tagManager);

                            solrPageSearchService.index(page);
                            node.setProperty(SolrSyncPropertyEnum.PROPERTY_NAME.getPropertyValue(),
                                    SolrSyncPropertyEnum.PASSED.getPropertyValue());
                            log.info("Index the page");
                        } else if (slingType.endsWith("openIdeasEventsTemplate")) {
                            page.setUrl(path);
                            page = processEventPage(page, node, resourceResolver);
                            solrPageSearchService.index(page);
                            node.setProperty(SolrSyncPropertyEnum.PROPERTY_NAME.getPropertyValue(),
                                    SolrSyncPropertyEnum.PASSED.getPropertyValue());
                        } else if (slingType.endsWith("openIdeasContributeTemplate")) {
                            page.setUrl(path);
                            page = processContributePage(page, node, resourceResolver);
                            solrPageSearchService.index(page);
                            node.setProperty(SolrSyncPropertyEnum.PROPERTY_NAME.getPropertyValue(),
                                    SolrSyncPropertyEnum.PASSED.getPropertyValue());
                        } else if (slingType.endsWith("openIdeasContributorsListTemplate")) {
                            page.setUrl(path);
                            page = processContributorsPage(page, node, resourceResolver, tagManager);
                            solrPageSearchService.index(page);
                            node.setProperty(SolrSyncPropertyEnum.PROPERTY_NAME.getPropertyValue(),
                                    SolrSyncPropertyEnum.PASSED.getPropertyValue());
                        } else {
                            log.debug("We do not index these pages");
                        }
                    } catch (Exception ex) {
                        log.error("Failed to index: " + path, ex);
                        node.setProperty(SolrSyncPropertyEnum.PROPERTY_NAME.getPropertyValue(),
                                SolrSyncPropertyEnum.FAILED.getPropertyValue());
                    }
                } else if ("Deactivate".equals(action)) {
                    log.info("DeaActivating the node: " + path);
                    try {
                        solrPageSearchService.unIndex(path);
                        node.setProperty(SolrSyncPropertyEnum.PROPERTY_NAME.getPropertyValue(),
                                SolrSyncPropertyEnum.PASSED.getPropertyValue());
                        log.info("UnIndex the page");
                    } catch (Exception ex) {
                        log.error("Failed to unIndex: " + path, ex);
                        node.setProperty(SolrSyncPropertyEnum.PROPERTY_NAME.getPropertyValue(),
                                SolrSyncPropertyEnum.FAILED.getPropertyValue());
                    }
                }
                session.save();
            }
        } catch (RepositoryException ex) {
            log.error("Failed to get the node from the repository", ex);
        }
    }

    /**
     * This method processes an Article page node.
     * 
     * @param processedPage
     *            the page object to be assigned data
     * @param node
     *            the node that we are indexing
     * @param resourceResolver
     *            the resource resolver to turn node into resource
     * @param tagManager
     *            the tag manager to send to our utils class
     * @return the processed page to index
     */
    private Page processArticlePage(Page processedPage, Node node, ResourceResolver resourceResolver,
            TagManager tagManager) {
        log.debug("processing an article page");
        processedPage.setPageType(IndexedPageTypeEnum.ARTICLE.getIndexedPageType());
        Resource resource = null;
        try {
            resource = resourceResolver.getResource(node.getPath());
        } catch (RepositoryException e) {
            log.error("error getting repository node", e);
        }
        Article article = new Article();

        article = ArticleUtils.createArticleObject(resource, article, tagManager, true);

        // Set the values for the page to be indexed
        processedPage.setShortSummary(article.getShortSummary());
        processedPage.setBodyText(article.getArticleText());
        processedPage.setCategory(article.getCategory());
        processedPage.setContributor(article.getContributors());
        processedPage.setCountry(article.getCountries());
        processedPage.setOrganization(article.getOrganisations());
        processedPage.setKeywords(article.getKeywords());
        processedPage.setRegion(article.getRegion());
        processedPage.setContributorBiog(article.getContributorBiogs());
        processedPage.setTheme(article.getTheme());
        processedPage.setTitle(article.getArticleTitle());
        processedPage.setCitation(article.getCitation());

        return processedPage;
    }

    /**
     * This method processes an Event page node.
     * 
     * @param processedPage
     *            the page object to be assigned data
     * @param node
     *            the node that we are indexing
     * @param resourceResolver
     *            the resource resolver to turn node into resource
     * @return the processed page to index
     */
    private Page processEventPage(Page processedPage, Node node, ResourceResolver resourceResolver) {
        processedPage.setTitle("Events");
        processedPage.setPageType(IndexedPageTypeEnum.EVENT.getIndexedPageType());

        Resource resource = null;
        try {
            resource = resourceResolver.getResource(node.getPath());
        } catch (RepositoryException e) {
            log.error("error getting repository node", e);
        }

        processedPage = IndexingUtils.populateEventPage(processedPage, resource);

        return processedPage;
    }

    /**
     * This method processes a Contribute page node.
     * 
     * @param processedPage
     *            the page object to be assigned data
     * @param node
     *            the node that we are indexing
     * @param resourceResolver
     *            the resource resolver to turn node into resource
     * @return the processed page to index
     */
    private Page processContributePage(Page processedPage, Node node, ResourceResolver resourceResolver) {
        processedPage.setTitle("Contribute");
        processedPage.setPageType(IndexedPageTypeEnum.CONTRIBUTE.getIndexedPageType());

        Resource resource = null;
        try {
            resource = resourceResolver.getResource(node.getPath());
        } catch (RepositoryException e) {
            log.error("error getting repository node", e);
        }

        processedPage = IndexingUtils.populateContributePage(processedPage, resource);

        return processedPage;
    }

    /**
     * This method processes a Contributors page node.
     * 
     * @param processedPage
     *            the page object to be assigned data
     * @param node
     *            the node that we are indexing
     * @param resourceResolver
     *            the resource resolver to turn node into resource
     * @param tagManager
     *            the tag manager to send to our utils class
     * @return the processed page to index
     */
    private Page processContributorsPage(Page processedPage, Node node, ResourceResolver resourceResolver,
            TagManager tagManager) {
        processedPage.setTitle("Contributors");
        processedPage.setPageType(IndexedPageTypeEnum.CONTRIBUTORS.getIndexedPageType());

        Resource resource = null;
        try {
            resource = resourceResolver.getResource(node.getPath());
        } catch (RepositoryException e) {
            log.error("error getting repository node", e);
        }

        processedPage = IndexingUtils.populateContributorsPage(processedPage, resource, tagManager);

        return processedPage;
    }
}
