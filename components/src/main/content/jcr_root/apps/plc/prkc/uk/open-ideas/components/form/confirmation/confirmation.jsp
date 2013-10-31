<%--

  Form Confirmation Text  component.

  Component to render and display form confirmation text such as "thank you" or "form submitted"

--%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@page session="false" import="com.pearson.openideas.cq5.components.form.Confirmation"%><%
%><%
	Confirmation confirmation = new Confirmation(pageContext);
	pageContext.setAttribute("confirmation", confirmation);
%>
<div class="title-separator">
<h1 class="landing-title">${confirmation.title}</h1>
</div>
<p>${confirmation.text}</p>