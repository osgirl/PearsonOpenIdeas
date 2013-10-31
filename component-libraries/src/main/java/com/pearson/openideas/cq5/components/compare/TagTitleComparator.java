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

import com.day.cq.tagging.Tag;

/**
 * Comparator used to order tags by title.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 */
public class TagTitleComparator implements Comparator<Tag> {

    /**
     * {@inheritDoc}
     */
    public int compare(Tag tag1, Tag tag2) {
        return tag1.getTitle().compareTo(tag2.getTitle());
    }

}
