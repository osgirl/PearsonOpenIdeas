<%@page session="false" %><%
%><%@page import="com.day.cq.wcm.foundation.forms.FormsHelper,
                  com.day.cq.wcm.foundation.forms.ValidationHelper,
                  com.adobe.granite.xss.XSSAPI,
                  org.apache.sling.api.resource.ResourceResolver,
                  javax.jcr.Session,
                  javax.jcr.Node"%><%
%><%@taglib prefix="cq" uri="http://www.day.com/taglibs/cq/1.0" %><%
%><cq:defineObjects/><%

    ResourceResolver resolver = slingRequest.getResourceResolver();
    Session session = resolver.adaptTo(Session.class);

    final String name = FormsHelper.getParameterName(resource);
    final String formId = FormsHelper.getFormId(slingRequest);
    
    XSSAPI xssAPI2 = bindings.getSling().getService(XSSAPI.class).getRequestSpecificAPI(slingRequest);    

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
    
          
%>
{   var result=false;
    var elem = document.forms["<%= xssAPI.encodeForJSString(formId) %>"].elements;
    var last, confirm;
    for (var i=0; i < elem.length; i++) {
       var name = elem[i].name;
       if (name=='<%= xssAPI.encodeForJSString(name) %>') {
            last = elem[i].value;       
       } else if (name=='<%= xssAPI.encodeForJSString(name) %>_confirm') {
            confirm = elem[i].value;
            break;
       }   
    }
    
    if(last!=confirm) {
        cq5forms_showMsg(<%
            %>'<%= xssAPI2.encodeForJSString(formId) %>',<%
            %>'<%= xssAPI2.encodeForJSString(name) %>_confirm',<%
            %>'<%= xssAPI2.encodeForJSString(ValidationHelper.getConstraintMessage(resource)) %>');
        return false; } 
        
       // Validate password format
        if(!last.match(/<%=passwordFormatRegEx %>/)){
         cq5forms_showMsg(<%
                      %>'<%= xssAPI2.encodeForJSString(formId) %>',<%
                     %>'<%= xssAPI2.encodeForJSString(name) %>_confirm',<%
                    %>'<%= passwordFormatMessage %>');
          return false;
        }   
        
   }