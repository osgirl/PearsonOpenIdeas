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

/**
 * This Class represents a query sort field, allowing specification of the field name and direction to sort on.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 */
public class SortField {

    private SortDirection direction;
    private String field;

    /**
     * Construct a new SortField.
     * 
     * @param field
     *            Name of the field to sort by.
     * @param direction
     *            Direction to sort (ascending or descending)
     */
    public SortField(final String field, final SortDirection direction) {
        this.field = field;
        this.direction = direction;
    }

    /**
     * Construct a new SortField.
     * 
     * @return SortDirection enum representing ascending or descending sort order
     */
    public SortDirection getDirection() {
        return direction;
    }

    /**
     * set the direction of the sort field (ASC or DESC).
     * 
     * @param direction
     *            enum representing ascending or descending sort order
     */
    public void setDirection(final SortDirection direction) {
        this.direction = direction;
    }

    /**
     * get the name of the field to sort.
     * 
     * @return String representing the field to sort by
     */
    public String getField() {
        return field;
    }

    /**
     * Set the name of the field to sort.
     * 
     * @param field
     *            String representing the field to sort by
     */
    public void setField(final String field) {
        this.field = field;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[direction=" + getDirection() + ";field=" + field + "]";
    }

    /**
     * sets the sort direction for this field.
     * 
     * @param directionValue
     *            the direction values as a String, "ASC" or "DESC"
     */
    public void setSortDirection(final String directionValue) {
        if (directionValue.equalsIgnoreCase(SortDirection.DESC.toString())) {
            direction = SortDirection.DESC;
        } else {
            direction = SortDirection.ASC;
        }
    }
}
