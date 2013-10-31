<%--

  Related Articles component.

  This component appears as a sidebar on the right side Article Template

--%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@page session="false" import="com.day.cq.wcm.foundation.Image, com.pearson.openideas.cq5.components.content.RelatedArticles" %><%
%><%

	  	RelatedArticles relatedArticles = new RelatedArticles(pageContext);
	    pageContext.setAttribute("relatedArticles", relatedArticles);

%>
<aside class="sidebar">
	<c:if test="${not empty relatedArticles.listOfArticles}">
       <div class="related">
          <div class="relatedarticle">RELATED ARTICLES 
             <img class="related-article-img" src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/chainlink.png"/>
          </div>
       </div>
	</c:if>
	<c:forEach var="article" items="${relatedArticles.listOfArticles}" begin="0" end="4" step="1">
		<a href="${article.url}.html"><div class="article_item">
		   <c:set var="articleImage" value="${article.image}"/>
		   <div class="related-image-wrapper"><%
		        Image image = (Image) pageContext.getAttribute("articleImage");
		        image.draw(out);
		    %></div>
	        <div class="asidetag">
              <div class="tag">
                 <p class="taghead">${article.category}</p>
                 <p class="tagcontent">${article.theme}<img src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/tag.png" alt="tag"/></p>
             </div>
            </div>
			<h2 class="relatedArticleTitle">${article.articleTitle}</h2>
		</div></a>
	</c:forEach>
</aside>