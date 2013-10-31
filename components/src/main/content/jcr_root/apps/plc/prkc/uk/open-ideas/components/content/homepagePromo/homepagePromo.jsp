<%--

  Homepage Promo Box component.

  Promo box for the homepage (size handled by CSS)

--%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@page session="false" import="com.pearson.openideas.cq5.components.content.HomepagePromo"%><%
%><%
	HomepagePromo homepagePromo = new HomepagePromo(pageContext);
    pageContext.setAttribute("homepagePromo", homepagePromo);
%>

<section class="promobox-wrapper clearfix">
	<c:if test="${homepagePromo.authorMode}">
	      <p>Double click here to edit (this will disappear outside of edit mode)</p>
	</c:if>
	<a class="homepage-promo-link" href="${homepagePromo.url}">
	    <c:if test="${not empty homepagePromo.image}">
	           <% homepagePromo.getImage().draw(out); %>
	    </c:if>
	    <div class="articletag">
		    <c:if test="${not empty homepagePromo.theme}">
		    <div class="tag">
		        <p class="taghead">${homepagePromo.category}</p>
		        <p class="tagcontent">${homepagePromo.theme}<img src="/etc/designs/plc/prkc/uk/open-ideas/clientlibs/img/tag.png" alt="tag"/></p>
		    </div>
		    </c:if>
	
	    </div>
	    <c:choose>
		    <c:when test="${not empty homepagePromo.title}">
		        <p class="articletext">${homepagePromo.title}</p>
		    </c:when>
		    <c:otherwise>
	           <p class="articletext">${homepagePromo.promoText}</p>
	    	</c:otherwise>
	    </c:choose>	
	</a>
</section>