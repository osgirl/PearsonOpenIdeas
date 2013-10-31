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
                javax.jcr.Node,
                javax.jcr.NodeIterator,
                javax.jcr.query.*,
                org.apache.sling.api.resource.Resource,
                org.apache.sling.jcr.api.SlingRepository"%><%
%><%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0" %><%
%><sling:defineObjects/><%

    ResourceResolver resolver = slingRequest.getResourceResolver();
    Session session = resolver.adaptTo(Session.class);

    final ResourceBundle resBundle = slingRequest.getResourceBundle(request.getLocale());

    final FieldDescription desc = FieldHelper.getConstraintFieldDescription(slingRequest);

    final String name = FormsHelper.getParameterName(resource);
    final String nameConfirm = name + "_confirm";
    
    final String password = request.getParameter(name);
    final String passwordConfirm = request.getParameter(nameConfirm);     
        
    // Get password field node
    Node resourceNode = session.getNode(resource.getPath());    
  
    // Get password format regular expession
    String passwordFormatRegEx = "";
    try {
        passwordFormatRegEx  = resourceNode.getProperty("passwordFormatRegEx").getString();
    } catch (javax.jcr.RepositoryException e) {
        // Do nothing
    }

    // Get password format message
    String passwordFormatMessage = "";
    try {
        passwordFormatMessage = resourceNode.getProperty("passwordFormatMessage").getString();
    } catch (javax.jcr.RepositoryException e) {
        // Do nothing
    }  
    
    final Pattern p = Pattern.compile(passwordFormatRegEx);
    final Matcher m = p.matcher(password);
    
    // Check if passwords match (password and confirm password)
    if (password.equals(passwordConfirm) == false) {
        // Show error message
        ValidationInfo.addConstraintError(slingRequest, desc); 
    } else if (m.matches() == false) {
        ValidationInfo.createValidationInfo(slingRequest).addErrorMessage(desc.getName(), passwordFormatMessage); 
    }
       
%>