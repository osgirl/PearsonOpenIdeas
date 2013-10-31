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

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pearson.openideas.cq5.components.beans.search.SortField;
import com.pearson.openideas.cq5.components.beans.solr.SolrSearchParameters;

/**
 * Provides static helper methods used for interaction with SOLR.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 * 
 */
public class SolrHelper {

    private static final Logger log = LoggerFactory.getLogger(SolrHelper.class);

    /**
     * private constructor to prevent instantiation.
     */
    private SolrHelper() {
        // do nothing
    }

    /**
     * Build a SolrQuery object based on the parameters.
     * 
     * @param parameters
     *            the search parameters
     * @return the SolrQuery object
     */
    public static SolrQuery buildQuery(final SolrSearchParameters parameters) {
        log.debug("Building SolrQuery.");
        log.info("Build the base query.");

        String q = "*:*";

        if (parameters.getQuery() != null) {
            q = parameters.getQuery();
        }
        SolrQuery query = new SolrQuery(q);

        if (parameters.getType() != null) {
            log.debug("set query query type to {}", parameters.getType());
            query.setQueryType(parameters.getType());
        }

        if (parameters.getDisplayCount() > 0) {
            log.debug("set query display count to {}", parameters.getDisplayCount());
            query.setRows(parameters.getDisplayCount());
        }

        if (parameters.getCurrentRecord() > 0) {
            log.debug("set query start to {}", parameters.getCurrentRecord());
            query.setStart(parameters.getCurrentRecord());
        }
        log.info("Done building the base query.");

        log.info("Adding facet to query.");
        addFacet(parameters.getFacetField(), query);
        log.info("Done adding facet to query.");

        log.info("Adding search criteria to query.");
        addSearchCriteria(parameters.getSearchCriteria(), query);
        log.info("Done adding search criteria to query.");

        log.info("Adding filter criteria to query.");
        addFilterCriteria(parameters.getFilterCriteria(), query);
        log.info("Done adding filter criteria to query.");

        log.info("Adding sort fields to query.");
        addSortFields(parameters.getSortFields(), query);
        log.info("Done adding sort fields to query.");

        log.debug("done building SolrQuery, returning.");
        return query;
    }

    /**
     * Add sort fields to a SOLR Query.
     * 
     * @param sortFields
     *            the list of SortFields to add
     * @param query
     *            the query
     */
    public static void addSortFields(final List<SortField> sortFields, final SolrQuery query) {
        if (sortFields != null && sortFields.size() > 0) {
            for (SortField fld : sortFields) {
                if (fld.getField() != null && fld.getDirection() != null) {
                    log.debug("Adding sort field {} with direction {}.", fld.getField(), fld.getDirection().toString());
                    query.addSortField(fld.getField(),
                            fld.getDirection().toString().equalsIgnoreCase("asc") ? SolrQuery.ORDER.asc
                                    : SolrQuery.ORDER.desc);
                }
            }
        }
    }

    /**
     * Add Filter Criteria to a SOLR Query.
     * 
     * @param filterCriteria
     *            the list of filter criteria
     * @param query
     *            the query
     */
    public static void addFilterCriteria(final Map<String, List<Object>> filterCriteria, final SolrQuery query) {
        if (filterCriteria != null && filterCriteria.size() > 0) {
            for (String key : filterCriteria.keySet()) {
                List<Object> values = filterCriteria.get(key);
                for (Object value : values) {

                    if (value == null) {
                        log.debug("Adding null filter criteria {}.", key);
                        query.addFilterQuery(key);
                    } else if (value instanceof String[]) {
                        ModifiableSolrParams params = new ModifiableSolrParams();
                        for (String s : (String[]) value) {
                            log.debug("Adding multi-value filter criteria {}:{}.", key, s);
                            params.add(key, s);
                        }
                        query.add(params);
                    } else {
                        log.debug("Adding single-value filter criteria {}:{}.", key, String.valueOf(value));
                        query.addFilterQuery(key + ":" + String.valueOf(value));
                    }
                }
            }
        }

    }

    /**
     * Add search criteria to a SOLR Query.
     * 
     * @param searchCriteria
     *            the search criteria to add
     * @param query
     *            the query
     */
    public static void addSearchCriteria(final Map<String, Object> searchCriteria, final SolrQuery query) {
        for (String key : searchCriteria.keySet()) {
            Object obj = searchCriteria.get(key);
            if (obj instanceof String[]) {
                log.debug("Adding multi-value search criteria {}={}.", key, String.valueOf(obj));
                query.setParam(key, (String[]) obj);
            } else {
                log.debug("Adding single-value search criteria {}={}.", key, String.valueOf(obj));
                query.setParam(key, String.valueOf(obj));
            }

        }

    }

    /**
     * Add a facet field to a SOLR Query.
     * 
     * @param facetField
     *            the facet field to be added
     * @param query
     *            the query
     */
    public static void addFacet(final String facetField, final SolrQuery query) {
        if (facetField != null && facetField.trim().length() > 0) {
            log.debug("Adding facet for field {}.", facetField);
            query.setFacet(true);
            query.addFacetField(facetField);
            query.setFacetMinCount(1);
            query.setFacetSort(facetField);
        }
    }
}
