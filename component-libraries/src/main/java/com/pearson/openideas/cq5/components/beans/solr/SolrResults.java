/**
 * Created: Jun 18, 2013 2:36:52 PM
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

import java.util.List;
import java.util.Map;

/**
 * @version 2.0
 * 
 * @author Sam Labib
 * @param <E>
 */
public class SolrResults<E> {

    private List<E> beans;
    private Map<String, Map<String, List<String>>> highlighting;
    private long totalMatches;

    /**
     * Get the beans.
     * 
     * @return The beans
     */
    public List<E> getBeans() {
        return beans;
    }

    /**
     * Set the beans.
     * 
     * @param beans
     *            The beans to set
     */
    public void setBeans(List<E> beans) {
        this.beans = beans;
    }

    /**
     * Get the highlighting.
     * 
     * @return The highlighting
     */
    public Map<String, Map<String, List<String>>> getHighlighting() {
        return highlighting;
    }

    /**
     * Set the highlighting.
     * 
     * @param highlighting
     *            The highlighting to set
     */
    public void setHighlighting(Map<String, Map<String, List<String>>> highlighting) {
        this.highlighting = highlighting;
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
     * Set the totalMatches.
     * 
     * @param totalMatches
     *            The totalMatches to set
     */
    public void setTotalMatches(long totalMatches) {
        this.totalMatches = totalMatches;
    }

}
