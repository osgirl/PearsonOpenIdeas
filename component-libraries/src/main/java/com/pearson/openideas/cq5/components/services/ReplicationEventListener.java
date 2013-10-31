/**
 * Created: Jun 1, 2013 11:15:00 AM
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

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationAction;
import com.pearson.openideas.cq5.components.services.solr.page.SolrPageSyncService;

/**
 * This event listener is fired on publish. It will be used to index the content to solr.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 */
@Component
@Service
@Property(name = "event.topics", value = { ReplicationAction.EVENT_TOPIC })
public class ReplicationEventListener implements EventHandler {

    // the logger
    private static final Logger log = LoggerFactory.getLogger(ReplicationEventListener.class);

    @Reference
    private SlingRepository repository;

    @Reference
    private SolrPageSyncService solrPageSyncService;

    /**
     * {@inheritDoc}
     */
    public void handleEvent(Event event) {
        ReplicationAction action = ReplicationAction.fromEvent(event);

        if (action != null) {
            log.info("Replication action {} occured on {} ", action.getType().getName(), action.getPath());

            Session session = null;
            try {
                session = repository.loginAdministrative(null);
                if (action.getPath().startsWith("/content/plc/prkc/uk/open-ideas")) {
                    solrPageSyncService.syncPage(session, action.getPath(), action.getType().getName());
                }
            } catch (RepositoryException ex) {
                log.error("Failed to get the node from the repository", ex);
            } finally {
                if (session != null && session.isLive()) {
                    session.logout();
                    session = null;
                }
            }
        }
    }
}
