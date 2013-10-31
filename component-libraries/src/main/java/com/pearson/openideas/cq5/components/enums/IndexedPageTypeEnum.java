/**
 * Created: June 18, 2013 10:20AM 
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
 * Enum representing the indexed page types.
 * 
 * @version 2.0
 * 
 * @author Todd M Guerra
 */
public enum IndexedPageTypeEnum {

    ARTICLE("article"),
    CONTRIBUTE("contribute"),
    CONTRIBUTORS("contributors"),
    EVENT("event");

    private String indexedPageType;

    /**
     * Constructor.
     * 
     * @param indexedPageType
     *            the indexed page type
     */
    private IndexedPageTypeEnum(String indexedPageType) {
        this.indexedPageType = indexedPageType;
    }

    /**
     * Gets the indexed page type.
     * 
     * @return the indexed page type
     */
    public String getIndexedPageType() {
        return indexedPageType;
    }
}
