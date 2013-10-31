/**
 * Created: Jun 3, 2013 1:43:26 PM
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

import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.pearson.openideas.cq5.components.enums.NamespaceEnum;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility for managing the Explore Arrays.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 */
public class ExploreUtils {

    private Tag[] categoryTags;
    private Tag[] themeTags;
    private Tag[] regionTags;

    /**
     * Private Constructor.
     * 
     * @param pageProperties
     *            The page properties
     * @param tagManager
     *            The tagManager to use
     */
    public ExploreUtils(InheritanceValueMap pageProperties, TagManager tagManager) {
        categoryTags = getTag(tagManager, pageProperties.getInherited("sectors", String[].class),
                NamespaceEnum.CATEGORY.getNamespaceTagId());
        themeTags = getTag(tagManager, pageProperties.getInherited("themes", String[].class),
                NamespaceEnum.THEME.getNamespaceTagId());
        regionTags = getTag(tagManager, pageProperties.getInherited("regions", String[].class),
                NamespaceEnum.REGION.getNamespaceTagId());
    }

    /**
     * Get the tag names for the full list.
     * 
     * @param tagManager
     *            The tagManager to use
     * @param saved
     *            The saved tag names in order
     * @param tagId
     *            The tag id to use
     * @return The full tag name list.
     */
    private Tag[] getTag(TagManager tagManager, String[] saved, String tagId) {
        List<Tag> list = new LinkedList<Tag>();
        Tag tag;
        if (saved != null) {
            for (String id : saved) {
                tag = tagManager.resolve(id);
                if (tag != null && tag.getCount() > 0) {
                    list.add(tag);
                }
            }
        }
        Tag categoryNameSpace = tagManager.resolve(tagId);
        Iterator<Tag> categoryNameSpaceIterator = categoryNameSpace.listChildren();
        while (categoryNameSpaceIterator.hasNext()) {
            tag = categoryNameSpaceIterator.next();
            if (tag != null && tag.getCount() > 0 && !list.contains(tag)) {
                list.add(tag);
            }
        }

        return list.toArray(new Tag[list.size()]);
    }

    /**
     * Get the categoryTags.
     * 
     * @return The categoryTags
     */
    public Tag[] getCategoryTags() {
        return categoryTags;
    }

    /**
     * Get the themeTags.
     * 
     * @return The themeTags
     */
    public Tag[] getThemeTags() {
        return themeTags;
    }

    /**
     * Get the regionTags.
     * 
     * @return The regionTags
     */
    public Tag[] getRegionTags() {
        return regionTags;
    }
}
