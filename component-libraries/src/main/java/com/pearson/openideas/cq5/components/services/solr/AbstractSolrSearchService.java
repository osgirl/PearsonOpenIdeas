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

import java.io.IOException;
import java.util.Dictionary;
import java.util.List;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pearson.openideas.cq5.components.beans.solr.SolrResults;
import com.pearson.openideas.cq5.components.beans.solr.SolrSearchParameters;

/**
 * An Abstract implementation of the SolrSearchService which provides implementation for core solr operations.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 * 
 * @param <E>
 *            the entity type handled by the implementing SolrSearchService
 */
@Component(componentAbstract = true)
public abstract class AbstractSolrSearchService<E> implements SolrSearchService<E> {

    private static final String MSG_REMOVE_INDEX = "An Exception occurred while removing documents from index.";
    private static final String MSG_INDEX = "An Exception occurred while indexing documents.";

    private static final Logger log = LoggerFactory.getLogger(AbstractSolrSearchService.class);

    // Property Name for the OSGI Config Property to get the SOLR Server URL
    @Property(label = "SOLR Master Server URL", description = "Master SOLR Server URL", value = AbstractSolrSearchService.DEFAULT_URL)
    private static final String MASTER_SOLR_SERVER_URL_PROPERTY = "openideas.master.solr.server.url";
    @Property(label = "SOLR Slave Server URL", description = "Slave SOLR Server URL", value = AbstractSolrSearchService.DEFAULT_URL)
    private static final String SLAVE_SOLR_SERVER_URL_PROPERTY = "openideas.slave.solr.server.url";

    private static final String DEFAULT_URL = "http://localhost:8983/solr/openIdeas";

    // the SOLR Server URL
    private String masterSolrServerUrl;
    private String slaveSolrServerUrl;

    @Reference
    private SolrServerConfigurationService solrServerConfigService;

    /**
     * Get a {@link SolrServer} instance.
     * 
     * @param master
     *            A flag indicating whether to get the master server or the slave server.
     * @return the SOLR Server to interact with
     * @throws Exception
     *             if an exception occurs while getting the server.
     */
    protected SolrServer getServer(boolean master) throws Exception {
        if (master) {
            return getSolrServerConfigService().getServer(getMasterSolrServerUrl());
        }
        return getSolrServerConfigService().getServer(getSlaveSolrServerUrl());
    }

    /**
     * get the entity class.
     * 
     * @return the Class Object
     */
    protected abstract Class<E> getEntityClass();

    /**
     * 
     * {@inheritDoc}
     */
    public SolrResults<E> retrieve(final SolrSearchParameters parameters) throws Exception {
        SolrResults<E> results = new SolrResults<E>();

        try {
            SolrServer server = getServer(false);
            SolrQuery query = SolrHelper.buildQuery(parameters);
            QueryResponse response = server.query(query);

            parameters.setTotalRecords((int) response.getResults().getNumFound());

            List<FacetField> facets = response.getFacetFields();
            if (facets != null) {
                for (FacetField facet : facets) {
                    List<Count> counts = facet.getValues();
                    if (counts != null) {
                        for (Count count : counts) {
                            parameters.addFacet(count.getName(), (int) count.getCount());
                        }
                    }
                }
            }

            results.setTotalMatches(response.getResults().getNumFound());
            results.setHighlighting(response.getHighlighting());
            results.setBeans(response.getBeans(getEntityClass()));
        } catch (SolrServerException e) {
            String msg = "An Exception Occurred while querying SOLR";
            log.error(msg, e);
            throw new Exception(msg, e);
        }

        return results;
    }

    /**
     * {@inheritDoc}
     */
    public void index(final E bean) throws Exception {
        SolrServer server = getServer(true);
        try {
            server.addBean(bean);
            server.commit();
        } catch (SolrServerException e) {
            log.error(MSG_INDEX, e);
            throw new Exception(MSG_INDEX, e);
        } catch (IOException e) {
            log.error(MSG_INDEX, e);
            throw new Exception(MSG_INDEX, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void index(final List<E> beans) throws Exception {
        SolrServer server = getServer(true);
        try {
            server.addBeans(beans);
            server.commit();
        } catch (SolrServerException e) {
            log.error(MSG_INDEX, e);
            throw new Exception(MSG_INDEX, e);
        } catch (IOException e) {
            log.error(MSG_INDEX, e);
            throw new Exception(MSG_INDEX, e);
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    public void unIndex(final String uniqueId) throws Exception {
        SolrServer server = getServer(true);
        try {
            server.deleteById(uniqueId);
            server.commit();
        } catch (SolrServerException e) {
            log.error(MSG_REMOVE_INDEX, e);
            throw new Exception(MSG_REMOVE_INDEX, e);
        } catch (IOException e) {
            log.error(MSG_REMOVE_INDEX, e);
            throw new Exception(MSG_REMOVE_INDEX, e);
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    public void unIndex(final List<String> ids) throws Exception {
        SolrServer server = getServer(true);
        try {
            server.deleteById(ids);
            server.commit();
        } catch (SolrServerException e) {
            log.error(MSG_REMOVE_INDEX, e);
            throw new Exception(MSG_REMOVE_INDEX, e);
        } catch (IOException e) {
            log.error(MSG_REMOVE_INDEX, e);
            throw new Exception(MSG_REMOVE_INDEX, e);
        }
    }

    /**
     * Get the solr server config service.
     * 
     * @return The solr server config service
     */
    public SolrServerConfigurationService getSolrServerConfigService() {
        return solrServerConfigService;
    }

    /**
     * Get the Master Solr Server Url.
     * 
     * @return The Master Solr Server Url
     */
    public String getMasterSolrServerUrl() {
        return masterSolrServerUrl;
    }

    /**
     * Set the Master Solr Server Url.
     * 
     * @param masterSolrServerUrl
     *            The Master Solr Server Url to set
     */
    public void setMasterSolrServerUrl(String masterSolrServerUrl) {
        this.masterSolrServerUrl = masterSolrServerUrl;
    }

    /**
     * Get the Slave Solr Server Url.
     * 
     * @return The Slave Solr Server Url
     */
    public String getSlaveSolrServerUrl() {
        return slaveSolrServerUrl;
    }

    /**
     * Set the Slave Solr Server Url.
     * 
     * @param slaveSolrServerUrl
     *            The Slave Solr Server Url to set
     */
    public void setSlaveSolrServerUrl(String slaveSolrServerUrl) {
        this.slaveSolrServerUrl = slaveSolrServerUrl;
    }

    /**
     * Activate the component to get the SOLR URL property value from the OSGI configuration.
     * 
     * @param context
     *            the osgi component context
     */
    protected void activate(final ComponentContext context) {
        log.info("Activating SolrServerConfigurationService {}", this.getClass().getName());
        Dictionary<?, ?> properties = context.getProperties();
        setMasterSolrServerUrl(PropertiesUtil.toString(properties.get(MASTER_SOLR_SERVER_URL_PROPERTY), DEFAULT_URL));
        setSlaveSolrServerUrl(PropertiesUtil.toString(properties.get(SLAVE_SOLR_SERVER_URL_PROPERTY), DEFAULT_URL));
    }
}
