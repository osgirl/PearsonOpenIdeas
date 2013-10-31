/**
 * Created: June 17, 2013 %:31 PM
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

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.pearson.openideas.cq5.components.beans.solr.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Utility class for use during indexing of unstructured pages.
 * 
 * @author Todd M Guerra
 * 
 * @version 2.0
 */
public class IndexingUtils {

    // the logger
    // private static final Logger log = LoggerFactory.getLogger(IndexingUtils.class);

    /**
     * Empty private constructor since this is a utils class.
     */
    private IndexingUtils() {

    }

    /**
     * This method assigns the data we need to index for Events pages.
     * 
     * @param page
     *            the page object to assign data to
     * @param resource
     *            the resource we're pulling data from
     * @return the populated page object
     */
    public static Page populateEventPage(Page page, Resource resource) {

        Resource par = resource.getChild("par");
        StringBuilder builder = new StringBuilder();

        if (par != null) {
            Iterator<Resource> parResources = par.listChildren();
            while (parResources.hasNext()) {
                Resource parResource = parResources.next();
                ValueMap resourceMap = parResource.adaptTo(ValueMap.class);
                String type = resourceMap.get("sling:resourceType", "");
                if (type.endsWith("event")) {
                    builder.append(resourceMap.get("eventName", ""));
                    builder.append(" ");
                    builder.append(resourceMap.get("eventDescription", ""));
                    builder.append(" ");
                }
            }
        }

        String eventText = builder.toString();
        page.setBodyText(eventText);

        return page;
    }

    /**
     * This method assigns the data we need to index for the contribute (contact us) page.
     * 
     * @param page
     *            the page object to populate
     * @param resource
     *            the resource we are pulling the data from
     * @return the populated page object
     */
    public static Page populateContributePage(Page page, Resource resource) {

        Resource par = resource.getChild("par");

        if (par != null) {
            Iterator<Resource> parResources = par.listChildren();

            while (parResources.hasNext()) {
                Resource parResource = parResources.next();
                ValueMap resourceMap = parResource.adaptTo(ValueMap.class);
                String type = resourceMap.get("sling:resourceType", "");
                if (type.endsWith("confirmation")) {
                    page.setBodyText(resourceMap.get("text", ""));
                }
            }
        }
        return page;
    }

    /**
     * This method pulls the data from the contributors page and populates a page object with it for indexing.
     * 
     * @param page
     *            the page object to populate
     * @param resource
     *            the resource we are pulling data from
     * @param tagManager
     *            the tag manager used for managing the tags on this page
     * @return the populated page object
     */
    public static Page populateContributorsPage(Page page, Resource resource, TagManager tagManager) {

        Resource par = resource.getChild("par");

        List<String> contributorBiogs = new ArrayList<String>();
        List<String> contributors = new ArrayList<String>();

        if (par != null) {
            Iterator<Resource> parResources = par.listChildren();

            while (parResources.hasNext()) {
                Resource parResource = parResources.next();
                ValueMap resourceMap = parResource.adaptTo(ValueMap.class);
                String type = resourceMap.get("sling:resourceType", "");
                if (type.endsWith("articleContributor")) {
                    Tag[] contribTags = tagManager.getTags(parResource);
                    if (contribTags != null && contribTags.length > 0) {
                        contributors.add(contribTags[0].getTitle());
                        contributorBiogs.add(contribTags[0].getDescription());
                    }
                } else if (type.endsWith("confirmation")) {
                    page.setBodyText(resourceMap.get("text", ""));
                }
            }
        }

        page.setContributor(contributors);
        page.setContributorBiog(contributorBiogs);

        return page;
    }
}
