package com.pearson.openideas.cq5.components.utils;

import java.util.Date;

import javax.servlet.http.Cookie;

/**
 * Utility class to help convert Cookie objects between Java Servlet Cookie's and Apache HttpClient Cookie's.
 * 
 * @author sangupta
 * @version 1.0
 * @since 30 Oct 2010
 */
public class ApacheCookieUtils {

    /**
     * Private constructor.
     */
    private ApacheCookieUtils() {
        // Do nothing
    }

    /**
     * Method to convert an Apache HttpClient cookie to a Java Servlet cookie.
     * 
     * @param apacheCookie
     *            the source apache cookie
     * @return a java servlet cookie
     */
    public static Cookie servletCookieFromApacheCookie(final org.apache.commons.httpclient.Cookie apacheCookie) {
        if (apacheCookie == null) {
            return null;
        }

        final String name = apacheCookie.getName();
        String value = apacheCookie.getValue();

        final Cookie cookie = new Cookie(name, value);

        // set the domain
        // value = apacheCookie.getDomain();
        // if(value != null) {
        // cookie.setDomain("." + value);
        // }

        // path
        value = apacheCookie.getPath();
        if (value != null) {
            cookie.setPath(value);
        }

        // secure
        cookie.setSecure(apacheCookie.getSecure());

        // comment
        value = apacheCookie.getComment();
        if (value != null) {
            cookie.setComment(value);
        }

        // version
        cookie.setVersion(apacheCookie.getVersion());

        // From the Apache source code, maxAge is converted to expiry date using the following formula
        // if (maxAge >= 0) {
        // setExpiryDate(new Date(System.currentTimeMillis() + maxAge * 1000L));
        // }
        // Reverse this to get the actual max age

        final Date expiryDate = apacheCookie.getExpiryDate();
        if (expiryDate != null) {
            final long maxAge = (expiryDate.getTime() - System.currentTimeMillis()) / 1000;
            // we have to lower down, no other option
            cookie.setMaxAge((int) maxAge);
        }

        // return the servlet cookie
        return cookie;
    }

    /**
     * Method to convert a Java Servlet cookie to an Apache HttpClient cookie.
     * 
     * @param cookie
     *            the Java servlet cookie to convert
     * @return the Apache HttpClient cookie
     */
    public static org.apache.commons.httpclient.Cookie apacheCookieFromServletCookie(final Cookie cookie) {
        if (cookie == null) {
            return null;
        }

        org.apache.commons.httpclient.Cookie apacheCookie = null;

        // get all the relevant parameters
        final String domain = cookie.getDomain();
        final String name = cookie.getName();
        final String value = cookie.getValue();
        final String path = cookie.getPath();
        final int maxAge = cookie.getMaxAge();
        final boolean secure = cookie.getSecure();

        // create the apache cookie
        apacheCookie = new org.apache.commons.httpclient.Cookie(domain, name, value, path, maxAge, secure);

        // set additional parameters
        apacheCookie.setComment(cookie.getComment());
        apacheCookie.setVersion(cookie.getVersion());

        // return the apache cookie
        return apacheCookie;
    }

    public static org.apache.commons.httpclient.Cookie[] apacheCookieFromServletCookies(final Cookie[] servletCookies) {

        final int numberOfCookies = servletCookies.length;

        org.apache.commons.httpclient.Cookie[] cookies = new org.apache.commons.httpclient.Cookie[numberOfCookies];

        for (int i = 0; i < numberOfCookies; i++) {
            cookies[i] = ApacheCookieUtils.apacheCookieFromServletCookie(servletCookies[i]);
        }

        return cookies;
    }

    public static Cookie[] servletCookieFromApacheCookies(final org.apache.commons.httpclient.Cookie[] apacheCookies) {

        final int numberOfCookies = apacheCookies.length;

        Cookie[] cookies = new Cookie[numberOfCookies];

        for (int i = 0; i < numberOfCookies; i++) {
            cookies[i] = ApacheCookieUtils.servletCookieFromApacheCookie(apacheCookies[i]);
        }

        return cookies;
    }

}