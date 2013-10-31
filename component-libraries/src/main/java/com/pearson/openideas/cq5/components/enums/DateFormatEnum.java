/**
 * Created: May 1, 2012 1:34:33 PM
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
package com.pearson.openideas.cq5.components.enums;

/**
 * Enum representing the Date Formats.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 */
public enum DateFormatEnum {

    CQDATEFORMAT("MM/dd/yy"), CLIENTDATEFORMAT("EEE dd MMMM yyyy");

    private final String dateFormat;

    /**
     * Constructor.
     * 
     * @param dateFormat
     *            The date format
     */
    private DateFormatEnum(String dateFormat) {
        this.dateFormat = dateFormat;

    }

    /**
     * Get the date format.
     * 
     * @return The string representing the date format.
     */
    public String getDateFormat() {
        return dateFormat;
    }
}
