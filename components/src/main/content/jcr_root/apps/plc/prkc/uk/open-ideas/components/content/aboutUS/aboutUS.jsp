<%--

  About Us Component component.

  This is the main content component of the About US page

--%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%>
<%@page session="false" import="com.pearson.openideas.cq5.components.content.AboutUs" %><%
%>
<%
    AboutUs about = new AboutUs(pageContext);
    pageContext.setAttribute("about", about);
%>
<div class="about-us-content">
	<div class="about-title-container"><h1 class="about-us-title">ABOUT US</h1></div>
	<div class="about-us-image">
	    <c:if test="${not empty about.image}">
	           <% about.getImage().draw(out); %>
	    </c:if>
	</div>
	<c:choose>
	    <c:when test="${empty about.text}">
	        <h3>DOUBLE CLICK HERE TO ADD ABOUT TEXT</h3>
	    </c:when>
	    <c:otherwise>
	        <div class="articlebody">${about.text}</div>
	    </c:otherwise>
	</c:choose>
</div>