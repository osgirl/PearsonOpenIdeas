<%--
  Copyright 1997-2010 Day Management AG
  Barfuesserplatz 6, 4001 Basel, Switzerland
  All Rights Reserved.

  This software is the confidential and proprietary information of
  Day Management AG, ("Confidential Information"). You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered into
  with Day.

  ==============================================================================

  Default header script 
  
  Simply includes the header component relevant to the given page type. 

  ==============================================================================

--%>
<%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp" %>
<%@page session="false"%>

    <!--[if lt IE 7]>
    <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
    <![endif]-->

    <cq:include path="navigationMenu" resourceType="/apps/plc/prkc/uk/open-ideas/components/content/NavigationMenuComponent" />
