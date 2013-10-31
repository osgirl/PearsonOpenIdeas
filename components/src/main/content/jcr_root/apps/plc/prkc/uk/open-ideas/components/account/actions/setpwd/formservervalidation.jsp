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

    String noUserIdMessage = resourceNode.getProperty("noUserIdMessage").getString();
    String noResetPasswordKeyMessage = resourceNode.getProperty("noResetPasswordRequested").getString();
    String invalidResetPasswordKeyMessage = resourceNode.getProperty("invalidResetPasswordKeyMessage").getString(); 
    String noResetPasswordRequestedMessage = resourceNode.getProperty("noResetPasswordRequestedMessage").getString();
    long resetPasswordLimitHours = resourceNode.getProperty("resetPasswordLimitHours").getLong();
    String resetPasswordLimitMessage = resourceNode.getProperty("resetPasswordLimitMessage").getString();
               
    // Ge user id and key from hidden form fields    
    final String key = request.getParameter("ky") == null ? null : slingRequest.getRequestParameter("ky").getString();
    final String uid = request.getParameter("uid") == null ? null : slingRequest.getRequestParameter("uid").getString();
    log.debug("uid: " + uid);
    log.debug("key: " + key);
            
    // Change password for user who has received a reset password email
    final JackrabbitSession jrSession = JackrabbitSession.class.cast(session);
    final UserManager um = jrSession.getUserManager();
    
    // Get user
    User user = null;
    Node userNode = null;
    if (uid != null) {
    
        // Get user from user id supplier
        user = User.class.cast(um.getAuthorizable(uid));
        
        // Get user properties
        if (user != null) {
            userNode = jrSession.getNode(user.getPath()); 
        }
    }
    
    
    Calendar compareResetPasswordDateTime = null;
    int differceHours = 0;
    String compareResetPasswordKey = "";
    if (userNode != null && userNode.hasProperty("RESETPASSWORDATETIME")) {
    
        // Get reset password key
        compareResetPasswordKey = userNode.getProperty("RESETPASSWORDKEY").getString();
    
        // Get reset password date time on user node to compare time between requesting reset and changing password
        compareResetPasswordDateTime = userNode.getProperty("RESETPASSWORDATETIME").getDate();
        
        // Get current date
        Calendar currentDateTime = Calendar.getInstance();
        
        // Compare dates
        long differceMilliseconds = currentDateTime.getTimeInMillis() - compareResetPasswordDateTime.getTimeInMillis();
        differceHours = (int) (differceMilliseconds / (1000*60*60));
 
    }           
               
    
    if (uid == null) {
    
        // Add validation errror message to form          
        ValidationInfo.createValidationInfo(slingRequest).addErrorMessage(null, noUserIdMessage );
        
    } else if (key == null) {
    
        // Add validation errror message to form          
        ValidationInfo.createValidationInfo(slingRequest).addErrorMessage(null, noResetPasswordKeyMessage );

    } else if (key.equals(compareResetPasswordKey) == false) {
    
        // Add validation errror message to form          
        ValidationInfo.createValidationInfo(slingRequest).addErrorMessage(null, invalidResetPasswordKeyMessage  );
    
    
    } else if (compareResetPasswordDateTime == null) {
    
        // Add validation errror message to form          
        ValidationInfo.createValidationInfo(slingRequest).addErrorMessage(null, noResetPasswordRequestedMessage);    
    
    } else if (differceHours >= resetPasswordLimitHours) {
    
        // Add validation errror message to form          
        ValidationInfo.createValidationInfo(slingRequest).addErrorMessage(null, resetPasswordLimitMessage);    
    
    } 
      
     // Logout of session          
     session.logout();                 

%>