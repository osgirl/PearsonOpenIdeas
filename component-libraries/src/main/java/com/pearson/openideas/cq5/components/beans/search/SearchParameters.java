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
package com.pearson.openideas.cq5.components.beans.search;

import java.util.List;

/**
 * Model object for the search parameters.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 */
public class SearchParameters {

    private int totalRecords;
    private int currentPage;
    private int currentRecord;
    private int displayCount;
    private List<SortField> sortFields;

    /**
     * Get the totalRecords.
     * 
     * @return The totalRecords
     */
    public int getTotalRecords() {
        return totalRecords;
    }

    /**
     * Set the totalRecords.
     * 
     * @param totalRecords
     *            The totalRecords to set
     */
    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    /**
     * Get the currentPage.
     * 
     * @return The currentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Set the currentPage.
     * 
     * @param currentPage
     *            The currentPage to set
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * Get the currentRecord.
     * 
     * @return The currentRecord
     */
    public int getCurrentRecord() {
        return currentRecord;
    }

    /**
     * Set the currentRecord.
     * 
     * @param currentRecord
     *            The currentRecord to set
     */
    public void setCurrentRecord(int currentRecord) {
        this.currentRecord = currentRecord;
    }

    /**
     * Get the displayCount.
     * 
     * @return The displayCount
     */
    public int getDisplayCount() {
        return displayCount;
    }

    /**
     * Set the displayCount.
     * 
     * @param displayCount
     *            The displayCount to set
     */
    public void setDisplayCount(int displayCount) {
        this.displayCount = displayCount;
    }

    /**
     * Get the sortFields.
     * 
     * @return The sortFields
     */
    public List<SortField> getSortFields() {
        return sortFields;
    }

    /**
     * Set the sortFields.
     * 
     * @param sortFields
     *            The sortFields to set
     */
    public void setSortFields(List<SortField> sortFields) {
        this.sortFields = sortFields;
    }
}
