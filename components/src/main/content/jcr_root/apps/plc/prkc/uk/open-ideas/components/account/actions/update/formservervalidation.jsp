<%@page session="false" %><%
%><%@page import="java.util.regex.Matcher,
                java.util.regex.Pattern,
                java.util.ResourceBundle,
                java.util.Calendar,
                java.util.Date,
                java.text.SimpleDateFormat,
                java.text.DateFormat,
                com.day.cq.wcm.foundation.forms.FieldHelper,
                com.day.cq.wcm.foundation.forms.FieldDescription,
                com.day.cq.wcm.foundation.forms.FormsHelper,
                com.day.cq.wcm.foundation.forms.ValidationInfo,
                org.apache.sling.api.resource.ResourceResolver,
                javax.jcr.Session,
                javax.jcr.Node,
                javax.jcr.NodeIterator,
                javax.jcr.query.*,
                org.apache.sling.jcr.api.SlingRepository,
                java.util.HashMap,
                org.apache.jackrabbit.api.JackrabbitSession,
                org.apache.jackrabbit.api.security.user.User,
                org.apache.jackrabbit.api.security.user.UserManager"%>
<%
%><%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0" %><%
%><sling:defineObjects/><%
    
    SlingRepository repository = sling.getService(SlingRepository.class);
    Session session = repository.loginAdministrative(null);
    
    // Get password reset limit and message
    Node resourceNode = session.getNode(resource.getPath()); 
    String loggedInMessage = resourceNode.getProperty("loggedInMessage").getString();    
    
    // Get logged in user Id
    String loggedInUserId = "";
    com.day.cq.security.profile.Profile userProfile = resourceResolver.adaptTo(com.day.cq.security.profile.Profile.class);
    if (userProfile != null) {
        loggedInUserId = userProfile.getAuthorizable().getID();
    }
    
    // Session logout
    session.logout();
                 
    
    if (loggedInUserId.equals("anonymous") == false) {
    
        // Add validation errror message to form          
        ValidationInfo.createValidationInfo(slingRequest).addErrorMessage(null, loggedInMessage);
        
    }
               
                      

%>