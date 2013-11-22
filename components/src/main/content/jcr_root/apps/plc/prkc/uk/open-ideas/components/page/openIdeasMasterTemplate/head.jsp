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

  Default head script.

  Draws the HTML head with some default content:
  - includes the WCML init script
  - includes the head libs script
  - includes the favicons
  - sets the HTML title
  - sets some meta data

  ==============================================================================

--%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp" %><%
%><%@ page import="com.day.cq.commons.Doctype,
                   org.apache.commons.lang3.StringEscapeUtils" %><%
    String xs = Doctype.isXHTML(request) ? "/" : "";
    String favIcon = "/etc/designs/plc/prkc/uk/open-ideas/clientlibs/favicon.ico";
    if (resourceResolver.getResource(favIcon) == null) {
        favIcon = null;
    }
%>

  <head>
    <meta http-equiv="X-UA-Compatible" content="IE=10" />
    <meta name="viewport" content="width=768,maximum-scale=1.0">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"<%=xs%>>
    <meta http-equiv="keywords" content="<%= StringEscapeUtils.escapeHtml4(WCMUtils.getKeywords(currentPage, false)) %>"<%=xs%>>
    <meta http-equiv="description" content="<%= StringEscapeUtils.escapeHtml4(properties.get("jcr:description", "")) %>"<%=xs%>>   
    <cq:include script="headlibs.jsp"/>
    <cq:include script="/libs/wcm/core/components/init/init.jsp"/>
    <cq:includeClientLib categories="pearsonopenideas"/>
    <%-- <cq:includeClientLib categories="cq.collab.comments" /> --%>
    <% if (favIcon != null) { %>
    <link rel="icon" type="image/vnd.microsoft.icon" href="<%= favIcon %>"<%=xs%>>
    <link rel="shortcut icon" type="image/vnd.microsoft.icon" href="<%= favIcon %>"<%=xs%>>
    <% } %>
    <title>${template.pageTitle}</title>

    <%--Adding Google Analytics before /head so we can track metrics--%>
    <script type="text/javascript">

        /*
        var _gaq = _gaq || [];
        _gaq.push(['_setAccount', 'UA-45636952-1']);
        _gaq.push(['_setDomainName', 'none']);
        _gaq.push(['_setAllowLinker', 'true']);
        _gaq.push(['_trackPageview']);

        (function() {
          var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
          ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
          var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
        })();
        */
    </script>

    <cq:include path="cloudservices" resourceType="cq/cloudserviceconfigs/components/servicecomponents"/>

</head>