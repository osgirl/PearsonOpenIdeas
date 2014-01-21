<%--

  Navigation Menu Component component.

  The navigation menu that appears at the top of the page

--%><%
%>
<%@include file="/apps/plc/prkc/uk/open-ideas/global.jsp"%><%
%><%@page session="false" import="com.pearson.openideas.cq5.components.content.OpenIdeasNavigationMenu, com.day.cq.wcm.api.WCMMode" %>
<%
    String button1Text = properties.get("button1Text", String.class);
    String button2Text = properties.get("button2Text", String.class);

    String button1Link = properties.get("button1Link", String.class);
    String button2Link = properties.get("button2Link", String.class);

    String buttonSelect = properties.get("buttonSelect", String.class);
    String twoButtons = "twoButtons";
%>

<div class="button-area">
    <button onclick="location.href='<%=button1Link%>'">
        <%=button1Text%>
    </button>

    <c:set var="buttonSelect" value="<%=buttonSelect%>"/>
    <c:set var="twoButtons" value="<%=twoButtons%>"/>
    <c:if test="${buttonSelect == twoButtons}">
        <button onclick="location.href='<%=button2Link%>'">
            <%=button2Text%>
        </button>
    </c:if>
</div>