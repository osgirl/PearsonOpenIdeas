<%
%><%@ page session="false" %><%
%><%@ page import="java.util.List,
                   org.apache.commons.lang3.StringEscapeUtils,
                   org.apache.sling.api.resource.Resource,
                   com.day.cq.wcm.foundation.forms.FormsConstants,
                   com.day.cq.wcm.foundation.forms.FormResourceEdit,
                   com.day.cq.security.profile.Profile" %><%
%><%@ taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0" %><%
%><sling:defineObjects/><%

    // Get user profile of logged in user
    final Profile currentProfile = slingRequest.adaptTo(Profile.class);
    
    // Get profile resource from profile path
    Resource profileResrouce = resourceResolver.getResource(currentProfile.getPath());
    
    if (profileResrouce != null) { %>
<input type="hidden" name="<%= FormResourceEdit.RESOURCES_PARAM %>" value="<%= StringEscapeUtils.escapeHtml4(profileResrouce.getPath()) %>"><%
    }

    String redirectTo = slingRequest.getParameter("redirectTo");
    if (redirectTo != null) { %>
<input type="hidden" name="<%= FormsConstants.REQUEST_PROPERTY_REDIRECT %>" value="<%= StringEscapeUtils.escapeHtml4(redirectTo) %>">
<%
    }
%>
