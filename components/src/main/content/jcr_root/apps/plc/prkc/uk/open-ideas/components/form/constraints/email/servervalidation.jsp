<%@page session="false" %><%
%><%@page import="java.util.regex.Matcher,
                java.util.regex.Pattern,
                java.util.ResourceBundle,
                com.day.cq.wcm.foundation.forms.FieldHelper,
                com.day.cq.wcm.foundation.forms.FieldDescription,
                com.day.cq.wcm.foundation.forms.FormsHelper,
                com.day.cq.wcm.foundation.forms.ValidationInfo,
                org.apache.sling.api.resource.ResourceResolver,
                javax.jcr.Session,
                javax.jcr.NodeIterator,
                javax.jcr.query.*,
                org.apache.sling.jcr.api.SlingRepository,
                com.pearson.openideas.cq5.components.utils.UserUtils"%><%
%><%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0" %><%
%><sling:defineObjects/><%

    final ResourceBundle resBundle = slingRequest.getResourceBundle(request.getLocale());

    final Pattern p = Pattern.compile("^[^@]+([@]{1})[0-9a-zA-Z\\._-]+([\\.]{1})[0-9a-zA-Z\\._-]+$");
    final FieldDescription desc = FieldHelper.getConstraintFieldDescription(slingRequest);
    final String[] values = request.getParameterValues(desc.getName());
    if ( values != null ) {
        for(int i=0; i<values.length; i++) {
            final Matcher m = p.matcher(values[i]);
            if ( !m.matches() ) {
                if ( desc.isMultiValue() ) {
                    ValidationInfo.addConstraintError(slingRequest, desc, i);
                } else {
                    ValidationInfo.addConstraintError(slingRequest, desc);                      
                }
            } else {          
                try {            
                
                    SlingRepository repos = sling.getService(SlingRepository.class);
                    Session adminSession = null;
                    adminSession = repos.loginAdministrative(null);
                
                    // Get logged in user Id
                    String loggedInUserId = "";
                    com.day.cq.security.profile.Profile userProfile = resourceResolver.adaptTo(com.day.cq.security.profile.Profile.class);
                    if (userProfile != null) {
                        loggedInUserId = userProfile.getAuthorizable().getID();
                    }
                
                    // Check user exists (based on email)
                    UserUtils userUtils = new UserUtils();
                    boolean userExists = userUtils.checkUserExists(adminSession, loggedInUserId,"/home/users/plc/prkc/openideas/pearson-open-ideas", values[i]);
                    
                    adminSession.logout();
                    
                    if ( userExists ) {
                        ValidationInfo.createValidationInfo(slingRequest).addErrorMessage(desc.getName(), resBundle.getString("User already exists with specified email address")); 
                    }
                    
                } catch (Exception e) {
                
                    // Log error
                    log.error(e.getMessage());
                    
                    // Add default validation message to stop form being processed
                    ValidationInfo.createValidationInfo(slingRequest).addErrorMessage(desc.getName(), resBundle.getString("Error validating email address"));                     
                }           
            }
        }
    } 
%>