package com.pearson.openideas.cq5.components.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CookieUtils {

	public boolean removeCookie(final String cookieName, final HttpServletRequest request, final HttpServletResponse response){
		boolean cookieRemoved=false;

		final Cookie[] cookies = request.getCookies();
		if (cookies != null){
		  for (Cookie ck : cookies) {
		    if (cookieName.equals(ck.getName())) {
		    	ck.setPath("/");
		  	    ck.setMaxAge(0);
		 	    response.addCookie(ck);
		    	cookieRemoved=true;
			      break;
		    }
		  }
		}
		return cookieRemoved;
	}

	public Cookie getCookie(final HttpServletRequest request, final String cookieName){
		Cookie cook=null;

		final Cookie[] cookies = request.getCookies();
		if (cookies != null){
		  for (Cookie ck : cookies) {
		    if (cookieName.equals(ck.getName())) {
		    	cook=ck;
			    break;
		    }
		  }
		}
		return cook;
	}

	@SuppressWarnings("deprecation")
	public String getCookieValue (final HttpServletRequest request, final String cookieName, final boolean noDecode){
		String ckValue=null;
		final Cookie cookie = getCookie( request, cookieName );
		if (cookie!=null){
			if (noDecode) {
				ckValue = cookie.getValue();
			} else {
				ckValue = URLDecoder.decode(cookie.getValue());
			}
		}
		return ckValue;
	}

	public String getCookieValue (final HttpServletRequest request, final String cookieName){
		return getCookieValue(request, cookieName, false);
	}


	@SuppressWarnings("deprecation")
	public Cookie setCookie(final String cookieName, final String cookieVal, final HttpServletResponse response, final boolean noEncode) {
 	   Cookie cookie = null;
 	   if (noEncode) {
 		  cookie = new Cookie(cookieName, cookieVal);
 	   } else {
 		  cookie = new Cookie(cookieName, URLEncoder.encode(cookieVal));
 	   }
 	   cookie.setPath("/");
 	   cookie.setMaxAge(-1); // Default behaviour: the cookie is not stored persistently, and will be deleted when the user agent (web browser) exits.
 	   response.addCookie(cookie);
	   return cookie;
	}	
	
	public Cookie setCookie(final String cookieName, final String cookieVal, final HttpServletResponse response) {
		return setCookie(cookieName, cookieVal, response, false);
	}
	
}
