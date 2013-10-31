/**
 * Created: Jun 5, 2013 12:45:00 AM
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
package com.pearson.openideas.cq5.components.services.solr;

import java.util.HashMap;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.osgi.framework.Constants;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Sling Service used to get a {@link SolrServer} reference.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 * 
 */
@Component(label = "Pearson Open Ideas SOLR Server Configuration Service", immediate = true)
@Property(name = Constants.SERVICE_DESCRIPTION, value = "Open Ideas Site SOLR Server Configuration Service")
@Service(SolrServerConfigurationService.class)
public class SolrServerConfigurationService {

    // Should SOLR requests follow redirects
    private static final boolean SOLR_SERVER_FOLLOW_REDIRECTS = false;

    // Max retry count for SOLR requests
    private static final int SOLR_SERVER_MAX_RETRIES = 1;

    // Max total connections for SOLR Server
    private static final int SOLR_SERVER_MAX_TOTAL_CONNECTIONS = 100;

    // Max connection per host for SOLR Server
    private static final int SOLR_SERVER_MAX_CONNECTIONS_PER_HOST = 20;

    private static final Logger log = LoggerFactory.getLogger(SolrServerConfigurationService.class);

    // Map of Available SOLR Servers based on the URL
    private Map<String, SolrServer> servers;

    /**
     * get the SOLR Server instance.
     * 
     * @return the SolrServer
     * @param solrServerUrl
     *            the url of the SOLR Server
     * @throws Exception
     *             if an error occurs
     */
    public SolrServer getServer(final String solrServerUrl) throws Exception {
        SolrServer solrServer = servers.get(solrServerUrl);
        if (solrServer == null) {
            log.info("Server was null, creating.");
            HttpSolrServer httpSolrServer = new HttpSolrServer(solrServerUrl);

            httpSolrServer.setDefaultMaxConnectionsPerHost(SOLR_SERVER_MAX_CONNECTIONS_PER_HOST);
            httpSolrServer.setMaxTotalConnections(SOLR_SERVER_MAX_TOTAL_CONNECTIONS);
            httpSolrServer.setFollowRedirects(SOLR_SERVER_FOLLOW_REDIRECTS);
            httpSolrServer.setMaxRetries(SOLR_SERVER_MAX_RETRIES);
            solrServer = httpSolrServer;
            servers.put(solrServerUrl, solrServer);
        }

        return solrServer;
    }

    /**
     * The Activate Method is called on Service Activation. Gets property values from OSGI Config.
     * 
     * @param context
     *            the OSGI Component Context object
     */
    protected void activate(final ComponentContext context) {
        log.info("Activating SolrServerConfigurationService");
        servers = new HashMap<String, SolrServer>();
    }

}
