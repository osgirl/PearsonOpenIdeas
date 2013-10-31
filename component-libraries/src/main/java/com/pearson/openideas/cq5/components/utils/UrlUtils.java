/**
 * Created: Jul 12, 2013 11:09:37 AM
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

import com.day.cq.wcm.api.Page;
import com.pearson.openideas.cq5.components.services.PropertiesService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;

/**
 * A utility to help with URL building and redirecting.
 * 
 * @version 2.0
 * 
 * @author Sam Labib
 */
public class UrlUtils {

    // the logger
    private static final Logger log = LoggerFactory.getLogger(UrlUtils.class);

    /**
     * Empty private constructor since this is a utility class.
     */
    private UrlUtils() {
    }

    /**
     * Get the page URL including the selectors and parameters.
     * 
     * @param currentPage
     *            The current page
     * @param slingScriptHelper
     *            The sling script helper
     * @param slingRequest
     *            The sling request
     * @return The URL
     */
    public static String getPageUrl(Page currentPage, SlingScriptHelper slingScriptHelper,
            SlingHttpServletRequest slingRequest) {

        log.debug("The current page is: " + currentPage.getPath());
        StringBuilder urlBuilder = new StringBuilder(currentPage.getPath());
        if (slingRequest.getRequestPathInfo().getSelectors() != null
                && slingRequest.getRequestPathInfo().getSelectors().length > 0) {
            String[] selectors = slingRequest.getRequestPathInfo().getSelectors();
            for (String selector : selectors) {
                urlBuilder.append('.');
                urlBuilder.append(selector);
            }
        }
        urlBuilder.append(".html?");

        Enumeration<String> keys = slingRequest.getParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            urlBuilder.append('&');
            urlBuilder.append(key);
            urlBuilder.append('=');
            try {
                urlBuilder.append(URLEncoder.encode(slingRequest.getParameter(key), "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                log.error("UnsupportedEncodingException", ex);
            }
        }
        log.debug("The URL is: " + urlBuilder.toString());
        return urlBuilder.toString();
    }

    /**
     * Redirect the user after logout.
     * 
     * @param currentPage
     *            The current page
     * @param slingScriptHelper
     *            The sling script helper
     * @param slingRequest
     *            The sling request
     * @param slingResponse
     *            The sling response
     * @param forceRedirect
     *            A flag indicating whether to always redirect or only if it is the profile page.
     */
    public static void logoutRedirect(Page currentPage, SlingScriptHelper slingScriptHelper,
            SlingHttpServletRequest slingRequest, SlingHttpServletResponse slingResponse, boolean forceRedirect) {
        String url = null;
        if (currentPage.getName().equals("profile")) {
            PropertiesService propertiesService = slingScriptHelper.getService(PropertiesService.class);
            url = propertiesService.getPublishUrl();
            log.debug("URL is being redirected to: " + url);
        } else if (forceRedirect) {
            url = getPageUrl(currentPage, slingScriptHelper, slingRequest);
        }
        if (url != null) {
            url = url.replace("&logout=true", "");
            url = url.replace("?logout=true", "");
            try {
                slingResponse.sendRedirect(url);
            } catch (IOException e) {
                log.error("Error redirecting", e);
            }
        }
    }
}
