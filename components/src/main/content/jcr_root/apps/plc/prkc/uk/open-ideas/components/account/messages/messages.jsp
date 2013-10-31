<%--

  Messages component.

  Messages component for English.com (Students)

--%>
<%@ page import="com.day.cq.wcm.api.WCMMode,
org.apache.sling.commons.json.JSONObject"%>
<%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@page session="false" %><%
%>

<%
if (WCMMode.fromRequest(request) != WCMMode.DISABLED) {
        %><cq:includeClientLib categories="ped.elt.global.englishdotcomstudents.widgets"/><%
    }
%>

<%

// Get message from reequest parameter
String[] message = request.getParameterValues("message");

// Get messages property
String[] messages = properties.get("messages", String[].class);

boolean messageOutput = false;

if (message != null && messages != null) {

    for (String msg : message) {
        for (String m : messages) {
            JSONObject json = new JSONObject(m);
            
            String code = json.getString("code");
            String text = json.getString("text");
            
            if (code.equals(msg)) {
                out.write("<li>" + text + "</li>");
                messageOutput = true;
            }
            
        }
    }
    
}

if (WCMMode.fromRequest(request) == WCMMode.EDIT && messageOutput == false) {
    out.write("<li>Messages</li>");
}
%>
