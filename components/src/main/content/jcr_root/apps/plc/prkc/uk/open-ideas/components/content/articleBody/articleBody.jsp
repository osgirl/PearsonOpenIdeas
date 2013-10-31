<%--

  Article Component.

--%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@page session="false" import="com.pearson.openideas.cq5.components.content.ArticleBody" %><%
%><%
    ArticleBody article = new ArticleBody(pageContext);
    pageContext.setAttribute("article", article);
%>

<div class="editorschoice">
<c:if test="${not empty article.editorsChoice}">
	<div class="inner">
		<div class="innerback">
				EDITORS CHOICE
                <img src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/star.png" alt="star" />
		</div>
	</div>
</c:if>
	<!--end of inner -->
	<c:if test="${not empty article.image}">
	   <% article.getImage().draw(out); %>
	</c:if>
	<c:if test="${not empty article.theme}">
	<div class="inner2">
		<div class="tag">
			<p class="taghead">${article.category}</p>
			<p class="tagcontent">${article.theme}<img src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/tag.png" alt="tag" />
			</p>
		</div>
	</div>
	</c:if>
</div>

<div class="publish">
	<c:choose>
		<c:when test="${empty article.newPublishDate}">
            ${article.originalPublishDate}
          </c:when>
		<c:otherwise>
            ${article.newPublishDate}
          </c:otherwise>
	</c:choose>
	<c:if test="${article.useLastModifiedDate}">
           - Last Updated: ${article.lastModifiedDate}
    </c:if>
    <c:if test="${not empty article.leadContributor}">
        <p class="article-lead-contributor"> Lead Contributor: ${article.leadContributor}</p>
   </c:if>
</div>
<h2 class="article-title">${article.articleTitle }</h2>
<c:choose>
    <c:when test="${empty article.text}">
        <h3>DOUBLE CLICK HERE TO GET  ARTICLE STARTED</h3>
    </c:when>
    <c:otherwise>
        <div class="articlebody">${article.text}</div>
    </c:otherwise>

</c:choose>

