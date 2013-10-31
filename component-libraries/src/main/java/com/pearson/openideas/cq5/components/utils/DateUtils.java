/**
 * Created: May 15, 2013 4:00:25 PM
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

import com.pearson.openideas.cq5.components.beans.Article;
import com.pearson.openideas.cq5.components.enums.DateFormatEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Utility class to handle date formatting and sorting.
 * 
 * @version 2.0
 * 
 * @author Todd M. Guerra
 */
public class DateUtils {

    private static final Logger log = LoggerFactory.getLogger(DateUtils.class);

    /**
     * Empty private constructor since this is a utility class.
     */
    private DateUtils() {

    }

    /**
     * This method takes a list of articles and sorts by most recent date.
     * 
     * @param listOfArticles
     *            the list of articles to sort
     * @return sorted list of articles
     */
    public static List<Article> sortByMostRecentArticles(List<Article> listOfArticles) {

        final SimpleDateFormat sdf = new SimpleDateFormat(DateFormatEnum.CLIENTDATEFORMAT.getDateFormat());

        Collections.sort(listOfArticles, new Comparator<Article>() {
            public int compare(Article a1, Article a2) {
                if (a1.getPubDate() == null || a2.getPubDate() == null) {
                    return 0;
                }
                try {
                    return a2.getPubDateObject().compareTo(a1.getPubDateObject());
                } catch (Exception e) {
                    log.debug("related articles date could not be parsed!");
                }
                return 0;
            }
        });

        return listOfArticles;
    }
}
