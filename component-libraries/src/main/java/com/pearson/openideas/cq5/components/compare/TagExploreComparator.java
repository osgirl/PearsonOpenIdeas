/**
 * Created: Jun 3, 2013 1:27:00 PM
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
package com.pearson.openideas.cq5.components.compare;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.day.cq.tagging.Tag;

/**
 * Comparator used to order tags based on the explore order.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 */
public class TagExploreComparator implements Comparator<Tag> {

    private Map<Tag, Integer> tagPosition = new HashMap<Tag, Integer>();

    /**
     * Constructor used to build the tag positions.
     * 
     * @param exploreTags
     *            The explore tag array used to figure out the tag position.
     */
    public TagExploreComparator(Tag[] exploreTags) {
        int counter = 0;
        for (Tag tag : exploreTags) {
            tagPosition.put(tag, counter);
            counter++;
        }
    }

    /**
     * {@inheritDoc}
     */
    public int compare(Tag tag1, Tag tag2) {
        return tagPosition.get(tag1).compareTo(tagPosition.get(tag2));
    }

}
