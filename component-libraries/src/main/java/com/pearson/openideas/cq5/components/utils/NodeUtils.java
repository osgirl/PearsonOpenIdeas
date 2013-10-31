/**
 * Created: June 13, 2013 2:18:25 PM
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
package com.pearson.openideas.cq5.components.utils;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

/**
 * Utility class for manipulating nodes.
 * 
 * @author Todd M Guerra
 * 
 * @version 2.0
 */
public class NodeUtils {

    // the logger
    // private static final Logger log = LoggerFactory.getLogger(NodeUtils.class);

    /**
     * Empty private constructor since this is a util class.
     */
    private NodeUtils() {

    }

    /**
     * Gets the first property from a node.
     * 
     * @param node
     *            the node that we are pulling the value from
     * @param propName
     *            the property we are pulling from this node
     * @return the first property value
     * @throws RepositoryException
     *             thrown if the property cannot be obtained
     */
    public static String getSingleProperty(Node node, String propName) throws RepositoryException {
        Property property = node.getProperty(propName);
        Value value = property.getValue();
        return value.getString();
    }
}
