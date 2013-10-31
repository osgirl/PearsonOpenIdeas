/**
 * Created: Jun 17, 2013 11:35:45 AM
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
package com.pearson.openideas.cq5.components.services.enums;

/**
 * Enum for the solr sync property.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 */
public enum SolrSyncPropertyEnum {

    PROPERTY_NAME("SolrSyncStatus"),
    PROCESSING("Processing"),
    PASSED("Passed"),
    FAILED("Failed");

    private String propertyValue;

    /**
     * Constructor.
     * 
     * @param propertyValue
     *            The property value to set for the property
     */
    private SolrSyncPropertyEnum(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    /**
     * Get the propertyValue.
     * 
     * @return The propertyValue
     */
    public String getPropertyValue() {
        return propertyValue;
    }

    /**
     * Set the propertyValue.
     * 
     * @param propertyValue
     *            The propertyValue to set
     */
    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }
}
