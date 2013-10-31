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
 * SortDirection, used by {@link SortField}.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 * 
 */
public enum SortDirection {
    ASC("ASC"),
    DESC("DESC");

    // String representation of the sort direction
    private final String direction;

    /**
     * Construct a new SortDirection.
     * 
     * @param direction
     *            the sort direction as a String "ASC" or "DESC"
     */
    SortDirection(final String direction) {
        this.direction = direction;
    }

    /**
     * @return the direction String
     */
    @Override
    public String toString() {
        return direction;
    }
}
