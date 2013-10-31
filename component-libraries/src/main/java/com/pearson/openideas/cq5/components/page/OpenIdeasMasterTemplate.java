/**
 * Created: Apr 11, 2013 3:06:00 PM
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
package com.pearson.openideas.cq5.components.page;

import com.adobe.granite.security.user.UserProperties;
import com.crownpartners.cq.quickstart.core.component.AbstractComponent;
import com.pearson.openideas.cq5.components.utils.CookieUtils;
import com.pearson.openideas.cq5.components.utils.UrlUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.servlet.http.Cookie;
import javax.servlet.jsp.PageContext;

/**
 * The Master template to help make sure all the templates are consistent.
 * 
 * @version 2.0
 * 
 * @author Todd Guerra
 */
public class OpenIdeasMasterTemplate extends AbstractComponent {

    private static final Logger log = LoggerFactory.getLogger(OpenIdeasMasterTemplate.class);

    private String pageTitle;
    private String[] accounts;
    private boolean loggedIn;
    private String loggedInName;
    private String error;

    private static final String PAGETITLEPREFIX = "Pearson - Open Ideas: ";

    /**
     * Constructor sets the page context.
     * 
     * @param pageContext
     *            The page context
     */
    public OpenIdeasMasterTemplate(final PageContext pageContext) {
        super(pageContext);
    }

    /**
     * init method doesn't do a whole lot right now.
     */
    public void init() {
        log.debug("master page template init called for page {}", getCurrentPage().getPath());
        CookieUtils cookieUtils = new CookieUtils();

        String logout = getRequestParameter("logout");
        log.debug("Should we try to logout? " + logout);

        if (StringUtils.isNotBlank(logout)) {
            Cookie cookie = cookieUtils.getCookie(getSlingRequest(), "login-token");
            cookie.setPath("/");
            cookie.setMaxAge(0);

            Cookie nameCookie = cookieUtils.getCookie(getSlingRequest(), "login-name");
            if (nameCookie != null) {
                nameCookie.setPath("/");
                nameCookie.setMaxAge(0);
                getSlingResponse().addCookie(nameCookie);
            }
            getSlingResponse().addCookie(cookie);

            UrlUtils.logoutRedirect(getCurrentPage(), getSlingScriptHelper(), getSlingRequest(), getSlingResponse(),
                    true);

            loggedIn = false;
        } else {
            log.debug("cookie value for this page: " + cookieUtils.getCookieValue(getSlingRequest(), "login-token"));
            if (StringUtils.isNotBlank(cookieUtils.getCookieValue(getSlingRequest(), "login-token"))) {
                loggedIn = true;
            }

            log.debug("Are we logged in? " + loggedIn);
        }

        pageTitle = PAGETITLEPREFIX + getProperties().get("jcr:title", "");
        accounts = getPageProperties().getInherited("accounts", String[].class);

        if (loggedIn) {
            UserProperties props = getSlingRequest().adaptTo(UserProperties.class);

            try {
                loggedInName = props.getProperty("givenName");
                log.debug("Who is logged in? " + loggedInName);

            } catch (RepositoryException e) {
                log.error("error pulling first name", e);
            }
        } else {
            UrlUtils.logoutRedirect(getCurrentPage(), getSlingScriptHelper(), getSlingRequest(), getSlingResponse(),
                    false);
        }

        if (StringUtils.isNotBlank(getRequestParameter("j_reason"))) {
            error = "Login failed, please try again";
        }
    }

    /**
     * Get the pageTitle.
     * 
     * @return The pageTitle
     */
    public String getPageTitle() {
        return pageTitle;
    }

    /**
     * Set the pageTitle.
     * 
     * @param pageTitle
     *            The pageTitle to set
     */
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    /**
     * Get the accounts.
     * 
     * @return The accounts
     */
    public String[] getAccounts() {
        return accounts;
    }

    /**
     * Set the accounts.
     * 
     * @param accounts
     *            The accounts to set
     */
    public void setAccounts(String[] accounts) {
        this.accounts = accounts;
    }

    /**
     * Gets loggedIn.
     * 
     * @return Value of loggedIn.
     */
    public Boolean getLoggedIn() {
        return loggedIn;
    }

    /**
     * Sets new loggedIn.
     * 
     * @param loggedIn
     *            New value of loggedIn.
     */
    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * Gets loggedInName.
     * 
     * @return Value of loggedInName.
     */
    public String getLoggedInName() {
        return loggedInName;
    }

    /**
     * Sets new loggedInName.
     * 
     * @param loggedInName
     *            New value of loggedInName.
     */
    public void setLoggedInName(String loggedInName) {
        this.loggedInName = loggedInName;
    }

    /**
     * Gets error.
     * 
     * @return Value of error.
     */
    public String getError() {
        return error;
    }

    /**
     * Sets new error.
     * 
     * @param error
     *            New value of error.
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Get the pagetitleprefix.
     * 
     * @return The pagetitleprefix
     */
    public static String getPagetitleprefix() {
        return PAGETITLEPREFIX;
    }

}
