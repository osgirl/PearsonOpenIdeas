<%@page session="false" %><%
%>
<%@ page import="com.day.cq.wcm.foundation.forms.FormsHelper" %>
<%@ page import="org.apache.sling.api.resource.ResourceUtil" %>
<%@ page import="org.apache.sling.api.resource.ValueMap" %>
<%@ page import="org.apache.sling.jcr.api.SlingRepository" %>
<%@ page import="javax.jcr.Session" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.jackrabbit.api.JackrabbitSession" %>
<%@ page import="org.apache.jackrabbit.api.security.user.User" %>
<%@ page import="org.apache.jackrabbit.api.security.user.UserManager" %>
<%@ page import="com.pearson.openideas.cq5.components.services.WorkflowService" %>
<%@ page import="com.pearson.openideas.cq5.components.utils.UserUtils" %>
<%@ page import="javax.jcr.Node" %>

<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0" %>
<sling:defineObjects/><%

    final ValueMap properties = ResourceUtil.getValueMap(resource);
    final String key = request.getParameter("ky") == null ? null : slingRequest.getRequestParameter("ky").getString();
    final String uid = request.getParameter("uid") == null ? null : slingRequest.getRequestParameter("uid").getString();
    final String pwd = request.getParameter("passwordreset") == null ? null : slingRequest.getRequestParameter("passwordreset").getString();

    Session session = null;
    String message = null;
    
    try {

        SlingRepository repository = sling.getService(SlingRepository.class);
        session = repository.loginAdministrative(null);
        UserUtils userUtils = new UserUtils();	
        message = "";
    	
        // Change password
        if (key == null && uid == null) {   
        
            // Change password of logged in user
            com.day.cq.security.User user = resourceResolver.adaptTo(com.day.cq.security.User.class);
            
            // Don't change password if the user is admin or anonymous
            if (user.getName().equals("admin") == false && user.getName().equals("anonymous") == false) {
                user.changePassword(pwd);
            }
            
            // Get user properties
            final Node userNode = session.getNode(user.getHomePath());             
                  
            // Update user node properties      
            userNode.setProperty("password", userUtils.encodePassword(pwd)); // Add encoded password for replicating to other publish instances
            
            userNode.setProperty("USERACTION", "MODIFY");    
            
             // Save session
            session.save();
            
            // Reverse replicate user
            WorkflowService workflowService = sling.getService(WorkflowService.class);
            workflowService.startWorkflow("publish", "/etc/workflow/models/reverse_replication/jcr:content/model", userNode.getPath());
               
            // Update message to pass back to user                            
            message  = "passwordUpdated";                             
                                  
        
        } else if (key != null && uid != null) {    
        
            // Change password for user who has received a reset password email
            final JackrabbitSession adminSession = JackrabbitSession.class.cast(session);
            final UserManager um = adminSession.getUserManager();
        
            // Get user
            final User user = User.class.cast(um.getAuthorizable(uid));
            
            // Get user properties
            final Node userNode = adminSession.getNode(user.getPath()); 
            
            // Get reset password key on user node to compare
            final String compareResetPasswordKey = userNode.getProperty("RESETPASSWORDKEY").getString();
            
            // Get reset password date time on user node to compare time between requesting reset and changing password
            final String compareResetPasswordDateTime = userNode.getProperty("RESETPASSWORDATETIME").getString();        
            
                 
            if (compareResetPasswordKey.equals(key)) {
            
                user.changePassword(pwd);
                
                javax.jcr.Value nullValue = null;
                
                   // Clear password reset values 
                userNode.setProperty("RESETPASSWORDKEY", nullValue );
                userNode.setProperty("RESETPASSWORDURL", nullValue );
                userNode.setProperty("RESETPASSWORDATETIME", nullValue );
                              
                userNode.setProperty("password", userUtils.encodePassword(pwd)); // Add encoded password for replicating to other publish instances
                                   
                userNode.setProperty("USERACTION", "MODIFY");
                
                // Save session
                session.save();
                
                // Reverse replicate user
                WorkflowService workflowService = sling.getService(WorkflowService.class);
                workflowService.startWorkflow("publish", "/etc/workflow/models/reverse_replication/jcr:content/model", userNode.getPath());
                      
                // Update message to pass back to user                            
                message  = "passwordUpdated";    
                                    
            } else {
            
                message  = "passwordUpdateError";
            
            }
        } else {
        
            message  = "passwordUpdateError";    
        
        }

    }
    catch (Exception e) {
        // Log error
        log.error("set password error: " + e.getMessage());
    }
    finally {
    	if (session != null) {
        	session.logout();
    	}
    }

    String path = properties.get("pwdChangedPage", "");
    
    // Remove existing query string from path
    if (path.indexOf("?") > -1) {
        path = path.substring(0, path.indexOf("?"));
    }   
    
    if ("".equals(path)) {
        FormsHelper.redirectToReferrer(slingRequest, slingResponse, new HashMap<String, String[]>());
    } else {
        if (path.indexOf(".") < 0) {
            path += ".html?message=" + message;
        }
        response.sendRedirect(path);
    }

%>