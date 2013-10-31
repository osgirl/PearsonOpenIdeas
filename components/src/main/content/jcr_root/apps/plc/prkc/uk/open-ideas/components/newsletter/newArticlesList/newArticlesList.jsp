<%--

  New Articles List component.

  Provides a list of articles released in the last week

--%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@page session="false" import="com.day.cq.wcm.foundation.Image, java.util.Enumeration, com.pearson.openideas.cq5.components.newsletter.NewArticlesList" %><%
%><%
	NewArticlesList newArticlesList = new NewArticlesList(pageContext);
    pageContext.setAttribute("newArticlesList", newArticlesList);
%>
<div style="background:#fcf5ea;">
<c:forEach var="entry" items="${newArticlesList.articlesByTheme}">
	<table style="background:#fcf5ea;border-collapse:collapse;width:100%;">
		<tr>
			<th colspan="4" style="color:#9D1C4A; font-size:18px; padding:2px; text-align:left; font-weight:normal;">${entry.key}</th>
			
		</tr>
	</table>
	<c:forEach var="article" items="${entry.value}">
	<table style="background:#fcf5ea;border-collapse:collapse;width:100%;">
		<tr>
			<th rowspan="4" style="width:18%;overflow:hidden;"><img style="width:190px;height:111px;" src="${article.newsletterImageUrl}" /></th>	
		</tr>
		<tr>
			<td style="vertical-align:top;text-align:left;color:black;font-size:14px;padding:0 0 0 10px;">${article.articleTitle}</td>
		</tr>
		<tr>
			<td style="text-align:left;color:black;font-size:14px;padding:0 0 0 10px;">${article.pubDate}</td>
		</tr>
		<tr>
			<td style="text-align:left;color:blue;font-size:14px;padding:0 0 0 10px;"><a style="text-decoration:underline; text-transform:uppercase;" href="${article.url}.html">READ ARTICLE > </a></br></td>
		</tr>
	</table>
	</c:forEach>
</c:forEach>
</div>

