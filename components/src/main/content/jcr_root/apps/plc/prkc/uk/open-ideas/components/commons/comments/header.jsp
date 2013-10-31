<%@ page import="com.adobe.cq.social.commons.CommentSystem,
                     com.day.cq.i18n.I18n,
                     com.day.cq.wcm.api.WCMMode,
                     java.util.Locale,
                     java.util.ResourceBundle" %><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%

    I18n i18n = new I18n(slingRequest.getResourceBundle(currentPage.getLanguage(false)));
%>

<h2><%= i18n.get("Comments") %></h2>
