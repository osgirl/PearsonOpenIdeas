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
 * Enum representing the Namespaces.
 * 
 * @version 2.0
 * 
 * @author Todd M Guerra
 */
public enum NamespaceEnum {

    THEME("plc-prkc-uk-open-ideas-themes"),
    CATEGORY("plc-prkc-uk-open-ideas-categories"),
    COUNTRY("plc-prkc-uk-open-ideas-countries"),
    KEYWORD("plc-prkc-uk-open-ideas-keywords"),
    ORGANISATION("plc-prkc-uk-open-ideas-organisations"),
    REGION("plc-prkc-uk-open-ideas-regions"),
    HOMEPAGE("plc-prkc-uk-open-ideas-homepage"),
    CONTRIBUTOR("plc-prkc-uk-open-ideas-contributors");

    private String namespace;

    /**
     * Constructor.
     * 
     * @param namespace
     *            the namespace
     */
    private NamespaceEnum(String namespace) {
        this.namespace = namespace;
    }

    /**
     * Get the namespace.
     * 
     * @return the value of the namespace
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Get the namespace tag ID.
     *
     * @return the value of the namespace tag ID
     */
    public String getNamespaceTagId() {
        return namespace + ":";
    }

}
