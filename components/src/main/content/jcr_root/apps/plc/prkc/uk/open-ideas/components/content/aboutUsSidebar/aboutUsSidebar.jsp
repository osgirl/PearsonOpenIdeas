<%--

  About Us Sidebar Component component.

  Component 

--%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@page session="false" import="com.pearson.openideas.cq5.components.content.AboutUsSidebar"%><%
%><%
	AboutUsSidebar about = new AboutUsSidebar(pageContext);
    pageContext.setAttribute("about", about);
%>
<div class="about-us-sidebar-item">
	<div class="about-us-sidebar-image">
		<a href="${about.url}">
		    <c:if test="${not empty about.image}">
		           <% about.getImage().draw(out); %>
		    </c:if>
	    </a>
	</div>
	<c:choose>
	    <c:when test="${empty about.text}">
	        <h3>DOUBLE CLICK HERE TO ADD TEXT</h3>
	    </c:when>
	    <c:otherwise>
	        <div class="about-sidebar-text">${about.txt}<a href="${about.url}">${about.text}</a></div>
	    </c:otherwise>
	</c:choose>
</div>