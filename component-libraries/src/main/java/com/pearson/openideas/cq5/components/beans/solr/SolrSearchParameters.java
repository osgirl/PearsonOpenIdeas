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
package com.pearson.openideas.cq5.components.beans.solr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pearson.openideas.cq5.components.beans.search.SortField;

/**
 * Object to specify search parameters for querying SOLR.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 * 
 */
public class SolrSearchParameters {

    private String type;
    private Integer displayCount = 0;
    private Integer currentRecord = 0;
    private Integer totalRecords = 0;
    private String facetField;
    private List<Facets> facets;
    private Map<String, Object> searchCriteria;
    private Map<String, List<Object>> filterCriteria;
    private List<SortField> sortFields;
    private String query;

    /**
     * get the type.
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * set the type.
     * 
     * @param type
     *            the type to set
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * get the display count.
     * 
     * @return the displayCount
     */
    public Integer getDisplayCount() {
        return displayCount;
    }

    /**
     * set the display count.
     * 
     * @param displayCount
     *            the displayCount to set
     */
    public void setDisplayCount(final Integer displayCount) {
        this.displayCount = displayCount;
    }

    /**
     * get the current record.
     * 
     * @return the currentRecord
     */
    public Integer getCurrentRecord() {
        return currentRecord;
    }

    /**
     * set the current record.
     * 
     * @param currentRecord
     *            the currentRecord to set
     */
    public void setCurrentRecord(final Integer currentRecord) {
        this.currentRecord = currentRecord;
    }

    /**
     * get the facet field.
     * 
     * @return the facetField
     */
    public String getFacetField() {
        return facetField;
    }

    /**
     * set the facet field.
     * 
     * @param facetField
     *            the facetField to set
     */
    public void setFacetField(final String facetField) {
        this.facetField = facetField;
    }

    /**
     * get the search criteria.
     * 
     * @return the searchCriteria
     */
    public Map<String, Object> getSearchCriteria() {
        if (searchCriteria == null) {
            searchCriteria = new HashMap<String, Object>();
        }
        return searchCriteria;
    }

    /**
     * set the search criteria.
     * 
     * @param searchCriteria
     *            the searchCriteria to set
     */
    public void setSearchCriteria(final Map<String, Object> searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    /**
     * get the filter criteria.
     * 
     * @return the filterCriteria
     */
    public Map<String, List<Object>> getFilterCriteria() {
        return filterCriteria;
    }

    /**
     * set the filter criteria.
     * 
     * @param filterCriteria
     *            the filterCriteria to set
     */
    public void setFilterCriteria(final Map<String, List<Object>> filterCriteria) {
        this.filterCriteria = filterCriteria;
    }

    /**
     * get the sort fields.
     * 
     * @return the sortFields
     */
    public List<SortField> getSortFields() {
        if (sortFields == null) {
            sortFields = new ArrayList<SortField>();
        }
        return sortFields;
    }

    /**
     * set the sort fields.
     * 
     * @param sortFields
     *            the sortFields to set
     */
    public void setSortFields(final List<SortField> sortFields) {
        this.sortFields = sortFields;
    }

    /**
     * set or add the filter criteria.
     * 
     * @param name
     *            the name of the filter to be added
     * @param value
     *            the filter criteria to set or add
     * 
     */
    public void addFilterCriteria(final String name, final Object value) {
        if (filterCriteria == null) {
            filterCriteria = new HashMap<String, List<Object>>();
        }
        List<Object> values = filterCriteria.get(name);
        if (values == null) {
            values = new ArrayList<Object>();
        }
        values.add(value);
        filterCriteria.put(name, values);
    }

    /**
     * Get the query.
     * 
     * @return The query
     */
    public String getQuery() {
        return query;
    }

    /**
     * Set the query.
     * 
     * @param query
     *            The query
     */
    public void setQuery(final String query) {
        this.query = query;
    }

    /**
     * Add a facet to the search parameters.
     * 
     * @param name
     *            the name of the facet to add
     * @param count
     *            the number of items contained in the facet
     */
    public void addFacet(final String name, final int count) {
        if (facets == null) {
            facets = new ArrayList<Facets>();
        }
        facets.add(new Facets(name, count));
    }

    /**
     * get the list of facets.
     * 
     * @return the facets
     */
    public List<Facets> getFacets() {
        return facets;
    }

    /**
     * Get the total number records.
     * 
     * @return the total number of records
     */
    public Integer getTotalRecords() {
        return totalRecords;
    }

    /**
     * Set the total number records.
     * 
     * @param totalRecords
     *            the total number records.
     */
    public void setTotalRecords(final Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    /**
     * Bean Class representing a search facet.
     * 
     * @author sam.labib
     * 
     */
    public class Facets {
        private String name;
        private int count;

        /**
         * construct the facet instance.
         * 
         * @param name
         *            the name of the facet field
         * @param count
         *            the count of items in the facet
         */
        public Facets(final String name, final int count) {
            this.name = name;
            this.count = count;
        }

        /**
         * get the count of items in the facet.
         * 
         * @return the count
         */
        public int getCount() {
            return count;
        }

        /**
         * set the count of items in the facet.
         * 
         * @param count
         *            the count to set
         */
        public void setCount(final int count) {
            this.count = count;
        }

        /**
         * get the name of the facet field.
         * 
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * set the name of the facet field.
         * 
         * @param name
         *            the name
         */
        public void setName(final String name) {
            this.name = name;
        }
    }

}
