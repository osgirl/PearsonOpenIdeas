/**
 * Created: Jun 17, 2013 11:02:05 AM
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

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.jcr.api.SlingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.pearson.openideas.cq5.components.services.enums.SolrSyncPropertyEnum;
import com.pearson.openideas.cq5.components.services.solr.page.SolrPageSyncService;

/**
 * Service to run and sync any pages that failed to sync during normal replication.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 */
@Component(label = "Pearson Open Ideas Replication Recovery Service", immediate = true, metatype = true)
@Service
@Property(name = "scheduler.expression", value = "0 0 0 * * ?")
public class ReplicationRecoveryService implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ReplicationRecoveryService.class);

    @Reference
    private QueryBuilder builder;

    @Reference
    private SlingRepository repository;

    @Reference
    private SolrPageSyncService solrPageSyncService;

    /**
     * {@inheritDoc}
     */
    public void run() {
        Session session = null;

        // Build the query
        Map<String, String> map = new HashMap<String, String>();
        map.put("path", "/content/plc/prkc/uk/open-ideas");
        map.put("type", "cq:Page");
        map.put("property", "jcr:content/" + SolrSyncPropertyEnum.PROPERTY_NAME.getPropertyValue());
        map.put("property.1_value", SolrSyncPropertyEnum.FAILED.getPropertyValue());
        map.put("property.2_value", SolrSyncPropertyEnum.PROCESSING.getPropertyValue());

        try {
            session = repository.loginAdministrative(null);
            Query query = builder.createQuery(PredicateGroup.create(map), session);
            query.setHitsPerPage(0);
            SearchResult result = query.getResult();

            for (Hit hit : result.getHits()) {
                log.debug("Path found for hit: " + hit.getPath());
                Node node = session.getNode(hit.getPath()).getNode("jcr:content");
                if (node == null) {
                    log.warn("Not syncing node: " + hit.getPath());
                } else {
                    String action = node.getProperty("cq:lastReplicationAction").getString();
                    solrPageSyncService.syncPage(session, hit.getPath(), action);
                }
            }
        } catch (RepositoryException ex) {
            log.error("failed to get session", ex);
        }
    }
}
