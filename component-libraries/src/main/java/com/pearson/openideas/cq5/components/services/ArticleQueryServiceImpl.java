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
package com.pearson.openideas.cq5.components.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

/**
 * This service executes queries and returns a list of articles for our newsletter.
 * 
 * @author Todd M Guerra
 * 
 * @version 2.0
 */
@Component(label = "Pearson Open Ideas Article Query Service", immediate = true, metatype = true)
@Service(ArticleQueryService.class)
@Properties({ @Property(name = Constants.SERVICE_DESCRIPTION, value = "Pearson Open Ideas Article Query Service"),
        @Property(name = Constants.SERVICE_VENDOR, value = "CROWN") })
public class ArticleQueryServiceImpl implements ArticleQueryService {

    @Reference
    private QueryBuilder builder;

    @Reference
    private SlingRepository repository;

    private static final Logger log = LoggerFactory.getLogger(ArticleQueryServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    public List<String> getArticles(Map<String, String> map) {

        Session session = null;
        List<String> paths = new ArrayList<String>();

        try {
            session = repository.loginAdministrative(null);
            Query query = builder.createQuery(PredicateGroup.create(map), session);
            query.setHitsPerPage(0);
            SearchResult result = query.getResult();

            log.debug("total matches: " + result.getTotalMatches());
            log.debug("Hits? " + result.getHits().size());

            for (Hit hit : result.getHits()) {
                log.debug("Path found for hit: " + hit.getPath());
                paths.add(hit.getPath());
            }
        } catch (Exception e) {
            log.error("failed to get session", e);
        } finally {
            if (session != null && session.isLive()) {
                session.logout();
                session = null;
            }
        }

        return paths;

    }
}
