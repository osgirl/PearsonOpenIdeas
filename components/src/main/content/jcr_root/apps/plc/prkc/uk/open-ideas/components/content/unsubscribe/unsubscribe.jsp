<%--

  Unsubscribe component.

  This component allows the user to visit the page this component is placed on to unsubscribe from receiving more emails.

--%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@page session="false" import="com.pearson.openideas.cq5.components.content.Unsubscribe"%><%
%><%
	Unsubscribe unsubscribe = new Unsubscribe(pageContext);
	pageContext.setAttribute("unsubscribe", unsubscribe);
%>