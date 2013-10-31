<%
%><%@ page session="false" %><%
%><%@ page import="java.util.List,
                   org.apache.commons.lang3.StringEscapeUtils,
                   org.apache.sling.api.resource.Resource,
                   com.day.cq.wcm.foundation.forms.FormsConstants,
                   com.day.cq.wcm.foundation.forms.FormResourceEdit" %><%
%><%@ taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0" %><%
%><sling:defineObjects/><%

    String returnToPage = slingRequest.getParameter("returnToPage");
    if (returnToPage != null) { %>
<input type="hidden" name="returnToPage" value="<%= returnToPage %>">
<%
    }
%>
