<%--

  Landing Page Component

--%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@page session="false" import="com.day.cq.wcm.foundation.Image, com.pearson.openideas.cq5.components.search.SearchResults"%><%
    SearchResults searchResults = new SearchResults(pageContext);
    pageContext.setAttribute("searchResults",searchResults);
%>
<div class="separator">
    <h1 class="landing-title">Search Results</h1>
</div>
<c:if test="${!searchResults.authorMode}">
	<c:choose>
	    <c:when test="${searchResults.totalMatches == 0}">
	    <div class="no-search-results">
	    	<%@include file="form.jsp"%>
		    <p class="total-results">Showing 0 of 0 Search Results</p>
		    <p class="no-results-text">There are no search results available for your search. Please try again.</p>
	    </div>
	    </c:when>
	    <c:otherwise>
	       <div class="search-results">
	    	 <%@include file="form.jsp"%>
	    	 <p class="total-results">Showing ${searchResults.matchesOnThisPage} of ${searchResults.totalMatches} Search Results</p>
	       </div>
	        <c:forEach var="page" items="${searchResults.pages}" varStatus="count">
	        <c:if test="${page.isValid}">
		        <div class="search-item search-item_${count.count}">
			        <div class="search-item-image">
			            <a href="${page.url}.html">
				        <c:choose>
					        <c:when test="${page.pageType eq 'article'}">
					           <c:set var="image" value="${page.image}"/>
					                <%
					                    Image image = (Image) pageContext.getAttribute("image");
					                    image.draw(out);
					                %>
					        </c:when>
					        <c:otherwise>
					            <img src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/openideasthumbnail.jpg" alt="Open Ideas Logo" />
					        </c:otherwise>
				        </c:choose>
				        </a>
			        </div>
			        <div class="search-item-information">
			            <a class="title-link" href="${page.url}.html"><h2 class="landing-article-title">${page.title}</h2></a>
			            ${page.excerpt}<a href="${page.url}.html">VIEW FULL ARTICLE</a><br />
			        </div>
		        </div>
		     </c:if>
	        </c:forEach>   
	    </c:otherwise>
	</c:choose>
	
	<c:if test="${searchResults.totalMatches gt searchResults.pagesize}">
	    <c:set var="pageNumber" value="${searchResults.pageNumber}" />
	    <c:set var="pageCount" value="${searchResults.pageCount}" />
	    <c:set var="pageString" value="${searchResults.params}" />
	    <ul id="pagination-clean">
	        <c:if test="${pageNumber != 1}">
	            <li class="previous"><a href="${pageString}${pageNumber-1}">Previous</a></li>
	        </c:if>
	        
	        <c:if test="${pageNumber > 1}">
	            <li><a href="${pageString}${pageNumber-1}">${pageNumber-1}</a></li>
	        </c:if>
	        
	        <li class="active">${pageNumber}</li>
	        
	        <c:forEach begin="${pageNumber+1}" end="${pageNumber+4}" varStatus="nPage">
	            <c:if test="${pageCount >= nPage.index}">
	                <li><a href="${pageString}${nPage.index}">${nPage.index}</a></li>
	            </c:if>
	        </c:forEach>
	        <c:if test="${pageCount > 1 && pageNumber < pageCount}">
	            <li class="next"><a href="${pageString}${pageNumber+1}">Next</a></li>
	        </c:if>
	    </ul>
	</c:if>
</c:if>