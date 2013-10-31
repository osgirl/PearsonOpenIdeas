<%
%><%@ page session="false" %><%
%><%@ page import="java.util.List,
                   org.apache.sling.api.resource.Resource,
                   com.day.cq.wcm.foundation.forms.FormResourceEdit,
                   com.day.cq.wcm.foundation.forms.FormsHelper,
                   com.day.cq.security.profile.Profile" %><%
%><%@ taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0" %><%
%><sling:defineObjects/><%

    // Get user profile of logged in user
    final Profile currentProfile = slingRequest.adaptTo(Profile.class);
    
    // Get profile resource from profile path
    Resource profileResrouce = resourceResolver.getResource(currentProfile.getPath());
    
    if (profileResrouce != null) {
        FormsHelper.setFormLoadResource(slingRequest, profileResrouce);
    }
%>