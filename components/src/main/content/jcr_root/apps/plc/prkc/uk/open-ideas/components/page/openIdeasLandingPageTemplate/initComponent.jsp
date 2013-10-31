<%--
  Copyright 1997-2008 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

  ==============================================================================

  Component Initialization Script

  Initialized the template components model class and sets it into the page context as request
  scope for easy access from other rendering scripts.

  ==============================================================================

--%><%@page session="false"
            contentType="text/html; charset=utf-8"
            import="com.pearson.openideas.cq5.components.page.OpenIdeasLandingPageTemplate" %><%
%><%@taglib prefix="cq" uri="http://www.day.com/taglibs/cq/1.0" %><%
%><cq:defineObjects/>
<%
	OpenIdeasLandingPageTemplate template = new OpenIdeasLandingPageTemplate(pageContext);
	pageContext.setAttribute("template",template, PageContext.REQUEST_SCOPE);
 %>