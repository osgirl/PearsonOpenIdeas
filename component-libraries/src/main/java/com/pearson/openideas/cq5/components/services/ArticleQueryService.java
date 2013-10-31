/**
 * Created: June 10, 2013 8:40:00 PM
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

package com.pearson.openideas.cq5.components.services;

import java.util.List;
import java.util.Map;

/**
 * This service executes queries and returns a list of articles for our newsletter.
 * 
 * @author Todd M Guerra
 * 
 * @version 2.0
 */
public interface ArticleQueryService {

    /**
     * This method gets a list of recent articles.
     * 
     * @param map
     *            The map containing the search parameters
     * @return The list of articles. The string is the path
     */
    List<String> getArticles(Map<String, String> map);
}
