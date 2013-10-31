<%--
    Post handler for sending a custom email
--%>
<%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%>
<%@page session="false" import="com.pearson.openideas.cq5.components.form.SendContributorEmail" %>
<%
	SendContributorEmail sendEmail = new SendContributorEmail(pageContext);
    pageContext.setAttribute("sendEmail", sendEmail);

    response.sendRedirect("/content/plc/prkc/uk/open-ideas/en/contribute/thank-you.html");
%>
