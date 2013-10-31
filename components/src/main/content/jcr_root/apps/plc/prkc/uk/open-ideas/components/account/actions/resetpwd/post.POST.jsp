<%@page session="false" %><%
%><%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.net.URL" %>
<%@ page import="javax.jcr.Session" %>
<%@ page import="org.apache.sling.api.request.RequestParameter" %>
<%@ page import="org.apache.sling.api.resource.ResourceUtil" %>
<%@ page import="org.apache.sling.api.resource.ValueMap" %>
<%@ page import="org.apache.sling.jcr.api.SlingRepository" %>
<%@ page import="com.day.cq.security.AccountManager" %>
<%@ page import="com.day.cq.security.AccountManagerFactory" %>
<%@ page import="com.day.cq.security.Authorizable" %>
<%@ page import="com.day.cq.security.User" %>
<%@ page import="com.day.text.Text"%>
<%@ page import="com.day.cq.wcm.foundation.forms.FormsHelper" %>
<%@ page import="java.util.UUID" %>
<%@ page import="javax.jcr.Node" %>
<%@ page import="com.day.cq.commons.date.DateUtil" %>
<%@ page import="com.pearson.openideas.cq5.components.utils.UserUtils" %>
<%@ page import="com.pearson.openideas.cq5.components.services.WorkflowService" %>
<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0" %><%!

    final String LOGIN = "rep:userId";

%><sling:defineObjects/><% 

    final ValueMap properties = ResourceUtil.getValueMap(resource); 
    final SlingRepository repos = sling.getService(SlingRepository.class);
    final AccountManagerFactory af = sling.getService(AccountManagerFactory.class);
    Session session = null;
    String error = null;
    try {
        session = repos.loginAdministrative(null);
        final AccountManager am = af.createAccountManager(session);
              
        UserUtils userUtils = new UserUtils();
        final String email = slingRequest.getRequestParameter("email").getString();  
        final String usersPath =  properties.get("usersPath", "");    
        
        // Look up userid from email address
        String userId = null;
        try {
            userId = userUtils.findUserId(session, usersPath , email);    
        } catch(Exception e) {
            // Log error
            error = e.getMessage();
        }        
            
        final String auth = userId ==null? null : userId;
        if (auth!=null) {
            Authorizable authorizable = am.findAccount(auth);
            String changePwdPage = properties.get("changePwdPage", null);

            if (authorizable != null && changePwdPage != null && authorizable.isUser()) {
                if (!changePwdPage.endsWith(".html")) {
                    changePwdPage += ".html";
                }
                URL url = new URL(request.getRequestURL().toString().replace("httpss", "https"));
                //am.requestPasswordReset((User)authorizable, new URL(url.getProtocol(), url.getHost(), url.getPort(), changePwdPage));

                // Set change password url 
                URL chanegPasswordUrl = new URL(url.getProtocol(), url.getHost(), changePwdPage);
                
                // Generate UUID (key)
                final UUID uuid = UUID.randomUUID();
                final String resetPasswordKey = uuid.toString();
                
                // Get user node
                Node userNode = session.getNode(authorizable.getHomePath());
                
                // Add password reset key to user node
                userNode.setProperty("RESETPASSWORDKEY", resetPasswordKey);
                
                // Add password reset link to user node
                userNode.setProperty("RESETPASSWORDURL", chanegPasswordUrl.toString());
                
                // Add password reset time to user node
                DateUtil dateUtil = new DateUtil();
                userNode.setProperty("RESETPASSWORDATETIME", dateUtil.getNow() );
                         
                
                // Update USERACTION to RESETPASSWORD
                userNode.setProperty("USERACTION", "RESETPASSWORD");

                //get the profile node and if it has any failed login attempts, clear them
                Node profileNode = userNode.getNode("profile");
                if (profileNode.hasProperty("failedAttempts")) {
                    profileNode.setProperty("failedAttempts", 0);
                }
                           
                // Save session
                session.save();  
                
                // Reverse replicate user
                WorkflowService workflowService = sling.getService(WorkflowService.class);
                workflowService.startWorkflow("publish", "/etc/workflow/models/plc/prkc/uk/open-ideas/pearson-open-ideasresetpassword/jcr:content/model", userNode.getPath());
            }
        }
        } catch (Exception e) {
            error = e.getMessage();
        } finally {
            if (session!=null) {
                session.logout();
            }
        }
    if (error!=null) {
        log.error(error);
    } 
    String path = properties.get("thankyouPage", "");
    if ("".equals(path)) {
        FormsHelper.redirectToReferrer(slingRequest, slingResponse, new HashMap<String, String[]>());
    } else {
        if (path.indexOf(".")<0) {
            path += ".html"; 
        }
        response.sendRedirect(slingRequest.getResourceResolver().map(request, path));
    }
%>