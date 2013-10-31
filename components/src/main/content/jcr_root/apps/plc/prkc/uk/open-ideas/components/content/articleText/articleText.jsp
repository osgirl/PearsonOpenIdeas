<%--

  Article Text component.

  Article Text component

--%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@page session="false" import="com.pearson.openideas.cq5.components.content.ArticleText" %><%
%><%
	ArticleText articleText = new ArticleText(pageContext);
    pageContext.setAttribute("articleText", articleText);
%>
<div class="articlebody">${articleText.text}</div>