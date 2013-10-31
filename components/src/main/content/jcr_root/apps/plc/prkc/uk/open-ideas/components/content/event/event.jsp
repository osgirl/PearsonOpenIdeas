<%--

  event component.

  The component creates an event used on the events page

--%><%
%><%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%>
<%@page session="false" import="com.pearson.openideas.cq5.components.content.PearsonEvent" %>
<%%><%
		PearsonEvent event = new PearsonEvent(pageContext);
		pageContext.setAttribute("event", event);
%>
<div class="event-item">
    <div class="event-image"><c:if test="${not empty event.image}">
	   <% event.getImage().draw(out); %>
	</c:if></div>
    <div class="event-right-section">
        <span class="event-name">${event.name}</span><br />
        <span>Date: </span><span>${event.date}</span><br />
        <span>Location: </span><span>${event.location}</span><br />
        <span>Country: </span><span>${event.country}</span><br />
        <p class="event-summary">${event.description}</p>
        <c:if test="${not empty event.url}">
	        <a class="event-url" target="_blank" href="${event.url}">
	        <c:choose>
			      <c:when test="${not empty event.urlText}">
			      	${event.urlText}
			      </c:when>	
			      <c:otherwise>
			       ${event.name}
			      </c:otherwise>
			</c:choose>
	        </a>
	    </c:if>
    </div>
</div>